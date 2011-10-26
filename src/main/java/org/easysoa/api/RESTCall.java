
package org.easysoa.api;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.osoa.sca.annotations.Service;


/**
 *
 * @author Michel Dirix
 */

@Service
public interface RESTCall {
    
  @GET
  @Path("/tree")
  @Produces("text/plain")
  String getCompositeTree();
  
  @GET
  @Path("/componentContent")
  @Produces("text/plain")
  String getComponentContent(@FormParam("id")String id);
  
  @GET
  @Path("/componentMenu")
  @Produces("text/plain")
  String getComponentMenu(@FormParam("id")String id);
  
  @POST
  @Path("/addElement")
  @Produces("text/plain")
  void addElement(@FormParam("id")String id,@FormParam("action")String action);

}
