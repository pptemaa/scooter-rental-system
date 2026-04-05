package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RoleDao;
import com.example.scooterrentalsystem.dao.UserDao;
import com.example.scooterrentalsystem.entity.Role;
import com.example.scooterrentalsystem.entity.User;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    public UserService(UserDao userDao,RoleDao roleDao){
        this.userDao=userDao;
        this.roleDao=roleDao;
    }
    public User registerUser(String email,String password,String firstName,String lastName,String roleName){
        if (userDao.findByEmail(email).isPresent()){
            throw new ConflictException("Пользователь с email " + email + " уже существует!");
        }
        Role role = roleDao.findByName(roleName).orElseThrow(()->new ResourceNotFoundException("Роль " + roleName +" не найдена в системе"));
        User newUser = new User(email,password,firstName,lastName,role);
        userDao.save(newUser);
        return newUser;
    }
    public void UpdateUserInfo(Long userId,String firstName,String lastName){
        User user = userDao.findById(userId).orElseThrow(()->new ConflictException("Пользователя с id " + userId +" не существует "));
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }

}
