package com.nphuonha.servletkit.models;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends BaseModel {
    @Expose
    private String username;
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
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