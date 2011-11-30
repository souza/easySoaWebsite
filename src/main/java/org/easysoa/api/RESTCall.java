
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
  
  @POST
  @Path("/saveElement")
  @Produces("text/plain")
  void saveElement(String params);
  
  @GET
  @Path("/templateForm")
  @Produces("text/plain")
  String getTemplateForm(@FormParam("templateName")String templateName);
  
  @POST
  @Path("/createApplication")
  @Produces("text/plain")
  void createApplication(String params);
  
  @GET
  @Path("/implementationContent")
  @Produces("text/plain")
  String getImplementationContent(@FormParam("modelId")String modelId,@FormParam("id")String id);
  
  @GET
  @Path("/interfaceContent")
  @Produces("text/plain")
  String getInterfaceContent(@FormParam("modelId")String modelId, @FormParam("id")String id);
  
  @GET
  @Path("/bindingContent")
  @Produces("text/plain")
  String getBindingContent(@FormParam("modelId")String modelId, @FormParam("id")String id);
  
  @GET
  @Path("/implementationFileContent")
  @Produces("text/plain")
  String getImplementationFileContent();

  @GET
  @Path("/editorMode")
  @Produces("text/plain")
  String getEditorMode();
  
  @POST
  @Path("/saveImplementation")
  @Produces("text/plain")
  void saveImplementation(@FormParam("content")String content);
  
  @GET
  @Path("/existingImplementations")
  @Produces("text/plain")
  String getExistingImplementations();

  @POST
  @Path("/uploadImplementation")
  @Produces("text/plain")
  void uploadImplementation(@FormParam("file")String file, @FormParam("implementationType")String implementationType, @FormParam("id")String id);
  
  @POST
  @Path("/createNewImplementation")
  @Produces("text/plain")
  void createNewImplementation(@FormParam("id")String id, @FormParam("className")String className, @FormParam("implemType")String implemType, @FormParam("createFile")boolean createFile);

}
