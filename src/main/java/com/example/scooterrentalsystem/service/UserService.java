package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalDao;
import com.example.scooterrentalsystem.dao.RoleDao;
import com.example.scooterrentalsystem.dao.UserDao;
import com.example.scooterrentalsystem.dto.UserUpdateDto;
import com.example.scooterrentalsystem.entity.Role;
import com.example.scooterrentalsystem.entity.User;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final RentalDao rentalDao;

    public UserService(UserDao userDao, RoleDao roleDao, RentalDao rentalDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.rentalDao = rentalDao;
    }

    public User registerUser(String email, String password, String firstName, String lastName, String roleName, java.math.BigDecimal balance) {
        if (userDao.findByEmail(email).isPresent()) {
            throw new ConflictException("Пользователь с email " + email + " уже существует");
        }
        Role role = roleDao.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Роль " + roleName + " не найдена в системе"));
        User newUser = new User(email, password, firstName, lastName, role, balance);
        userDao.save(newUser);
        log.info("Зарегистрирован пользователь id={} email={} balance={}", newUser.getId(), email, newUser.getBalance());
        return userDao.findByIdWithRole(newUser.getId()).orElse(newUser);
    }

    public void topUpBalance(Long userId, java.math.BigDecimal amount) {
        if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        }
        User user = getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        log.info("Баланс пользователя id={} пополнен на {}", userId, amount);
    }

    public User updateUser(Long userId, UserUpdateDto dto) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        if (dto.firstName() != null) {
            user.setFirstName(dto.firstName());
        }
        if (dto.lastName() != null) {
            user.setLastName(dto.lastName());
        }
        log.info("Обновлены данные пользователя id={}", userId);
        return userDao.findByIdWithRole(userId).orElse(user);
    }

    /** legacy name used earlier in коде — делегирует на {@link #updateUser(Long, UserUpdateDto)} */
    public void updateUserInfo(Long userId, String firstName, String lastName) {
        updateUser(userId, new UserUpdateDto(firstName, lastName));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.findByIdWithRole(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAllWithRoles();
    }

    public void deleteUser(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        if (rentalDao.findActiveRentalByUserId(userId).isPresent()) {
            throw new ConflictException("Нельзя удалить пользователя с активной арендой");
        }
        if (userDao.countRentalsForUser(userId) > 0) {
            throw new ConflictException("Нельзя удалить пользователя: есть история аренд");
        }
        userDao.delete(user);
        log.info("Удалён пользователь id={}", userId);
    }
}
