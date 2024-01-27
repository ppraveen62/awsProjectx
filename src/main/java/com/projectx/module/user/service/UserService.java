package com.projectx.module.user.service;


import com.projectx.module.user.dtos.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto) throws Exception;

    UserDto updateUser(UserDto userDTO, String token) throws Exception;

}
