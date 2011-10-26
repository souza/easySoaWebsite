package org.easysoa.api;

import java.util.List;

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

    void createService(User user,String name, String descrption, String packageName, String templateName);

    Composite searchService(String name, User user);

    void saveFile(String fileName,String fileContent);

    User deleteService(User user,String serviceName);

    Object launchService(User user,Application application);
    
    List<Application> searchService(String keywords);
    
    List<Application> getServices(String friendId);
    
    Application setDescription(Application application, String description);
    
    void createFile(User user,Application application,String dirName,String fileName);
    
    void removeFile(String path);
    
    Composite getComposite();
    
    Application getCurrentApplication();
    
}
