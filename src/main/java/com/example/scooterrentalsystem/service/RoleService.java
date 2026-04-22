package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RoleDao;
import com.example.scooterrentalsystem.dao.UserDao;
import com.example.scooterrentalsystem.entity.Role;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleDao roleDao;
    private final UserDao userDao;

    public RoleService(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Роль с ID " + id + " не найдена"));
    }

    public Role createRole(String name) {
        if (roleDao.findByName(name).isPresent()) {
            throw new ConflictException("Роль с именем " + name + " уже существует");
        }
        Role role = new Role(name);
        roleDao.save(role);
        log.info("Создана роль id={} name={}", role.getId(), name);
        return role;
    }

    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        if (userDao.countByRoleId(id) > 0) {
            throw new ConflictException("Нельзя удалить роль: она назначена пользователям");
        }
        roleDao.delete(role);
        log.info("Удалена роль id={}", id);
    }
}
