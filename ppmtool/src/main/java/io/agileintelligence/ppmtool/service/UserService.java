package io.agileintelligence.ppmtool.service;

import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.UsernameExistsException;
import io.agileintelligence.ppmtool.exceptions.UsernameExistsResponse;
import io.agileintelligence.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser) {
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new UsernameExistsException("Username '" + newUser.getUsername() + " already exists");
        }

    }

}
