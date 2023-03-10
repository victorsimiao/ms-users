package com.victorreis.msusers.model.dto;

import com.victorreis.msusers.model.entity.User;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String uuid;

    private String name;

    private Integer age;


    public static UserResponse toResponse(User user){
        return UserResponse.builder()
                .uuid(user.getUuid())
                .age(user.getAge())
                .name(user.getName())
                .build();
    }

}
