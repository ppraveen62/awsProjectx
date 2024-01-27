package com.projectx.module.user.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserDto {
    private Long userId;
    private String fullName;
    private String address1;
    private String address2;
    private String cityName;
    private String state;
    private String country;
    private String pinCode;
    private String countryCode;
    private String mobile1;
    private String mobile2;
    private String identityCardUrl;
    private String photoUrl;
    private String emailId;
    private String password;
    private List<String> pincodeServiceMapList;
    private List<UserServiceMapDto> userServiceMapDtoList;
}
