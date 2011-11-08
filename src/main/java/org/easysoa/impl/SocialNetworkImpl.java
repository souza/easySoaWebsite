
package org.easysoa.impl;

import org.easysoa.api.SocialNetwork;
import net.unto.twitter.Api;


/**
 *
 * @author dirix
 */
public class SocialNetworkImpl implements SocialNetwork{

    @Override
    public void postOnTwitter(String message, String userName, String password) {
        Api api = Api.builder().username(userName).password(password).build();
        api.updateStatus(message).build().post();
    }
    
}
