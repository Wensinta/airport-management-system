package rut.miit.airportweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PassengerDto {

    private Integer id;

    @NotNull(message = "User cannot be null")
    private UserDto user;

    @NotBlank(message = "Passport number cannot be blank")
    @NotNull(message = "Passport number cannot be null")
    private String passportNumber;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 20, message = "Phone number cannot be longer than 20 symbols")
    private String phone;

    @Email(message = "Email must have email pattern")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "")
    private String email;

    @NotNull(message = "Luggage checked cannot be null field")
    private Boolean luggageChecked;

    @NotNull(message = "Tickets cannot be null")
    private List<TicketDto> tickets;

}
