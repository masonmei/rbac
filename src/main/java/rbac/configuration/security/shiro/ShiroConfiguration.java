/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rbac.configuration.security.shiro;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class ShiroConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setLoginUrl("users/auth");
        shiroFilter.setSuccessUrl("users/list_user");
        shiroFilter.setUnauthorizedUrl("/forbidden");
        Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
        filterChainDefinitionMapping.put("/", "anon");
        filterChainDefinitionMapping.put("/users/auth", "anon");
        filterChainDefinitionMapping.put("/users/create_user", "anon");
        filterChainDefinitionMapping.put("/users/list_user", "authc,roles[ROLE_USER, ROLE_MANAGER]");
        //filterChainDefinitionMapping.put("/admin", "authc,roles[admin]");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
        shiroFilter.setSecurityManager(securityManager());
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("anon", new AnonymousFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("logout", new LogoutFilter());
        filters.put("roles", new RolesAuthorizationFilter());
        filters.put("user", new UserFilter());
        shiroFilter.setFilters(filters);
        System.out.println(shiroFilter.getFilters().size());
        return shiroFilter;
    }



  @Bean(name = "securityManager")
  public DefaultWebSecurityManager securityManager() {
    final DefaultWebSecurityManager securityManager
        = new DefaultWebSecurityManager();
    securityManager.setRealm(ldapRealm());
    //securityManager.setSessionManager(sessionManager());
    return securityManager;
  }

  @Bean
  public DefaultWebSessionManager sessionManager() {
    final DefaultWebSessionManager sessionManager
        = new DefaultWebSessionManager();
    sessionManager.setSessionDAO(sessionDao());
    sessionManager.setGlobalSessionTimeout(43200000); // 12 hours
    return sessionManager;
  }

  @Bean
  public SessionDAO sessionDao() {
    return new CustomSessionDao();
  }

    @Bean(name="ldapRealm")
    public LdapRealm ldapRealm(){
        LdapRealm ldapRealm = new LdapRealm();
        ldapRealm.setContextFactory(jndiLdapContextFactory());
        return ldapRealm;
    }

  @Bean
  public JndiLdapContextFactory jndiLdapContextFactory(){
        JndiLdapContextFactory jndiLdapContextFactory = new JndiLdapContextFactory();
        jndiLdapContextFactory.setUrl("ldaps://192.168.1.6:636");
        jndiLdapContextFactory.setSystemUsername("{0}");
        jndiLdapContextFactory.setSystemPassword("{0}");
        jndiLdapContextFactory.setAuthenticationMechanism("DIGEST-MD5");
        return jndiLdapContextFactory;
  }


  @Bean(name = "credentialsMatcher")
  public PasswordMatcher credentialsMatcher() {
    final PasswordMatcher credentialsMatcher = new PasswordMatcher();
    credentialsMatcher.setPasswordService(passwordService());
    return credentialsMatcher;
  }

  @Bean(name = "passwordService")
  public DefaultPasswordService passwordService() {
    return new DefaultPasswordService();
  }


  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
      DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
      defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
      AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
      authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
      return authorizationAttributeSourceAdvisor;
  }

}
