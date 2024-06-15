package com.BusReservation.capstone.Config;

import com.BusReservation.capstone.Entity.User;
import com.BusReservation.capstone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailService implements UserDetailsService {

    //injecting the repo layer
    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = repository.findByEmail(username);
        return userInfo.map(CustomUserDetail::new)
                .orElseThrow(()->new UsernameNotFoundException("user not available " + username));
    }

}
