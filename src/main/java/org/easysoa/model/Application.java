
package org.easysoa.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Application")
@XmlRootElement(name = "Application")
@Entity(name="Application")
public class Application
    implements Serializable
{

    @XmlAttribute(name = "id", required = true)
    protected Long id;
    @XmlAttribute(name = "compositeLocation", required = true)
    protected String compositeLocation;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "origin")
    @XmlSchemaType(name = "anyURI")
    protected Application origin;
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId(){
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(unique=true, nullable=false) 
    public String getCompositeLocation() {
        return compositeLocation;
    }

    public void setCompositeLocation(String value) {
        this.compositeLocation = value;
    }

    @OneToOne
    public Application getOrigin() {
        return origin;
    }

    public void setOrigin(Application application) {
        this.origin = application;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
