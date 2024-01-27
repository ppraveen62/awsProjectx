package com.projectx.module.user.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserServiceMapDto {
    private Long userId;
    private Long serviceId;
    private String serviceName;
    private Long experience;
    private String availableTimeStart;
    private String availableTimeEnd;
}
