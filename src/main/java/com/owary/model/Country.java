package com.owary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@XmlRootElement(name = "Country")
@XmlAccessorType(XmlAccessType.FIELD)
public class Country {

    @XmlElement(name="Name")
    private String name;
    @XmlElement(name="Capital")
    private String capital;
    @XmlElement(name="Area")
    private Double area;
    @XmlElement(name="Population")
    private Double population;

}
