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
package rbac.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.RestController;

import rbac.configuration.security.AuthenticationToken;
import rbac.configuration.security.shiro.CustomAuthenticationToken;
import rbac.configuration.security.shiro.CustomPrincipal;
import rbac.model.login.Role;

import java.util.Set;


@RestController
@RequestMapping("/users")
public class AuthController {

  private static final Logger log = LoggerFactory.
      getLogger(AuthController.class);

  @RequestMapping(value = "/login", method = POST)
  public AuthenticationToken authenticate(@RequestBody final UsernamePasswordToken credentials) {
  final Subject subject = SecurityUtils.getSubject();

      //CustomAuthenticationToken token = new CustomAuthenticationToken(credentials.getUsername(), credentials.getPassword());

      subject.login(credentials);

      subject.getSession().setAttribute("username", credentials.getUsername());

      subject.getSession().setAttribute("role",  ((CustomPrincipal) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()).getRoles());

      String username = credentials.getUsername();
      Set<Role> roles = ((CustomPrincipal) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()).getRoles();
      String sessionId = (String) subject.getSession().getId();
      return new AuthenticationToken(username, roles, sessionId);
  }

  @RequiresRoles("users")
  @RequestMapping(value = "/list_user", method = GET)
  public String getAll() {
    System.out.println("Role [ "+((CustomPrincipal) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()).getRoles()+" ]");
    return null;
  }

    @RequestMapping(value = "/logout", method = GET)
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

}
