package com.BusReservation.capstone.Service;

import com.BusReservation.capstone.Dto.UserDto;
import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Exception.UserAlreadyExistsException;
import com.BusReservation.capstone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;


    @Override
    public User saveUser(UserDto userDto) {
        // Check if a user with the same email already exists
        Optional<User> existingUser = userRepo.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("EmailId  already registered");
        }
        String password=passwordEncoder.encode(userDto.getPassword());
        User user = new User();
                user.setName(userDto.getName());
                user.setEmail(userDto.getEmail());
                user.setPassword(password);
                user.setRoles("USER");


        return userRepo.save(user);

    }

    public User currentUser(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findCurrentUser(email);

    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
