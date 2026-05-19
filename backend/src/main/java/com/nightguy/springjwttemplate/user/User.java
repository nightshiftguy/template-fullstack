package com.nightguy.springjwttemplate.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
  @Id @GeneratedValue private Long id;
  @NotBlank private String username;
  @NotNull private String password;
  @NotNull private Boolean enabled;

  @NotBlank
  @Enumerated(EnumType.STRING)
  private Role role;

  public User(Role role, Boolean enabled, String password, String username) {
    this.role = role;
    this.enabled = enabled;
    this.password = password;
    this.username = username;
  }

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  @NonNull
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
