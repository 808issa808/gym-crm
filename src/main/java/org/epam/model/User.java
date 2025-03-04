package org.epam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
abstract public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
}
