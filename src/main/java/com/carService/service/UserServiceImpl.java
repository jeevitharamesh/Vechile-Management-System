package com.carService.service;

import com.carService.model.CarService;
import com.carService.model.Role;
import com.carService.model.User;
import com.carService.repository.AppointmentRepository;
import com.carService.repository.CarServiceRepository;
import com.carService.repository.RoleRepository;
import com.carService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private CarServiceRepository carServiceRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, CarServiceRepository carServiceRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.carServiceRepository = carServiceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username not found!");
        }
        Collection<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).authorities(authorities).build();
    }

    @Transactional
    public User registerUser(User user, boolean employee) {
        if(userRepository.findByUsername(user.getUsername()) != null || userRepository.findByEmail(user.getEmail()) != null) {
            return null;
        } else if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null) {
            return null;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if(userRepository.findAll().size() == 0) user.getRoles().add(roleRepository.findByName("ADMIN").orElse(null));
            else{
                if(employee) user.getRoles().add(roleRepository.findByName("EMPLOYEE").orElse(null));
                else user.getRoles().add(roleRepository.findByName("USER").orElse(null));
            }

            return userRepository.save(user);
        }
        
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> findAllUnemployedWorkers(){
        return userRepository.findAllUnemployedWorkers();
    }

    @Transactional
    public User editUser(String firstName, String lastName, String username, String email, Integer id) {

        User user = userRepository.findById(id).orElse(null);

        if(user != null){
            if (!firstName.equals("") && !user.getFirstName().equals(firstName)) {
                user.setFirstName(firstName);
            }

            if (!lastName.equals("") && !user.getLastName().equals(lastName)) {
                user.setLastName(lastName);
            }

            if (!username.equals("") && !user.getUsername().equals(username)) {
                user.setUsername(username);
            }

            if (!email.equals("") && !user.getEmail().equals(email)) {
                user.setEmail(email);
            }

            return userRepository.save(user);
        }

        return null;
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    @Transactional
    public void delete(User user) {

        if(user.getRoles().get(0).getName().equals("EMPLOYEE")){
            Optional<CarService> carServiceOptional = carServiceRepository.findByEmployeeListContains(user);
            if (carServiceOptional.isPresent()){
                CarService carService = carServiceOptional.get();
                carService.getEmployeeList().remove(user);
                carServiceRepository.save(carService);
            }
        }

        if(appointmentRepository.findByUser(user).size() > 0){
            appointmentRepository.findByUser(user).forEach( a -> appointmentRepository.delete(a));
        }
        userRepository.delete(user);
    }
}
