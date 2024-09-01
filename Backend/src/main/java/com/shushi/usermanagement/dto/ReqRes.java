package com.shushi.usermanagement.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shushi.usermanagement.entity.OurUsers;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ReqRes {

    private int StatusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String city;
    private String role;
    private String password;
    private OurUsers ourUser;
    private List<OurUsers> ourUsersList=new ArrayList<>();

}
