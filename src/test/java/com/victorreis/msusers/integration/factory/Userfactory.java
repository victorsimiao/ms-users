package com.victorreis.msusers.integration.factory;

import com.victorreis.msusers.model.entity.User;

public final class Userfactory {
    private Userfactory() {
    }

    public static User createUser(String name, int age) {
        return User.builder()
                .name(name)
                .age(age)
                .build();
    }
}
