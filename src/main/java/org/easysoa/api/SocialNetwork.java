
package org.easysoa.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author dirix
 */
@Service
public interface SocialNetwork {
    
    @GET
    @Path("/statuses/update/status={status}")
    void postOnTwitter(@PathParam("status")String message, String userName, String password);
}
