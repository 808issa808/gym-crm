package org.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
abstract public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
}
