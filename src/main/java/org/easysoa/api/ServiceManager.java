package org.easysoa.api;

import java.util.List;
import java.util.Map;

import org.easysoa.model.Application;
import org.easysoa.model.User;
import org.eclipse.stp.sca.Composite;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author dirix
 */
@Service
public interface ServiceManager {

    void createService(User user,String name, String descrption, String packageName, String templateName, Map<String, Object> params);

    Composite searchService(String name, User user);

    void saveFile(String fileName,String fileContent);

    User deleteService(User user,String serviceName);

    Object launchService(User user,Application application);
    
    List<Application> searchService(String keywords);
    
    void reloadComposite();
    
    List<Application> getServices(String friendId);
    
    Application setDescription(Application application, String description);
    
    void createFile(User user,Application application,String dirName,String fileName);
    
    void removeFile(String path);
    
    Composite getComposite();
    
    void setComposite(Composite composite);
    
    Application getCurrentApplication();

    /**
     * search in the application directories if the file is in
     * @param file the file name as describe in the composite file
     * @return the url if exists
     */
	String isFileInApplication(String file);

	/**
	 * get all the target available for a reference
	 * @return a list of the available targets
	 */
	List<String> getAllTarget();

	/**
	 * Create class at the good location according as the type is a script or other source file
	 * @param type must be Script, Implementation
	 * @param fileName 
	 */
	void createFile(String type, String fileName);

	void changePackage(String implemType, String classNameOrigin);

}
