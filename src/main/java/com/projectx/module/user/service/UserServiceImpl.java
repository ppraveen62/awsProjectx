package com.projectx.module.user.service;

import com.projectx.module.common.utill.JwtUtill;
import com.projectx.module.user.dtos.UserDto;
import com.projectx.module.user.dtos.UserServiceMapDto;
import com.projectx.module.user.entity.User;
import com.projectx.module.user.entity.UserPincodeMap;
import com.projectx.module.user.entity.UserServiceMap;
import com.projectx.module.user.repository.UserPincodeMapRepository;
import com.projectx.module.user.repository.UserRepository;
import com.projectx.module.user.repository.UserServiceMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPincodeMapRepository userPincodeMapRepository;

    @Autowired
    UserServiceMapRepository userServiceMapRepository;

    @Autowired
    JwtUtill jwtUtill;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDTO) throws Exception {
        Optional<User> userOptional = userRepository.findByUserName(userDTO.getEmailId());
        if (userOptional.isPresent()) {
            throw new Exception("User already exists");
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);

        List<UserPincodeMap> pincodeMaps = new ArrayList<>();
        for (String pinCode : userDTO.getPincodeServiceMapList()) {
            UserPincodeMap pincodeMap = new UserPincodeMap();
            pincodeMap.setUserId(savedUser.getUserId());
            pincodeMap.setPinCode(pinCode);
            pincodeMaps.add(pincodeMap);
        }
        userPincodeMapRepository.saveAll(pincodeMaps);

        List<UserServiceMap> serviceMaps = new ArrayList<>();
        for(UserServiceMapDto serviceMapDto: userDTO.getUserServiceMapDtoList()){
           serviceMapDto.setUserId(savedUser.getUserId());
           UserServiceMap userServiceMap = convertToEntity(serviceMapDto);
           serviceMaps.add(userServiceMap);
        }
        userServiceMapRepository.saveAll(serviceMaps);

        userDTO.setUserId(savedUser.getUserId());
        return userDTO;

    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDTO, String token) throws Exception {
        Long userId = jwtUtill.getUserIdFromToken(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        updateEntityFromDTO(existingUser, userDTO);
        List<UserPincodeMap> existingPincodeMaps = userPincodeMapRepository.findByUserId(userId);
        updatePincodeMaps(existingPincodeMaps, userDTO.getPincodeServiceMapList(), userId);

        List<UserServiceMap> existingServiceMaps = userServiceMapRepository.findByUserId(userId);
        updateServiceMaps(existingServiceMaps, userDTO.getUserServiceMapDtoList(), userId);

        userRepository.save(existingUser);
        userDTO.setUserId(userId);
        return userDTO;
    }


    @Transactional
    private void updatePincodeMaps(List<UserPincodeMap> existingPincodeMaps, List<String> updatedPinCodes, Long userId) {

        List<UserPincodeMap> newPincodeMaps = new ArrayList<>();

        for (UserPincodeMap existingPincodeMap : existingPincodeMaps) {
            if (updatedPinCodes.contains(existingPincodeMap.getPinCode())) {
                updatedPinCodes.remove(existingPincodeMap.getPinCode());
            } else {
                userPincodeMapRepository.delete(existingPincodeMap);
            }
        }

        for (String newPinCode : updatedPinCodes) {
            UserPincodeMap pincodeMap = new UserPincodeMap();
            pincodeMap.setUserId(userId);
            pincodeMap.setPinCode(newPinCode);
            newPincodeMaps.add(pincodeMap);
        }
        userPincodeMapRepository.saveAll(newPincodeMaps);
    }

    @Transactional
    private void updateServiceMaps(List<UserServiceMap> existingServiceMaps, List<UserServiceMapDto> updatedServiceMaps, Long userId) {
        List<UserServiceMap> serviceMapsToDelete = new ArrayList<>();
        List<UserServiceMap> serviceMapsToAdd = new ArrayList<>();

        for (UserServiceMap existingServiceMap : existingServiceMaps) {
            UserServiceMapDto matchingServiceDto = findMatchingServiceDto(updatedServiceMaps, existingServiceMap.getServiceId());

            if (matchingServiceDto != null) {
                updateServiceMapFromDTO(existingServiceMap, matchingServiceDto);
            } else {
                serviceMapsToDelete.add(existingServiceMap);
            }
        }

        for (UserServiceMapDto updatedServiceDto : updatedServiceMaps) {
            UserServiceMap existingServiceMap = findExistingServiceMap(existingServiceMaps, updatedServiceDto.getServiceId());

            if (existingServiceMap == null) {
                UserServiceMap newServiceMap = convertToEntity(updatedServiceDto);
                newServiceMap.setUserId(userId);
                serviceMapsToAdd.add(newServiceMap);
            }
        }

        userServiceMapRepository.deleteAll(serviceMapsToDelete);
        userServiceMapRepository.saveAll(serviceMapsToAdd);
    }

    private UserServiceMapDto findMatchingServiceDto(List<UserServiceMapDto> serviceDtos, Long serviceId) {
        return serviceDtos.stream()
                .filter(serviceDto -> serviceDto.getServiceId().equals(serviceId))
                .findFirst()
                .orElse(null);
    }

    private UserServiceMap findExistingServiceMap(List<UserServiceMap> existingServiceMaps, Long serviceId) {
        return existingServiceMaps.stream()
                .filter(serviceMap -> serviceMap.getServiceId().equals(serviceId))
                .findFirst()
                .orElse(null);
    }


    private User convertToEntity(UserDto userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setAddress1(userDTO.getAddress1());
        user.setAddress2(userDTO.getAddress2());
        user.setCityName(userDTO.getCityName());
        user.setEmailId(userDTO.getEmailId());
        user.setState(userDTO.getState());
        user.setCountry(userDTO.getCountry());
        user.setPinCode(userDTO.getPinCode());
        user.setCountryCode(userDTO.getCountryCode());
        user.setMobile1(userDTO.getMobile1());
        user.setMobile2(userDTO.getMobile2());
        user.setIdentityCardUrl(userDTO.getIdentityCardUrl());
        user.setPhotoUrl(userDTO.getPhotoUrl());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserName(userDTO.getEmailId());
        return user;
    }

    private UserServiceMap convertToEntity(UserServiceMapDto dto) {
        UserServiceMap userServiceMap = new UserServiceMap();
        userServiceMap.setServiceId(dto.getServiceId());
        userServiceMap.setServiceName(dto.getServiceName());
        userServiceMap.setUserId(dto.getUserId());
        userServiceMap.setExperience(dto.getExperience());
        userServiceMap.setAvailableTimeStart(dto.getAvailableTimeStart());
        userServiceMap.setAvailableTimeEnd(dto.getAvailableTimeEnd());
        return userServiceMap;
    }

    private void updateEntityFromDTO(User user, UserDto userDTO) {
        user.setFullName(userDTO.getFullName());
        user.setAddress1(userDTO.getAddress1());
        user.setAddress2(userDTO.getAddress2());
        user.setCityName(userDTO.getCityName());
        user.setState(userDTO.getState());
        user.setCountry(userDTO.getCountry());
        user.setPinCode(userDTO.getPinCode());
        user.setCountryCode(userDTO.getCountryCode());
        user.setMobile1(userDTO.getMobile1());
        user.setMobile2(userDTO.getMobile2());
        user.setIdentityCardUrl(userDTO.getIdentityCardUrl());
        user.setPhotoUrl(userDTO.getPhotoUrl());
        user.setEmailId(userDTO.getEmailId());
        user.setUserName(userDTO.getEmailId());
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }

    private void updateServiceMapFromDTO(UserServiceMap serviceMap, UserServiceMapDto serviceDTO) {
        serviceMap.setServiceName(serviceDTO.getServiceName());
        serviceMap.setExperience(serviceDTO.getExperience());
        serviceMap.setAvailableTimeStart(serviceDTO.getAvailableTimeStart());
        serviceMap.setAvailableTimeEnd(serviceDTO.getAvailableTimeEnd());
    }




}
