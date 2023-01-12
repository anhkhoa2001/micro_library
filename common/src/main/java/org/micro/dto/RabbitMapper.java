package org.micro.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RabbitMapper {

    private String method;
    private String rabbit_type;
    private Boolean access;
}
