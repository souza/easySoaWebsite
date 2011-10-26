package org.easysoa.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easysoa.api.Utils;
import org.easysoa.model.User;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.DocumentRoot;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.util.ScaResourceFactoryImpl;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.jpa.Provider;

/**
 * 
 * @author dirix
 */
public class UtilsImpl implements Utils {

	@Reference
	public Provider<EntityManager> db;

	@Override
	public String getTownSuggestions(String townName) {
		System.out.println("getTownSuggestions");
		EntityManager em = db.get();
		em.getTransaction().begin();
		Query query = em
				.createQuery("SELECT DISTINCT t.townName FROM Town t WHERE t.townName LIKE :name");
		query.setParameter("name", townName + "%");
		java.util.List<String> search = query.getResultList();
		System.out.println(search.size());
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < search.size(); i++) {
			sb.append("\"" + search.get(i) + "\"");
			if (i != search.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public List<String> getCountries() {
		EntityManager em = db.get();
		Query query = em.createQuery("SELECT DISTINCT t.country FROM Town t");
		java.util.List<String> search = query.getResultList();
		if (search.isEmpty()) {
			return null;
		}
		return search;
	}

	@Override
	public void sendMailAccountCreation(User user) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.inria.fr");
		Session session = Session.getDefaultInstance(props, null);
		try {
			Message mesg = new MimeMessage(session);
			mesg.setFrom(new InternetAddress("michel.dirix@inria.fr"));
			InternetAddress toAddress = new InternetAddress(user.getMail());
			mesg.addRecipient(Message.RecipientType.TO, toAddress);
			mesg.setSubject("Your account creation");
			StringBuilder body = new StringBuilder();
			body.append("Thanks for your subscription on easySOA WebSite\n");
			body.append("Your login is : ");
			body.append(user.getLogin());
			body.append("\n");
			body.append("Your password is : ");
			body.append(user.getPassword());

			mesg.setText(body.toString());
			Transport.send(mesg);
		}
		catch (MessagingException ex) {
			while ((ex = (MessagingException) ex.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void sendMailForFriendRequest(User originUser, User targetUser) {
		try {
			InputStream is = ServiceManagerImpl.class
					.getResourceAsStream("/templates/friendRequestMail.template");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s;
			StringBuilder StringBuilder = new StringBuilder();

			while ((s = br.readLine()) != null) {
				if (s.contains(":targetFirstname")) {
					s = s.replace(":targetFirstname", targetUser.getUserName());
				}
				if (s.contains(":originSurname")) {
					s = s.replace(":originSurname", originUser.getSurname());
				}
				if (s.contains(":originFirstname")) {
					s = s.replace(":originFirstname", originUser.getUserName());
				}
				StringBuilder.append(s + System.getProperty("line.separator"));
			}
			br.close();
			is.close();

			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.inria.fr");
			Session session = Session.getDefaultInstance(props, null);
			Message mesg = new MimeMessage(session);
			mesg.setFrom(new InternetAddress("michel.dirix@inria.fr"));
			InternetAddress toAddress = new InternetAddress(targetUser.getMail());
			mesg.addRecipient(Message.RecipientType.TO, toAddress);
			mesg.setSubject("Friend request");

			mesg.setText(StringBuilder.toString());
			Transport.send(mesg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean saveComposite(EObject compositeObject, String path) {

		try {
			// create resource set and resource
			ResourceSet resourceSet = new ResourceSetImpl();

			// Register XML resource factory
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
					.put("*", new ScaResourceFactoryImpl());

			Resource resource = resourceSet.createResource(URI.createFileURI(path));

			// add the root object to the resource

			// create DR
			DocumentRoot dr = ScaFactory.eINSTANCE.createDocumentRoot();
			// add frascati namespace to composite
			dr.getXMLNSPrefixMap().put("frascati", "http://frascati.ow2.org/xmlns/sca/1.1");
			// add composite
			dr.setComposite((Composite) compositeObject);

			resource.getContents().add(dr);

			HashMap<Object, Object> options = new HashMap<Object, Object>();
			options.put(XMLResource.OPTION_ENCODING, new String("UTF-8"));

			resource.save(options);
			return true;
		}
		catch (IOException e) {

			return false;

		}

	}

}
