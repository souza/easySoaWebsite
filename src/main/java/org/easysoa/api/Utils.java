
package org.easysoa.api;

import java.util.List;
import org.easysoa.model.User;
import org.eclipse.emf.ecore.EObject;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author dirix
 */
@Service
public interface Utils {
    
    String getTownSuggestions(String townName);
    List<String> getCountries();
    void sendMailAccountCreation(User user);
    void sendMailForFriendRequest(User originUser, User targetUser);
    boolean saveComposite(EObject compositeObject , String path);
}
