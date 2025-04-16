package com.user.service;

import java.util.List;

import com.user.dto.BookingDto;
import com.user.entity.User;

public interface UserService {
//    User getUserById(Long id);
    User getCurrentUser();
    User createUser(User user);
    User updateUser(User updatedUser);
    void deleteUser();
    List<BookingDto> getBookingsByUserId();
}
