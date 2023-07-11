package com.example.polls.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserIdentityAvailability {
    private Boolean available;

    public UserIdentityAvailability(Boolean available) {
        this.available = available;
    }

}
