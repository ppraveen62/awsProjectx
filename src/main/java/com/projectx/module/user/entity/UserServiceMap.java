package com.projectx.module.user.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long serviceId;
    private String serviceName;
    private Long experience;
    private String availableTimeStart;
    private String availableTimeEnd;
}
