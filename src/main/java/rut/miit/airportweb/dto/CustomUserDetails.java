package rut.miit.airportweb.dto;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Этот класс используется как раз в {@link rut.miit.airportweb.config.security.CustomUserDetailsService}
 * для того, чтобы в {@link rut.miit.airportweb.config.security.CustomUserDetailsService} перевести в объект
 * из базы данных в него и вернуть там же.
 * Здесь должны быть все те же поля, что и у пользователя в базе данных.
 * Ознакомиться с пользователями можно в {@link rut.miit.airportweb.dao.entity.UserEntity} и однопакетных классах
 * */
public class CustomUserDetails implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
