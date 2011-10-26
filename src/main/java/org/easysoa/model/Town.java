package org.easysoa.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.persistence.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name="Town")
public class Town implements Serializable
{
    protected Long id;
    protected String country;
    protected String townName;
    protected double latitude;
    protected double longitude;

    public Town() {
    }

    public Town(String country, String townName, double latitude, double longitude) {
        this.country = country;
        this.townName = townName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId(){
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    
   
}
