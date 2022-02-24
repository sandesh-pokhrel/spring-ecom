package com.kavka.apiservices.common;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class Status implements Serializable {
    private String exMessage;
    protected String message;
    private String operation;
}
