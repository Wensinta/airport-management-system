package rut.miit.airportweb.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rut.miit.airportweb.dao.entity.BoardingPassEntity;

import java.util.Optional;

@Repository
public interface BoardingPassRepository extends JpaRepository<BoardingPassEntity, Integer> {

    @Query("SELECT bp FROM boarding_pass_entity bp WHERE bp.ticket.ticketNumber = :ticketNumber")
    Optional<BoardingPassEntity> findByTicketNumber (String ticketNumber);
}
