package org.micro.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RabbitMapper {

    private String method;
    private String rabbit_type;
    //access = true la url private
    private Boolean access;

    private Set<String> role;
}
