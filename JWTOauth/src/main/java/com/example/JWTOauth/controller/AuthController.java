package com.example.JWTOauth.controller;


import com.example.JWTOauth.dto.AuthResponse;
import com.example.JWTOauth.dto.Login;
import com.example.JWTOauth.dto.SignUp;
import com.example.JWTOauth.dto.Token;
import com.example.JWTOauth.entity.Client;
import com.example.JWTOauth.entity.User;
import com.example.JWTOauth.repository.ClientRepository;
import com.example.JWTOauth.security.TokenGenerator;
import com.example.JWTOauth.service.ClientDetailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    private JwtAuthenticationProvider refreshTokenAuthProvider;

    @Autowired
    private ClientDetailValidator clientDetailValidator;


    @Autowired
    private ClientRepository clientRepository;


    @PostMapping(path = "/register",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String register(SignUp signupDTO) {
        User user = new User();
        user.setUsername(signupDTO.getUserName());
        user.setPassword(signupDTO.getPassword());
        user.setId(signupDTO.getId());
        Client client = clientRepository.findByRegistrationId(signupDTO.getClientId());
        if (client == null) {
            throw new RuntimeException();
        }
        user.setClient(client);

        userDetailsManager.createUser(user);

        return "Registered successfully!";
    }

    @PostMapping(path = "/token",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity token(Token tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping(path = "/login",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<AuthResponse> testMethod(Login login) throws Exception {

        if (clientDetailValidator.validateClientDetails(login)) {
            Authentication authentication;
            try {
                authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.getUserName(), login.getPassword()));

            } catch (Exception ex) {
                throw new Exception("invalid username/password");
            }
            Token jwtToken = tokenGenerator.createToken(authentication);

            return ResponseEntity.ok(new AuthResponse(jwtToken.getAccessToken(), jwtToken.getRefreshToken()));
        }else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getUserDetails(){
        return "Get all the user details";
    }

}
