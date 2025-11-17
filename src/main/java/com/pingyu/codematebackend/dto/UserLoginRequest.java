package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable; // (提前加上，以防万一)

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L; // (提前加上)

    private String userAccount;
    private String userPassword;
}