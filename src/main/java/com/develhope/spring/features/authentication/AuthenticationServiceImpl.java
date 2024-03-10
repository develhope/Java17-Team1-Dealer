package com.develhope.spring.features.authentication;

import com.develhope.spring.features.authentication.dto.request.RefreshTokenRequest;
import com.develhope.spring.features.authentication.dto.request.SignUpRequest;
import com.develhope.spring.features.authentication.dto.request.SigninRequest;
import com.develhope.spring.features.authentication.dto.response.JwtAuthenticationResponse;
import com.develhope.spring.features.authentication.entities.RefreshToken;
import com.develhope.spring.features.authentication.repositories.RefreshTokenRepository;
import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (!StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("Name is empty");
        }

        if (!StringUtils.hasText(request.getEmail())) {
            throw new IllegalArgumentException("Email is empty");
        }

        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Password is empty");
        }

        if (request.getName().length() < 3) {
            throw new IllegalArgumentException("Name is too short: min 3 characters");
        }

        if (request.getName().length() > 20) {
            throw new IllegalArgumentException("Name is too long: max 20 characters");
        }

        if (request.getEmail().length() < 5) {
            throw new IllegalArgumentException("Email is too short: min 5 characters");
        }

        if (request.getEmail().length() > 50) {
            throw new IllegalArgumentException("Email is too long: max 50 characters");
        }

        if (request.getTelephoneNumber().length() < 5) {
            throw new IllegalArgumentException("Telephone number is too short: min 5 digits");
        }

        if (request.getTelephoneNumber().length() > 11) {
            throw new IllegalArgumentException("Telephone number is too long: max 11 digits");
        }

        if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("Password is too short: min 5 characters");
        }

        if (request.getPassword().length() > 20) {
            throw new IllegalArgumentException("Password is too long: max 20 characters");
        }
        final var roleString = request.getRole().toUpperCase();
        if (!Role.isValidUserRole(roleString)) {
            throw new IllegalArgumentException("Invalid role");
        }

        var userExists = userRepository.findByTelephoneNumber(request.getEmail());

        if (userExists.isPresent()) {
            throw new IllegalArgumentException("User with this telephone number already exists");
        }

        userExists = userRepository.findByEmail(request.getEmail());

        if (userExists.isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .telephoneNumber(request.getTelephoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(roleString)).build();

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);
        return JwtAuthenticationResponse.builder().authToken(jwt).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        //TODO Instead of only throw IllegalArgumentException maybe we could handle an error on the controller side
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        var jwt = jwtService.generateToken(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);
        return JwtAuthenticationResponse.builder().authToken(jwt).refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());

        if (refreshToken.isPresent() && !jwtService.isRefreshTokenExpired(refreshToken.get())) {
            //TODO Instead of only throw IllegalArgumentException maybe we could handle an error on the controller side
            var user = userRepository.findByEmail(refreshToken.get().getUserInfo().getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

            var jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder().authToken(jwt).refreshToken(refreshToken.get().getToken()).build();
        } else {
            //TODO Manage errors, for now we return an empty response
            return JwtAuthenticationResponse.builder().build();
        }
    }
}
