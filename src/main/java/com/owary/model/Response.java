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
@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

    @XmlElement(name = "Message")
    private String message;
    @XmlElement(name = "Code")
    private int code;


    public static Response success(){
        return Response.builder().code(200).message("Success").build();
    }

    public static Response created(){
        return Response.builder().code(201).message("Created").build();
    }

    public static Response failure(){
        return Response.builder().code(500).message("Failed").build();
    }
}
