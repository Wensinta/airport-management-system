package rut.miit.airportweb.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rut.miit.airportweb.dao.entity.TicketEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    Optional<TicketEntity> findByTicketNumber(String ticket_number);

    @Query("SELECT DISTINCT t FROM ticket_entity t JOIN FETCH t.flight f WHERE f.flightNumber = :flightNumber")
    List<TicketEntity> findAllByFlight(String flightNumber);

    @Query("SELECT DISTINCT t FROM ticket_entity t JOIN FETCH t.passenger p WHERE p.passportNumber = :passportNumber")
    List<TicketEntity> findAllByPassportNumber(String passportNumber);

}
