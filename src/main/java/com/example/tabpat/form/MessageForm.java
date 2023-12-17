package com.example.tabpat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class MessageForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String role;
    private String message;
}
