package rbac.configuration.security.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import rbac.configuration.LoginDatabaseConfiguration;
import rbac.model.login.Permission;
import rbac.model.login.Role;
import rbac.model.login.User;
import rbac.repository.login.PermissionRepository;
import rbac.repository.login.RoleRepository;
import rbac.repository.login.UserRepository;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.*;

@Component
public class LdapRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(LdapRealm.class);

    private static final String USERDN_SUBSTITUTION_TOKEN = "{0}";

    private String userDnPrefix;
    private String userDnSuffix;

    @Autowired
    private LdapContextFactory contextFactory;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    public LdapRealm() {
        super();
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(AuthenticationToken.class);
        //this.contextFactory = new JndiLdapContextFactory();
    }
    
    @Autowired
    public LdapRealm(@Qualifier("hsqlInMemory") UserRepository userRepository, @Qualifier("hsqlInMemory") RoleRepository roleRepository, @Qualifier("hsqlInMemory") PermissionRepository permissionRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    protected String getUserDnPrefix() {
        return userDnPrefix;
    }


    protected String getUserDnSuffix() {
        return userDnSuffix;
    }


    public void setUserDnTemplate(String template) throws IllegalArgumentException {
        if (!StringUtils.hasText(template)) {
            String msg = "User DN template cannot be null or empty.";
            throw new IllegalArgumentException(msg);
        }
        int index = template.indexOf(USERDN_SUBSTITUTION_TOKEN);
        if (index < 0) {
            String msg = "User DN template must contain the '" +
                    USERDN_SUBSTITUTION_TOKEN + "' replacement token to understand where to " +
                    "insert the runtime authentication principal.";
            throw new IllegalArgumentException(msg);
        }
        String prefix = template.substring(0, index);
        String suffix = template.substring(prefix.length() + USERDN_SUBSTITUTION_TOKEN.length());
        if (log.isDebugEnabled()) {
            log.debug("Determined user DN prefix [{}] and suffix [{}]", prefix, suffix);
        }
        this.userDnPrefix = prefix;
        this.userDnSuffix = suffix;
    }


    public String getUserDnTemplate() {
        return getUserDn(USERDN_SUBSTITUTION_TOKEN);
    }


    protected String getUserDn(String principal) throws IllegalArgumentException, IllegalStateException {
        if (!StringUtils.hasText(principal)) {
            throw new IllegalArgumentException("User principal cannot be null or empty for User DN construction.");
        }
        String prefix = getUserDnPrefix();
        String suffix = getUserDnSuffix();
        if (prefix == null && suffix == null) {
            log.debug("userDnTemplate property has not been configured, indicating the submitted " +
                    "AuthenticationToken's principal is the same as the User DN.  Returning the method argument " +
                    "as is.");
            return principal;
        }

        int prefixLength = prefix != null ? prefix.length() : 0;
        int suffixLength = suffix != null ? suffix.length() : 0;
        StringBuilder sb = new StringBuilder(prefixLength + principal.length() + suffixLength);
        if (prefixLength > 0) {
            sb.append(prefix);
        }
        sb.append(principal);
        if (suffixLength > 0) {
            sb.append(suffix);
        }
        return sb.toString();
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public void setContextFactory(LdapContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }


    public LdapContextFactory getContextFactory() {
        return this.contextFactory;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        Object principal = token.getPrincipal();
        Object credentials = token.getCredentials();

        if (principal == null) {
            throw new UnknownAccountException("Username not provided");
        }

       LdapContextFactory ldapContextFactory = this.getContextFactory();

       LdapContext ctx = null;

       CustomAuthenticationToken userToken = new CustomAuthenticationToken();

        List<Role> roleSet = new ArrayList<Role>();
        List<Permission> permissionSetUser = new ArrayList<Permission>();
        List<Permission> permissionSetManager = new ArrayList<Permission>();
        List<Permission> permissionSetAdmin = new ArrayList<Permission>();

       User user = new User();

        try {
            ctx = ldapContextFactory.getLdapContext(principal, credentials);
            NamingEnumeration<?> usersQuery = ctx.search("ou=users,dc=elphita,dc=org", "(objectclass=*)", getSimpleSearchControls());
            while (usersQuery.hasMore())
            {
                SearchResult result = (SearchResult) usersQuery.next();
                Attributes attrs = result.getAttributes();

                userToken.setUsername(attrs.get("cn").get().toString());
                userToken.setGid(Integer.parseInt(attrs.get("gidnumber").get().toString()));

            }

            NamingEnumeration<?> rolesQuery = ctx.search("ou=roles,dc=elphita,dc=org", "(&(objectClass=posixGroup)(memberUid="+userToken.getUsername()+"))", getSimpleSearchControls());
            //Role role_new = null;
            while (rolesQuery.hasMore()){
                SearchResult resultRole = (SearchResult) rolesQuery.next();
                Attributes attrsRole = resultRole.getAttributes();


                NamingEnumeration<?> permissionQuery = ctx.search("ou=permissions,dc=elphita,dc=org", "(&(objectClass=posixGroup)(memberUid="+attrsRole.get("cn").get().toString()+"))", getPermissionsSimpleSearchControls());
                while (permissionQuery.hasMore()){
                    SearchResult resultPermissions = (SearchResult) permissionQuery.next();
                    Attributes attrsPermission = resultPermissions.getAttributes();

                    List<String> memberUid = new ArrayList<String>();
                    String rolesString = attrsRole.get("cn").get().toString();
                    String[] roleList = rolesString.split(",");
                    Permission permissionUser = new Permission();
                    Permission permissionManager = new Permission();
                    Permission permissionAdmin = new Permission();
                    for(int i=0; i<roleList.length; i++) {

                        if((roleList[i].toString().equals("ROLE_USER")) == true) {

                             System.out.println("PERMISSION USER"+ attrsPermission.get("cn").get().toString());
                             permissionUser.setName(attrsPermission.get("cn").get().toString());
                             permissionSetUser.add(permissionUser);
                             permissionRepository.saveAndFlush(permissionUser);


                        }else if((roleList[i].toString().equals("ROLE_MANAGER")) == true){
                            System.out.println("PERMISSION MANAGER"+ attrsPermission.get("cn").get().toString());
                            permissionManager.setName(attrsPermission.get("cn").get().toString());
                            permissionSetManager.add(permissionManager);
                            permissionRepository.saveAndFlush(permissionManager);

                        }else if((roleList[i].toString().equals("ROLE_ADMIN")) == true){

                            System.out.println("PERMISSION ADMIN"+ attrsPermission.get("cn").get().toString());

                            permissionAdmin.setName(attrsPermission.get("cn").get().toString());
                            permissionSetAdmin.add(permissionAdmin);
                            permissionRepository.saveAndFlush(permissionAdmin);
                        }


                    }




                }

                Role role_new = new Role();
                role_new.setName(attrsRole.get("cn").get().toString());
                if(role_new.getName().equals("ROLE_USER")){
                  role_new.setPermission(permissionSetUser);
                }else if(role_new.getName().equals("ROLE_MANAGER")){
                  role_new.setPermission(permissionSetManager);
                }else if(role_new.getName().equals("ROLE_ADMIN")){
                  role_new.setPermission(permissionSetAdmin);
                }
                roleSet.add(role_new);
                roleRepository.saveAndFlush(role_new);


            }

            user.setUsername((String) principal);
            user.setRoles(roleSet);
            userRepository.saveAndFlush(user);

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            LdapUtils.closeContext(ctx);
        }


        return new SimpleAuthenticationInfo(new CustomPrincipal(user, user.getRoles()), credentials,
                ByteSource.Util.bytes(principal.toString()), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Set roleNamesSmplInfo = new HashSet();
        for(Role role : ((CustomPrincipal) principals.getPrimaryPrincipal()).getRoles()){
            roleNamesSmplInfo.add(role.getName());
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNamesSmplInfo);

        for(Role rolePermissionFecth : ((CustomPrincipal) principals.getPrimaryPrincipal()).getRoles()) {
            List<Permission> permissionSet = (List<Permission>) rolePermissionFecth.getPermission();
            for(Permission permission : permissionSet) {
                String permissionName = permission.getName();
                info.addStringPermission(permissionName);
            }
        }

        System.out.println("INFO " +info.getStringPermissions().toString());
        return info;
    }

    protected static SearchControls getSimpleSearchControls(){
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchControls.setTimeLimit(30000);
        return searchControls;
    }

    protected static SearchControls getPermissionsSimpleSearchControls(){
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setTimeLimit(30000);
        return searchControls;
    }
}
