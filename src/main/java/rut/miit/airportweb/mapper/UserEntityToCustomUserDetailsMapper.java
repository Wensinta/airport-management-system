package rut.miit.airportweb.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import rut.miit.airportweb.config.security.CustomUserDetails;
import rut.miit.airportweb.dao.entity.UserEntity;

@UtilityClass
@Slf4j
public class UserEntityToCustomUserDetailsMapper {

    public static CustomUserDetails map(UserEntity user) {
        log.info("Mapping user {} with role {} to details", user.getUsername(), user.getRole().name());
        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
