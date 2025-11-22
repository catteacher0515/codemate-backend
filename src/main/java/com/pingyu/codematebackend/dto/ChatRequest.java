package com.pingyu.codematebackend.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChatRequest implements Serializable {
    private Long teamId;
    private String content;
}