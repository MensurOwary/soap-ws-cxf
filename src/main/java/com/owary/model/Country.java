package com.owary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@XmlRootElement
public class Country {

    private String name;
    private String capital;
    private Double area;
    private Double population;

}
