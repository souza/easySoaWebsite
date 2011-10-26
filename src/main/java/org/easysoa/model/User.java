package org.easysoa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.persistence.*;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User")
@XmlRootElement(name = "User")
@Entity(name="AppUser")
public class User implements Serializable
{
    @XmlAttribute(name = "id", required = true)
    protected Long userId;
    @XmlAttribute(name = "login", required = true)
    protected String login;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "surname")
    protected String surname;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "mail", required = true)
    protected String mail;
    @XmlAttribute(name = "civility")
    protected Civility civility;
    @XmlAttribute(name = "town")
    protected String town;
    @XmlAttribute(name = "country")
    protected String country;
    @XmlAttribute(name = "friends")
    protected List<User> friends;
    @XmlAttribute(name = "birthday")
    protected String birthday;
    @XmlAttribute(name = "workspaceUrl")
    protected String workspaceUrl;
    @XmlAttribute(name = "providedApplication")
    protected List<Application> providedApplications;
    /*@XmlAttribute(name = "consumedApplication")
    protected List<Application> consumedApplications;*/
    @XmlAttribute(name= "friendRequests")
    protected List<FriendRequest> friendRequests;
    public User() {
    }

    public User(String login, String username, String surname, String password, String mail, Civility civility, String town, String country,String birthday) {
        this.login = login;
        this.username = username;
        this.surname = surname;
        this.password = password;
        this.mail = mail;
        this.civility = civility;
        this.town = town;
        this.friends = new ArrayList<User>();
        this.country = country;
        this.birthday = birthday;
        this.friendRequests = new ArrayList<FriendRequest>();
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId(){
        return this.userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }

      
    public void setUserName(String value) {
        this.username = value;
    }

    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String value) {
        this.surname = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getMail() {
        return mail;
    }

   
    public void setMail(String value) {
        this.mail = value;
    }

   
    public Civility getCivility() {
        return civility;
    }

    
    public void setCivility(Civility value) {
        this.civility = value;
    }

       
    public String getTown() {
        return town;
    }

    
    public void setTown(String value) {
        this.town = value;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
 
    @OneToMany
    public List<User> getFriends() {
        if (friends == null) {
            friends = new ArrayList<User>();
        }
        return this.friends;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWorkspaceUrl() {
        return workspaceUrl;
    }

    public void setWorkspaceUrl(String workspaceUrl) {
        this.workspaceUrl = workspaceUrl;
    }

    

    @OneToMany
    public List<Application> getProvidedApplications() {
        if (providedApplications == null) {
            providedApplications = new ArrayList<Application>();
        }
        return this.providedApplications;
    }

    public void setProvidedApplications(List<Application> providedApplications) {
        this.providedApplications = providedApplications;
    }

    

    /*
    @OneToMany
    public List<Application> getConsumedApplications() {
        if (consumedApplications == null) {
            consumedApplications = new ArrayList<Application>();
        }
        return this.consumedApplications;
    }*/

    @ManyToMany
    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

   
}
