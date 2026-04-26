package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalDao;
import com.example.scooterrentalsystem.dao.RoleDao;
import com.example.scooterrentalsystem.dao.UserDao;
import com.example.scooterrentalsystem.dto.UserUpdateDto;
import com.example.scooterrentalsystem.entity.Rental;
import com.example.scooterrentalsystem.entity.Role;
import com.example.scooterrentalsystem.entity.User;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    @Mock
    private RentalDao rentalDao;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when user email not found")
    void findByEmail_NotFound() {
        String email = "notfound@example.com";
        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    @DisplayName("Should register new user successfully")
    void registerUser_Success() {
        String email = "new@example.com";
        String password = "pass";
        Role role = new Role("ROLE_USER");
        when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        when(roleDao.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(password)).thenReturn("encodedPass");

        User registeredUser = userService.registerUser(email, password, "First", "Last", "ROLE_USER", BigDecimal.TEN);

        assertNotNull(registeredUser);
        verify(userDao).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when registering existing email")
    void registerUser_AlreadyExists() {
        String email = "existing@example.com";
        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(ConflictException.class, () -> 
            userService.registerUser(email, "pass", "F", "L", "USER", BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should top up user balance")
    void topUpBalance_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(BigDecimal.ZERO);
        when(userDao.findByIdWithRole(userId)).thenReturn(Optional.of(user));

        userService.topUpBalance(userId, BigDecimal.valueOf(100));

        assertEquals(0, BigDecimal.valueOf(100).compareTo(user.getBalance()));
    }

    @Test
    @DisplayName("Should throw exception for negative top up amount")
    void topUpBalance_NegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            userService.topUpBalance(1L, BigDecimal.valueOf(-10)));
    }

    @Test
    @DisplayName("Should delete user without active rentals or history")
    void deleteUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(rentalDao.findActiveRentalByUserId(userId)).thenReturn(Optional.empty());
        when(userDao.countRentalsForUser(userId)).thenReturn(0L);

        userService.deleteUser(userId);

        verify(userDao).delete(user);
    }

    @Test
    @DisplayName("Should throw exception when deleting user with active rental")
    void deleteUser_ActiveRentalConflict() {
        Long userId = 1L;
        User user = new User();
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(rentalDao.findActiveRentalByUserId(userId)).thenReturn(Optional.of(new Rental()));

        assertThrows(ConflictException.class, () -> userService.deleteUser(userId));
    }

    @Test
    @DisplayName("Should update user info")
    void updateUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setFirstName("Old");
        UserUpdateDto dto = new UserUpdateDto("New", "Last");
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(userDao.findByIdWithRole(userId)).thenReturn(Optional.of(user));

        User result = userService.updateUser(userId, dto);

        assertEquals("New", result.getFirstName());
        assertEquals("Last", result.getLastName());
    }
}