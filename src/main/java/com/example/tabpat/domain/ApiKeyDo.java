package com.example.tabpat.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class ApiKeyDo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String apiKey;

}
