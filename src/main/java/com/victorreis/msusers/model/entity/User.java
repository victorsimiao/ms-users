package com.victorreis.msusers.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.victorreis.msusers.model.dto.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:SS")
    private LocalDateTime updatedAT;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:SS")
    private LocalDateTime createdAt;


    public static User valueOf(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .age(userRequest.getAge())
                .build();
    }

    public void updateValues(UserRequest userRequest) {
        this.name = userRequest.getName();
        this.age = userRequest.getAge();
        this.updatedAT = LocalDateTime.now();
    }

}
