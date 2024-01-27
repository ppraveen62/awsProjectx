package com.projectx.module.user.controller;

import com.projectx.module.common.entity.AuthRequest;
import com.projectx.module.common.utill.JwtUtill;
import com.projectx.module.user.dtos.UserDto;
import com.projectx.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtill jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/intro")
    public  String home(){
        return "hello world user controllerasda";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("Inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws Exception {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");
        UserDto updatedUser = userService.updateUser(userDto,token);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
