package rut.miit.airportweb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardingPassDto {

    private Integer id;

    @NotNull(message = "Ticket cannot be null")
    private TicketDto ticket;

    @NotNull(message = "Ticket cannot be null")
    private LocalDateTime checkInTime;

    private Boolean passportVerified;
    private Boolean luggageVerified;
    private Boolean boarded;

    @NotNull(message = "Border guard cannot be null")
    private UserDto verifiedByBorderGuard;

    @NotNull(message = "Customs cannot be null")
    private UserDto verifiedByCustoms;

}
