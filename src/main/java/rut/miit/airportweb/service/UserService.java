package rut.miit.airportweb.service;

import rut.miit.airportweb.dto.UserDto;
import rut.miit.airportweb.dto.UserRegistrationDto;
import rut.miit.airportweb.exception.EntityNotFoundException;

import java.util.List;

/**
 * Сервис для управления пользователями
 */
public interface UserService {

    /**
     * Регистрация нового пользователя
     * @param registrationDto DTO регистрации
     * @return зарегистрированный пользователь
     */
    UserDto registerUser(UserRegistrationDto registrationDto);


    /**
     * Получить пользователя по имени пользователя
     * @param username имя пользователя
     * @return Optional с пользователем
     */
    UserDto getUserByUsername(String username) throws EntityNotFoundException;

    /**
     * Получить пользователя по имени и фамилии
     * @param firstName имя
     * @param lastName фамилия
     * @return Optional с пользователем
     */
    UserDto getUserByFullName(String firstName, String lastName) throws EntityNotFoundException;

    /**
     * Получить всех пользователей с оптимизированным запросом
     * @return список пользователей
     */
    List<UserDto> getAllUsersOptimized();

    /**
     * Аутентификация пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return аутентифицированный пользователь
     */
    UserDto authenticate(String username, String password) throws EntityNotFoundException;

    /**
     * Обновить информацию о пользователе
     * @param id идентификатор пользователя
     * @param userDto новые данные
     * @return обновленный пользователь
     */
    UserDto updateUser(String username, UserDto userDto);

    /**
     * Удалить пользователя
     * @param id идентификатор пользователя
     */
    void deleteUser(String username);

    /**
     * Проверить существование пользователя
     * @param username имя пользователя
     * @return true если пользователь существует
     */
    boolean userExists(String username);

    /**
     * Получить всех пассажиров (пользователей с ролью PASSENGER)
     * @return список пассажиров
     */
    List<UserDto> getAllPassengers();
}
