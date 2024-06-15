package com.BusReservation.capstone.Service;

import com.BusReservation.capstone.Dto.UserDto;
import com.BusReservation.capstone.Entity.User;

import java.util.Optional;

public interface UserService {

    public User saveUser(UserDto userDto);


}
