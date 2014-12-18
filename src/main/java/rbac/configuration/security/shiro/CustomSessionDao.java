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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomSessionDao extends AbstractSessionDAO {

  private static final Logger log = LoggerFactory
      .getLogger(CustomSessionDao.class);

  private Map<Serializable, Session> map;

  @Override
  protected Serializable doCreate(Session session) {
    final Serializable sessionId = generateSessionId(session);
    log.debug("Creating a new session identified by[{}]", sessionId);
    assignSessionId(session, sessionId);
    log.debug("Session Content:", session.toString());
    map.put(session.getId(), session);

    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    log.debug("Reading a session identified by[{}]", sessionId);
    return map.get(sessionId);
  }

  @Override
  public void update(Session session) throws UnknownSessionException {
    log.debug("Updating a session identified by[{}]", session.getId());
    map.replace(session.getId(), session);
  }

  @Override
  public void delete(Session session) {
    log.debug("Deleting a session identified by[{}]", session.getId());
    map.remove(session.getId());
  }

  @Override
  public Collection<Session> getActiveSessions() {
    return map.values();
  }

  /**
   * Retrieves a collection of sessions related to a user.
   * <p>
   * @param email the authentication identifier to look sessions for.
   * @return a collection of sessions.
   */
//  public Collection<Session> getSessionsForAuthenticationEntity(
//      final String email) {
//    log.debug("Looking up for sessions related to [{}]", email);
//    final SessionAttributePredicate<String> predicate
//        = new SessionAttributePredicate<String>("email", email);
//    return map.values(predicate);
//  }

  /**
   * Destroys currently allocated instance.
   */
//  public void destroy() {
//    log.info("Shutting down Hazelcast instance [{}]..", hcInstanceName);
//    final HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName(
//        hcInstanceName);
//    if (instance != null) {
//      instance.shutdown();
//    }
//  }

}
