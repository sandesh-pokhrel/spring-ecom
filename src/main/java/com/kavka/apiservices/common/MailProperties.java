package com.kavka.apiservices.common;

import lombok.Data;

@Data
public class MailProperties {

    private String host;
    private String port;
    private String username;
    private String password;
}
