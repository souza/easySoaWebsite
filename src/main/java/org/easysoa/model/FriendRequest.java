
package org.easysoa.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dirix
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FriendRequest")
@XmlRootElement(name = "FriendRequest")
@Entity(name="FriendRequest")
public class FriendRequest implements Serializable {
    
    @XmlAttribute(name = "id", required = true)
    protected Long id;
    @XmlAttribute(name = "originUser", required = true)
    protected User originUser;
    @XmlAttribute(name = "targetUser", required = true)
    protected User targetUser;

    public FriendRequest() {
    }

    
    public FriendRequest(User originUser, User targerUser) {
        this.originUser = originUser;
        this.targetUser = targerUser;
    }

    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    @OneToOne
    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    @OneToOne
    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targerUser) {
        this.targetUser = targerUser;
    }
    
    
    
}
