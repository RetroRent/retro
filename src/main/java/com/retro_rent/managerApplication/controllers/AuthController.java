package com.retro_rent.managerApplication.controllers;

import com.retro_rent.managerApplication.Dao.RenterDao;
import com.retro_rent.managerApplication.Dao.RoleDao;
import com.retro_rent.managerApplication.Dao.TenantDao;
import com.retro_rent.managerApplication.exception.BadRequestException;
import com.retro_rent.managerApplication.modle.AuthProvider;
import com.retro_rent.managerApplication.modle.Renter;
import com.retro_rent.managerApplication.modle.Tenant;
import com.retro_rent.managerApplication.modle.User;
import com.retro_rent.managerApplication.payload.*;
import com.retro_rent.managerApplication.Dao.UserDao;
import com.retro_rent.managerApplication.payload.*;
import com.retro_rent.managerApplication.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("UserRepository")
    @Autowired
    private UserDao userRepository;


        @Qualifier("RenterRepository")
        @Autowired
        private RenterDao renterRepository;


    @Qualifier("TenantRepository")
    @Autowired
    private TenantDao tenantRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleDao roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getFirst_name());
        user.setLastName(signUpRequest.getLast_name());
        user.setUserName(signUpRequest.getUser_name());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);
        user.setRole(roleRepository.findByRole("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        result.getEmail(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/registrationEnd")
    public ResponseEntity<?> registerUserStep2(@Valid @RequestBody RegistrationEndRequest registrationEndRequest) {
        Optional<User> user = userRepository.findByEmail(registrationEndRequest.getEmail());

        if(!user.isPresent()) {
            throw new BadRequestException("user did not finish first part of registraion.");
        }

        if (registrationEndRequest.getPaymentLink().isEmpty() && registrationEndRequest.isUser_type_renter()) {
            return ResponseEntity.ok(new ApiResponse(false, "registration error, user with renter type must have PayPal Me link not empty"));
        }

        user.get().setCity(registrationEndRequest.getCity());
        user.get().setStreet(registrationEndRequest.getStreet());
        user.get().setAddress_number(registrationEndRequest.getHome_number());
        user.get().setPhoneNumber(registrationEndRequest.getPhone_number());
        user.get().setPostal_code(registrationEndRequest.getPostal_code());
        user.get().setCity(registrationEndRequest.getCity());

        if (!registrationEndRequest.getPaymentLink().contains("https://") && !registrationEndRequest.getPaymentLink().contains("http://"))
        {
            registrationEndRequest.setPaymentLink(("https://".concat(registrationEndRequest.getPaymentLink())));
        }

        user.get().setPaymentLink(registrationEndRequest.getPaymentLink());

        if (registrationEndRequest.isUser_type_renter()) {
            Renter renter = new Renter();
            renter.setUser(user.get());
            renterRepository.save(renter);
            if (registrationEndRequest.isUser_type_tenant()) {
                user.get().setRole(roleRepository.findByRole("BOTH"));
                Tenant tenant = new Tenant();
                tenant.setUser(user.get());
                tenantRepository.save(tenant);
            } else {
                user.get().setRole(roleRepository.findByRole("RENTER"));
            }
        } else if (registrationEndRequest.isUser_type_tenant()){
            user.get().setRole(roleRepository.findByRole("TENANT"));
            Tenant tenant = new Tenant();
            tenant.setUser(user.get());
            tenantRepository.save(tenant);
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "you must select at least one user type"));
        }


        userRepository.save(user.get());

        return ResponseEntity.ok(new ApiResponse(true, "registration complete"));
    }
}