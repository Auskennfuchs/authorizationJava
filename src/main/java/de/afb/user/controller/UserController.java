package de.afb.user.controller;

import de.afb.persistence.entities.User;
import de.afb.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String  email) {
        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved "+n.getId();
    }

    @RequestMapping(path = "/all")
    @ResponseBody
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
