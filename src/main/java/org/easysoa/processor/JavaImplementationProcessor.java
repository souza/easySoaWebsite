package org.easysoa.processor;

import java.util.Map;

import org.easysoa.api.ServiceManager;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class JavaImplementationProcessor implements ComplexProcessorItf {

	@Reference
	protected ImplementationsProcessorItf implementationsProcessor; 
	@Reference 
	protected ServiceManager serviceManager;
	
	@Override
    public String getId() {
        return ScaPackage.eINSTANCE.getJavaImplementation().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getJavaImplementation().getName();
    }
	
	@Override
    public String getLabel(EObject eObject) {
        return "Java";
    }
	
    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        JavaImplementation javaImplementation = (JavaImplementation)eObject;
        JSONObject implemObject = new JSONObject();
        implemObject.put("id", "+implementation");
        implemObject.put("text", javaImplementation.getClass_());
        implemObject.put("im0", "JavaImplementation.gif");
        implemObject.put("im1", "JavaImplementation.gif");
        implemObject.put("im2", "JavaImplementation.gif");
        return implemObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	JavaImplementation javaImplementation = null;
    	if(eObject != null)	javaImplementation = (JavaImplementation)eObject;
    	else javaImplementation = (JavaImplementation)this.getNewEObject(null);
    	StringBuffer sb = new StringBuffer();
    	sb.append("<table id=\"implementation-panel\">");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"java-implementation-image\"></div>");
    	sb.append("Implementation : ");
    	sb.append("</td>");
    	sb.append("<td name=\"implementation-type\" id=\"implementation-type\" size=\"1\">");
    	sb.append("Java");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("Class : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(javaImplementation.getClass_()!=null)sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\""+javaImplementation.getClass_()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\"\"/>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td colspan=\"2\">");
    	sb.append("<div id=\"editor\"");
    	sb.append("</div>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("</table>");
    	
    	if(javaImplementation.getClass_()!=null){
    		String url = this.serviceManager.isFileInApplication(javaImplementation.getClass_());
    		this.implementationsProcessor.setUrl(url);
    		if(url!=null){
    			this.implementationsProcessor.setEditorMode("java");
    		}
    	}
    	else{
    		this.implementationsProcessor.setUrl(null);
    	}
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteImplementation')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		JavaImplementation implem = (JavaImplementation)eObject;
		implem.setClass((String)params.get("implementation"));
		return implem;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		System.out.println("getNewEObject Java");
		return ScaFactory.eINSTANCE.createJavaImplementation();
	}
	
}
