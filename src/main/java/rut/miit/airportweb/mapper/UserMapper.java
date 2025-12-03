package rut.miit.airportweb.mapper;

import org.springframework.stereotype.Component;
import rut.miit.airportweb.dao.entity.UserEntity;
import rut.miit.airportweb.dto.UserDto;

@Component
public class UserMapper {

    public UserDto map(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .passwordHash(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .passengerId(String.valueOf(user.getPassenger().getId()))
                .build();
    }

    // В обратную сторону то же самое, билдеры везде есть, если что-то не будет получаться, писать мне

}
