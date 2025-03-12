package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.UserLoginDTO;
import org.wildcodeschool.myblog.dto.UserRegistrationDTO;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.repository.UserRepository;
import org.wildcodeschool.myblog.security.AuthenticationService;
import org.wildcodeschool.myblog.service.UserService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, AuthenticationService authenticationService, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        User registeredUser = userService.registerUser(
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getPassword(),
                Set.of("ROLE_USER") // Par défaut, chaque utilisateur aura le rôle "USER"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authenticationService.authenticate(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );

//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PreAuthorize("#email == authentication.principal.email")
    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        // Déboguer le principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication Principal: " + authentication.getPrincipal());
        User principal = (User) authentication.getPrincipal();

        // Log de l'objet complet de l'utilisateur
        log.info("Authenticated user: {}", principal);  // Cela appellera toString() pour afficher des infos

        Optional<User> registeredUser = userRepository.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(registeredUser.orElse(null));
    }
}
