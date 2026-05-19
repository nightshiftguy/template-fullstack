package com.nightguy.springjwttemplate.security;

import com.nightguy.springjwttemplate.auth.AuthenticationRequest;
import com.nightguy.springjwttemplate.auth.AuthenticationResponse;
import com.nightguy.springjwttemplate.user.Role;
import com.nightguy.springjwttemplate.user.User;
import com.nightguy.springjwttemplate.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(AuthenticationRequest request) {
    Optional<User> optionalUser = repository.findByUsername(request.getLogin());
    if (optionalUser.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this login already exists");
    } else {
      User user =
          new User(
              Role.USER, false, passwordEncoder.encode(request.getPassword()), request.getLogin());
      repository.save(user);
      return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
    }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
    var user =
        repository
            .findByUsername(request.getLogin())
            .orElseThrow(() -> new IllegalStateException("User missing after authentication"));

    return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
  }
}
