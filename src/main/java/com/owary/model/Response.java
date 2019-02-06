package com.owary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Response {

    private String message;
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
