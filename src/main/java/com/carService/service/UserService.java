package com.carService.service;

import com.carService.model.CarService;
import com.carService.model.Role;
import com.carService.model.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDetails loadUserByUsername(String username);

    User registerUser(User user, boolean employee);

    User findUserByUsername(String username);

    Optional<User> findUserById(int id);

    List<User> getAllUsers();

    List<User> findAllUnemployedWorkers();

    User editUser(String firstName, String lastName, String username, String email, Integer id);

    Optional<User> getUserById(int id);

    void delete(User user);
}
