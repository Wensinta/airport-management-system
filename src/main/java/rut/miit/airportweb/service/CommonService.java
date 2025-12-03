package rut.miit.airportweb.service;

import rut.miit.airportweb.dto.FlightDto;
import rut.miit.airportweb.dto.TicketDto;

import java.util.List;

/**
 * Интерфейс, в котором представлены общие методы из Use Case диаграммы.
 * Например, это метод getFlightsList - просмотр списка рейсов
 * @author Yaroslav
 * */
public interface CommonService {
    List<FlightDto> getFlightsList();
}
