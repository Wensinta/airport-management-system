package rut.miit.airportweb.mapper;

import lombok.experimental.UtilityClass;
import rut.miit.airportweb.dao.entity.UserEntity;
import rut.miit.airportweb.dto.UserDto;
import rut.miit.airportweb.dto.UserRegistrationDto;

@UtilityClass
public class UserMapper {

    public static UserDto map(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .passwordHash(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .passengerId(String.valueOf(user.getPassenger().getId()))
                .build();
    }

    public static UserEntity map(UserRegistrationDto dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(UserEntity.Role.valueOf(dto.getRole()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

    // В обратную сторону то же самое, билдеры везде есть, если что-то не будет получаться, писать мне

}
