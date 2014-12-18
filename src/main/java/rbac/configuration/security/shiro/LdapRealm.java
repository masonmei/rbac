package rbac.configuration.security.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rbac.model.Role;
import rbac.model.User;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LdapRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(LdapRealm.class);

    private static final String USERDN_SUBSTITUTION_TOKEN = "{0}";

    private String userDnPrefix;
    private String userDnSuffix;

    private LdapContextFactory contextFactory;


    public LdapRealm() {
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(AuthenticationToken.class);
        this.contextFactory = new JndiLdapContextFactory();
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

        final CustomAuthenticationToken userToken = (CustomAuthenticationToken) token;
        String username = null;

        username = userToken.getUsername();

        if (username == null) {
            throw new UnknownAccountException("Username not provided");
        }

       LdapContextFactory ldapContextFactory = getContextFactory();

       LdapContext ctx = null;

       Set<Role> role =  new HashSet<Role>();

        try {
            ctx = ldapContextFactory.getLdapContext(username, String.valueOf(userToken.getPassword()));
            NamingEnumeration<?> usersQuery = ctx.search("ou=users,dc=elphita,dc=org", "(objectclass=*)", getSimpleSearchControls());
            while (usersQuery.hasMore())
            {
                SearchResult result = (SearchResult) usersQuery.next();
                Attributes attrs = result.getAttributes();
                System.out.println(attrs.get("cn"));
                System.out.println(attrs.get("gidnumber"));
                System.out.println(attrs.get("givenname"));
                System.out.println(attrs.get("homedirectory"));
                System.out.println(attrs.get("objectclass"));
                System.out.println(attrs.get("objectclass"));
                System.out.println(attrs.get("objectclass"));
                System.out.println(attrs.get("sn"));
                System.out.println(attrs.get("uid"));
                System.out.println(attrs.get("uidnumber"));
                System.out.println(attrs.get("userpassword"));
                userToken.setUsername(attrs.get("cn").get().toString());
                userToken.setGid(Integer.parseInt(attrs.get("gidnumber").get().toString()));

            }

            NamingEnumeration<?> groupsQuery = ctx.search("ou=groups,dc=elphita,dc=org", "(&(objectClass=posixGroup)(gidNumber="+userToken.getGid()+"))", getSimpleSearchControls());

            while (groupsQuery.hasMore())
            {
                SearchResult result = (SearchResult) groupsQuery.next();
                Attributes attrs = result.getAttributes();
                System.out.println(attrs.get("cn"));
                System.out.println(attrs.get("objectclass"));
                System.out.println(attrs.get("objectclass"));
                System.out.println(attrs.get("memberUid"));
                Role role_new = new Role();
                role_new.setId(role_new.getId());
                role_new.setName(attrs.get("cn").get().toString());
                role.add(role_new);
            }

        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            LdapUtils.closeContext(ctx);
        }

        User user = new User();
        user.setId(user.getId());
        user.setUsername(username);
        user.setRoles(role);

        System.out.println("Role [ "+ user.getRoles().iterator().next().getName()+" ]");

        return new SimpleAuthenticationInfo(new CustomPrincipal(user, user.getRoles()), userToken.getPassword(),
                ByteSource.Util.bytes(username), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Set roleNamesSmplInfo = new HashSet();
        for(Role role : ((CustomPrincipal) principals.getPrimaryPrincipal()).getRoles()){
            roleNamesSmplInfo.add(role.getName());
        }

        return new SimpleAuthorizationInfo(roleNamesSmplInfo);
    }

    protected static SearchControls getSimpleSearchControls(){
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchControls.setTimeLimit(30000);
        return searchControls;
    }
}
