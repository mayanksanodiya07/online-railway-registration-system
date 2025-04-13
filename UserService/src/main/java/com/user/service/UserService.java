package com.user.service;

import java.util.List;

import com.user.entity.User;

public interface UserService {
    User getUserById(Long id);
//    User getCurrentUser( );
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    List<Object> getBookingsByUserId(Long id, String token);
}
