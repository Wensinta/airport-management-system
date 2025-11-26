package rut.miit.airportweb.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



/**
 * {@link rut.miit.airportweb.dto.CustomUserDetails} это реализация {@link UserDetails}
 * для того, чтобы в этом сервисе достать из базы пользователя, конвертнуть его в UserDetails и сделать аутентификацию
 * с авторизацией
 * Т.е. в этом коде должна быть логика получения пользователя по какому-либо параметру из базы и перевод в нужный класс
 * */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
