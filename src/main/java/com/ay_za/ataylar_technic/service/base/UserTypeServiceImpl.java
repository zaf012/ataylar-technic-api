package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.UserTypeDto;
import com.ay_za.ataylar_technic.entity.UserType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserTypeServiceImpl {

    List<UserTypeDto> getAllUserTypes();

    @Transactional
    UserTypeDto createUserType(String userTypeName);

    @Transactional
    List<UserTypeDto> createDefaultUserTypes();

    Optional<UserTypeDto> getUserTypeById(Integer id);

    boolean existsByUserTypeName(String userTypeName);

    Optional<UserType> getRandomUserType();

    @Transactional
    UserTypeDto updateUserType(Integer id, String userTypeName);

    @Transactional
    void deleteUserType(Integer id);

    boolean checkUserTypeById(Integer id);
}
