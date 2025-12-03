package rut.miit.airportweb.service;

import rut.miit.airportweb.dto.TicketCreateDto;
import rut.miit.airportweb.dto.TicketDto;

import java.util.List;

/**
 * Сервис для управления билетами
 */
public interface TicketService {

    /**
     * Получить билет по номеру билета
     * @param ticketNumber номер билета
     * @return Optional с билетом
     */
    TicketDto getTicketByNumber(String ticketNumber);

    /**
     * Создать новый билет
     * @param ticketCreateDto DTO создания билета
     * @return созданный билет
     */
    TicketDto createTicket(TicketCreateDto ticketCreateDto);

//    /**
//     * Обновить билет
//     * @param id идентификатор билета
//     * @param ticketUpdateDto DTO обновления билета
//     * @return обновленный билет
//     */
//    TicketDto updateTicket(Integer id, TicketUpdateDto ticketUpdateDto);

    /**
     * Удалить билет
     * @param id идентификатор билета
     */
    void deleteTicket(Integer id);

    /**
     * Найти все билеты по номеру рейса
     * @param flightNumber номер рейса
     * @return список билетов
     */
    List<TicketDto> findAllByFlight(String flightNumber);

    /**
     * Найти все билеты по номеру паспорта
     * @param passportNumber номер паспорта
     * @return список билетов
     */
    List<TicketDto> findAllByPassportNumber(String passportNumber);

    /**
     * Обновить статус билета
     * @param ticketId ID билета
     * @param newStatus новый статус
     * @return обновленный билет
     */
    TicketDto updateTicketStatus(Integer ticketId, String newStatus);

    /**
     * Зарегистрировать пассажира на рейс (чек-ин)
     * @param ticketId ID билета
     * @return билет после регистрации
     */
    TicketDto checkInPassenger(Integer ticketId);

    /**
     * Посадить пассажира на рейс
     * @param ticketId ID билета
     * @return билет после посадки
     */
    TicketDto boardPassenger(Integer ticketId);

    /**
     * Получить занятые места на рейсе
     * @param flightId ID рейса
     * @return список занятых мест
     */
    List<String> getOccupiedSeats(Integer flightId);

    /**
     * Проверить доступность места
     * @param flightId ID рейса
     * @param seatNumber номер места
     * @return true если место доступно
     */
    boolean isSeatAvailable(Integer flightId, String seatNumber);

}
