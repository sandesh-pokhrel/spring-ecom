package com.kavka.apiservices.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "This is used to hold email and password information during login.")
public class LoginRequest implements Serializable {

    @NotBlank(message = "Email cannot be blank")
    @ApiModelProperty(name = "email", notes = "Email address during login", example = "mail@example.com",
            required = true, allowableValues = "false", dataType = "String")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @ApiModelProperty(name = "password", notes = "Password during login", example = "dummy_password", position = 1,
            required = true, allowableValues = "false", dataType = "String")
    private String password;
}
