
package org.easysoa.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Civility")
@XmlEnum
public enum Civility {

    @XmlEnumValue("Mr")
    MR("Mr"),
    @XmlEnumValue("Mrs")
    MRS("Mrs"),
    @XmlEnumValue("Miss")
    MISS("Miss");
    private final String value;

    Civility(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Civility fromValue(String v) {
        if(v.equals("Mr"))return Civility.MR;
        if(v.equals("Mrs"))return Civility.MRS;
        if(v.equals("Miss"))return Civility.MISS;
        throw new IllegalArgumentException(v);
    }

}
