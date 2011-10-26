package org.easysoa.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.namespace.QName;

import org.easysoa.api.ServiceManager;
import org.easysoa.api.Users;
import org.easysoa.api.Utils;
import org.easysoa.compositeTemplates.CompositeTemplateItf;
import org.easysoa.model.Application;
import org.easysoa.model.User;
import org.eclipse.stp.sca.Composite;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;
import org.ow2.frascati.jpa.Provider;
import org.ow2.frascati.parser.api.Parser;
import org.ow2.frascati.parser.core.ParsingContextImpl;

/**
 *
 * @author Michel Dirix
 */
@Scope("COMPOSITE")
public class ServiceManagerImpl implements ServiceManager {

    @Reference
    public Provider<EntityManager> db;
    @Reference
    public Users connection;
    private Namespace nameSpace;
    @Reference
    public Parser compositeParser;
    @Reference 
    public CompositeTemplateItf templates;
    @Reference
    public Utils utils;
    
    private Composite composite;
    private Application application;

    @Override
    public void createService(User user, String name, String description, String packageName, String templateName) {
        Application application = new Application();
        application.setDescription(description);
        application.setName(name);

        try {
            String path = user.getWorkspaceUrl();
            System.out.println("path : " + path);
            File serviceDirectory = new File(path + File.separator + name);
            serviceDirectory.mkdir();
            path = serviceDirectory.getPath();
            System.out.println("path : " + path);
            File pomFile = new File(path + File.separator + "pom.xml");
            pomFile.createNewFile();
            this.initializePom(pomFile, name);
            File resourcesDirectory = new File(path + File.separator + "src" + File.separator + "main" + File.separator + "resources");
            resourcesDirectory.mkdirs();
            String compositeFile = resourcesDirectory.getPath() + File.separator + name + File.separator + ".composite";
            this.initializeComposite(application, compositeFile, templateName);
            application.setCompositeLocation(compositeFile);
            
            packageName = packageName.replaceAll("\\.", File.separator + File.separator);
            File packageDirectory = new File(path + File.separator + "src" + File.separator + "main" + File.separator + "java"+File.separator+packageName);
            packageDirectory.mkdirs();
            File apiDirectory = new File(packageDirectory.getPath()+File.separator+"api");
            apiDirectory.mkdirs();
            File implDirectory = new File(packageDirectory.getPath()+File.separator+"impl");
            implDirectory.mkdirs();

            user.getProvidedApplications().add(application);

            EntityManager em = db.get();
            em.getTransaction().begin();
            em.persist(application);

            User userx = em.find(User.class, user.getId());
            userx.getProvidedApplications().add(application);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Composite searchService(String name, User user) {
        try {
            for (Application application : user.getProvidedApplications()) {
                if (application.getName().equals(name)) {
                    String location = application.getCompositeLocation();
                    location = location.replaceAll(File.separator + File.separator, "/");
                    Composite composite = (Composite) compositeParser.parse(new QName("file://" + location), new ParsingContextImpl());
                    this.composite = composite;
                    this.application = application;
                    return composite;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initializePom(File pomFile, String serviceName) {
        try {
            Element root = new Element("project");
            Namespace ns1 = Namespace.getNamespace("ns", "http://maven.apache.org/POM/4.0.0");
            root.addNamespaceDeclaration(ns1);
            Namespace ns2 = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.addNamespaceDeclaration(ns2);
            Namespace ns3 = Namespace.getNamespace("schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");
            root.addNamespaceDeclaration(ns3);

            Element modelVersion = new Element("modelVersion");
            modelVersion.setText("4.0.0");
            root.addContent(modelVersion);
            Element groupId = new Element("groupId");
            groupId.setText("org.ow2.frascati.examples");
            root.addContent(groupId);
            Element artifactId = new Element("artifactId");
            artifactId.setText(serviceName);
            root.addContent(artifactId);
            Element name = new Element("name");
            name.setText(serviceName);
            root.addContent(name);

            //------------PARENT-------------------//
            Element parent = new Element("parent");
            Element parentGroupId = new Element("groupId");
            parentGroupId.setText("org.ow2.frascati.examples");
            parent.addContent(parentGroupId);
            Element parentArtifactId = new Element("artifactId");
            parentArtifactId.setText("parent");
            parent.addContent(parentArtifactId);
            Element parentVersion = new Element("version");
            parentVersion.setText("1.5-SNAPSHOT");
            parent.addContent(parentVersion);

            root.addContent(parent);

            //-------------PROPERTIES-----------------//
            Element properties = new Element("properties");
            Element compositeFile = new Element("composite.file");
            compositeFile.setText(serviceName);
            properties.addContent(compositeFile);

            root.addContent(properties);

            //--------------BUILD---------------------//
            Element build = new Element("build");
            Element plugins = new Element("plugins");
            build.addContent(plugins);
            Element plugin = new Element("plugin");
            plugins.addContent(plugin);
            Element cxfGroupId = new Element("groupId");
            cxfGroupId.setText("org.apache.cxf");
            plugin.addContent(cxfGroupId);
            Element cxfArtifactId = new Element("artifactId");
            cxfArtifactId.setText("cxf-codegen-plugin");
            plugin.addContent(cxfArtifactId);

            root.addContent(build);

            Document document = new Document(root);
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            out.output(document, new FileOutputStream(pomFile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeComposite(Application application, String compositeFile, String templateName) {
        try {

            Composite composite = this.templates.createComposite(templateName);
            utils.saveComposite(composite, compositeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeClass(File classFile, String packageName) {
        try {
            String classFileName = classFile.getName();
            String[] divClassFileName = classFileName.split("\\.");
            classFileName = divClassFileName[0];
            InputStream is = ServiceManagerImpl.class.getResourceAsStream("/class.template");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s;
            FileWriter fw = new FileWriter(classFile.getPath());
            BufferedWriter output = new BufferedWriter(fw);
            while ((s = br.readLine()) != null) {
                if (s.contains(":packageName")) {
                    s = s.replace(":packageName", packageName);
                }
                if (s.contains(":className")) {
                    s = s.replace(":className", classFileName);
                }
                output.write(s + System.getProperty("line.separator"));
            }
            output.flush();
            output.close();
            fw.close();
            br.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveFile(String fileName, String fileContent) {
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter output = new BufferedWriter(fw);

            output.write(fileContent);
            output.flush();
            output.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User deleteService(User user, String applicationName) {
        EntityManager em = db.get();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT DISTINCT a FROM Application a WHERE a.name = :name");
        query.setParameter("name", applicationName);
        Application application = (Application) query.getSingleResult();
        user.getProvidedApplications().remove(application);
        User userx = em.find(User.class, user.getId());
        userx.getProvidedApplications().remove(application);
        em.remove(application);

        em.getTransaction().commit();

        File file = new File(user.getWorkspaceUrl() + File.separator + applicationName);
        this.deleteDirectory(file);

        return userx;
    }

    private void deleteDirectory(File file) {
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                deleteDirectory(f);
                f.delete();
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    public Object launchService(User user, Application application) {
        Object result = null;

        return result;
    }

    @Override
    public List<Application> searchService(String keywords) {
        List<Application> applications = new ArrayList<Application>();
        String[] keywordsTab = keywords.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keywordsTab.length; i++) {
            String keyword = keywordsTab[i];
            stringBuilder.append(" a.description LIKE '%" + keyword + "%'");
            if (i != keywordsTab.length - 1) {
                stringBuilder.append("OR");
            }
        }

        EntityManager em = db.get();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT DISTINCT a FROM Application a WHERE" + stringBuilder.toString());
        applications = query.getResultList();
        return applications;
    }

    @Override
    public List<Application> getServices(String friendId) {
        User friend = connection.searchUser(Long.parseLong(friendId));
        return friend.getProvidedApplications();
    }

    @Override
    public Application setDescription(Application application, String description) {
        EntityManager em = db.get();
        em.getTransaction().begin();
        Application appli = em.find(Application.class, application.getId());
        appli.setDescription(description);
        em.getTransaction().commit();
        return appli;
    }

    @Override
    public void createFile(User user, Application application, String dirName, String fileName) {
        try {
            String dirNameModified = dirName.replaceAll("\\.", File.separator + File.separator);
            String dirPath = user.getWorkspaceUrl() + File.separator + application.getName() + File.separator + dirNameModified;
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            if (fileName != null && !fileName.equals("")) {
                File fileCreated = new File(dirPath + File.separator + fileName);
                fileCreated.createNewFile();
                if (fileName.endsWith("java")) {
                    String[] paths = dirName.split("\\.");
                    StringBuilder packageName = new StringBuilder();
                    for (int i = 0; i < paths.length; i++) {
                        if (i != 0 && i != 1 && i != 2) {
                            packageName.append(paths[i]);
                            if (i != paths.length - 1) {
                                packageName.append(".");
                            }
                        }
                    }
                    this.initializeClass(fileCreated, packageName.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                removeFile(f.getPath());
            }
        } else {
            file.delete();
        }
    }

  
    @Override
    public Composite getComposite() {
        return composite;
    }

    @Override
    public Application getCurrentApplication() {
    	return this.application;
    }
}
