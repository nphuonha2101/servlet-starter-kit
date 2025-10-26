package com.example.demo.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseModel {
    private String username;
    private String email;
    private String password;
    private String status;

    public User(String username, String email, String password, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    @Getter
    public static enum Status {
        ACTIVE("ACTIVE"),
        INACTIVE("INACTIVE");

        private final String value;
        Status(String value) {
            this.value = value;
        }

    }
}