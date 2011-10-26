/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easysoa.api;

import org.easysoa.model.User;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author dirix
 */
@Service
public interface Users {

    User connect(String username,String password);
    User createAccount(String login, String password, String confirmPassword,
			String mail, String name, String surname, String civility, String town, String country, String birthday);
    User modifyInformations(User user);
    User searchUser(Long id);
    User searchUser(String idString);
}
