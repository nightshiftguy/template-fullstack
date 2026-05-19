package com.nightguy.springjwttemplate.user;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @NullMarked
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

    return userRepository
        .findByUsername(login)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
  }
}
