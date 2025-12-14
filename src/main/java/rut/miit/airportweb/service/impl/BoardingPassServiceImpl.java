package rut.miit.airportweb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.airportweb.dao.entity.BoardingPassEntity;
import rut.miit.airportweb.dao.entity.TicketEntity;
import rut.miit.airportweb.dao.entity.UserEntity;
import rut.miit.airportweb.dao.repository.BoardingPassRepository;
import rut.miit.airportweb.dao.repository.TicketRepository;
import rut.miit.airportweb.dao.repository.UserRepository;
import rut.miit.airportweb.dto.BoardingPassCreateDto;
import rut.miit.airportweb.dto.BoardingPassDto;
import rut.miit.airportweb.exception.EntityAlreadyExistsException;
import rut.miit.airportweb.exception.EntityNotFoundException;
import rut.miit.airportweb.exception.NotPermittedOperation;
import rut.miit.airportweb.mapper.BoardingPassMapper;
import rut.miit.airportweb.service.BoardingPassService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardingPassServiceImpl implements BoardingPassService {

    private final BoardingPassRepository boardingPassRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardingPassDto getBoardingPassById(Integer id) {
        log.debug("Getting boarding pass by ID: {}", id);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", id)));

        return BoardingPassMapper.map(boardingPass);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardingPassDto getBoardingPassByTicketNumber(String ticketNumber) {
        log.debug("Getting boarding pass by ticket number: {}", ticketNumber);

        BoardingPassEntity boardingPass = boardingPassRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass for ticket %s not found", ticketNumber)));

        return BoardingPassMapper.map(boardingPass);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BoardingPassDto createBoardingPass(BoardingPassCreateDto boardingPassCreateDto) {
        log.info("Creating boarding pass for ticket: {}", boardingPassCreateDto.getTicketNumber());

        // Проверяем существование билета
        TicketEntity ticket = ticketRepository.findByTicketNumber(boardingPassCreateDto.getTicketNumber())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Ticket with number %s not found",
                                boardingPassCreateDto.getTicketNumber())));

        // Проверяем, не существует ли уже посадочный талон для этого билета
        Optional<BoardingPassEntity> existingBoardingPass =
                boardingPassRepository.findByTicketNumber(boardingPassCreateDto.getTicketNumber());

        if (existingBoardingPass.isPresent()) {
            throw new EntityAlreadyExistsException(
                    String.format("Boarding pass already exists for ticket %s",
                            boardingPassCreateDto.getTicketNumber()));
        }

        // Проверяем статус билета - должен быть CHECKED_IN
        if (ticket.getStatus() != TicketEntity.TicketStatus.CHECKED_IN) {
            throw new NotPermittedOperation(
                    String.format("Cannot create boarding pass for ticket with status %s. " +
                            "Ticket must be CHECKED_IN", ticket.getStatus()));
        }

        // Проверяем существование пограничника
        UserEntity borderGuard = userRepository.findByUsername(
                        boardingPassCreateDto.getBorderGuardUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Border guard with username %s not found",
                                boardingPassCreateDto.getBorderGuardUsername())));

        // Проверяем роль пограничника
        if (borderGuard.getRole() != UserEntity.Role.BORDER_GUARD) {
            throw new NotPermittedOperation(
                    String.format("User %s is not a border guard. Role: %s",
                            borderGuard.getUsername(), borderGuard.getRole()));
        }

        // Проверяем существование таможенника
        UserEntity customsOfficer = userRepository.findByUsername(
                        boardingPassCreateDto.getCustomsOfficerUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Customs officer with username %s not found",
                                boardingPassCreateDto.getCustomsOfficerUsername())));

        // Проверяем роль таможенника
        if (customsOfficer.getRole() != UserEntity.Role.CUSTOMS_OFFICER) {
            throw new NotPermittedOperation(
                    String.format("User %s is not a customs officer. Role: %s",
                            customsOfficer.getUsername(), customsOfficer.getRole()));
        }

        // Проверяем статус рейса - должен быть BOARDING
        if (ticket.getFlight().getStatus() != rut.miit.airportweb.dao.entity.FlightEntity.FlightStatus.BOARDING) {
            throw new NotPermittedOperation(
                    String.format("Cannot create boarding pass for flight with status %s. " +
                            "Flight must be BOARDING", ticket.getFlight().getStatus()));
        }

        // Создаем посадочный талон
        BoardingPassEntity boardingPass = BoardingPassEntity.builder()
                .ticket(ticket)
                .checkInTime(LocalDateTime.now())
                .passportVerified(false)
                .luggageVerified(false)
                .boarded(false)
                .verifiedByBorderGuard(borderGuard)
                .verifiedByCustoms(customsOfficer)
                .build();

        // Сохраняем посадочный талон
        BoardingPassEntity savedBoardingPass = boardingPassRepository.save(boardingPass);

        log.info("Created boarding pass with ID: {} for ticket: {}",
                savedBoardingPass.getId(), boardingPassCreateDto.getTicketNumber());

        return BoardingPassMapper.map(savedBoardingPass);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteBoardingPass(Integer id) {
        log.info("Deleting boarding pass with ID: {}", id);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", id)));

        // Проверяем, не посажен ли уже пассажир
        if (Boolean.TRUE.equals(boardingPass.getBoarded())) {
            throw new NotPermittedOperation(
                    "Cannot delete boarding pass for already boarded passenger");
        }

        boardingPassRepository.delete(boardingPass);
        log.info("Deleted boarding pass with ID: {}", id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BoardingPassDto updatePassportVerification(Integer boardingPassId,
                                                      boolean passportVerified,
                                                      Integer verifiedByUserId) {
        log.info("Updating passport verification for boarding pass ID: {} to {}",
                boardingPassId, passportVerified);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(boardingPassId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", boardingPassId)));

        // Проверяем существование пользователя-пограничника
        UserEntity borderGuard = userRepository.findById(verifiedByUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with ID %d not found", verifiedByUserId)));

        // Проверяем роль пользователя - должен быть BORDER_GUARD
        if (borderGuard.getRole() != UserEntity.Role.BORDER_GUARD) {
            throw new NotPermittedOperation(
                    String.format("User %s (ID: %d) is not authorized to verify passports. Role: %s",
                            borderGuard.getUsername(), verifiedByUserId, borderGuard.getRole()));
        }

        // Обновляем статус проверки паспорта
        boardingPass.setPassportVerified(passportVerified);
        boardingPass.setVerifiedByBorderGuard(borderGuard);

        BoardingPassEntity updatedBoardingPass = boardingPassRepository.save(boardingPass);

        log.info("Updated passport verification for boarding pass ID: {} to {} by user ID: {}",
                boardingPassId, passportVerified, verifiedByUserId);

        return BoardingPassMapper.map(updatedBoardingPass);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BoardingPassDto updateLuggageVerification(Integer boardingPassId,
                                                     boolean luggageVerified,
                                                     Integer verifiedByUserId) {
        log.info("Updating luggage verification for boarding pass ID: {} to {}",
                boardingPassId, luggageVerified);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(boardingPassId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", boardingPassId)));

        // Проверяем существование пользователя-таможенника
        UserEntity customsOfficer = userRepository.findById(verifiedByUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with ID %d not found", verifiedByUserId)));

        // Проверяем роль пользователя - должен быть CUSTOMS_OFFICER
        if (customsOfficer.getRole() != UserEntity.Role.CUSTOMS_OFFICER) {
            throw new NotPermittedOperation(
                    String.format("User %s (ID: %d) is not authorized to verify luggage. Role: %s",
                            customsOfficer.getUsername(), verifiedByUserId, customsOfficer.getRole()));
        }

        // Обновляем статус проверки багажа
        boardingPass.setLuggageVerified(luggageVerified);
        boardingPass.setVerifiedByCustoms(customsOfficer);

        // Обновляем также статус багажа у пассажира
        if (luggageVerified && boardingPass.getTicket() != null &&
                boardingPass.getTicket().getPassenger() != null) {
            boardingPass.getTicket().getPassenger().setLuggageChecked(true);
        }

        BoardingPassEntity updatedBoardingPass = boardingPassRepository.save(boardingPass);

        log.info("Updated luggage verification for boarding pass ID: {} to {} by user ID: {}",
                boardingPassId, luggageVerified, verifiedByUserId);

        return BoardingPassMapper.map(updatedBoardingPass);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BoardingPassDto updateBoardingStatus(Integer boardingPassId, boolean boarded) {
        log.info("Updating boarding status for boarding pass ID: {} to {}",
                boardingPassId, boarded);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(boardingPassId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", boardingPassId)));

        // Проверяем, можно ли посадить пассажира
        if (boarded) {
            checkBoardingRequirements(boardingPass);
        }

        // Обновляем статус посадки
        boardingPass.setBoarded(boarded);

        // Если пассажир посажен, обновляем статус билета
        if (boarded && boardingPass.getTicket() != null) {
            boardingPass.getTicket().setStatus(TicketEntity.TicketStatus.BOARDED);
        }

        BoardingPassEntity updatedBoardingPass = boardingPassRepository.save(boardingPass);

        log.info("Updated boarding status for boarding pass ID: {} to {}",
                boardingPassId, boarded);

        return BoardingPassMapper.map(updatedBoardingPass);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardingReadinessCheck checkBoardingReadiness(Integer boardingPassId) {
        log.debug("Checking boarding readiness for boarding pass ID: {}", boardingPassId);

        BoardingPassEntity boardingPass = boardingPassRepository.findById(boardingPassId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Boarding pass with ID %d not found", boardingPassId)));

        boolean passportVerified = Boolean.TRUE.equals(boardingPass.getPassportVerified());
        boolean luggageVerified = Boolean.TRUE.equals(boardingPass.getLuggageVerified());

        // Проверяем все требования для посадки
        StringBuilder message = new StringBuilder();
        boolean isReady = true;

        // 1. Проверка паспорта
        if (!passportVerified) {
            message.append("Паспорт не проверен. ");
            isReady = false;
        }

        // 2. Проверка багажа
        if (!luggageVerified) {
            message.append("Багаж не проверен. ");
            isReady = false;
        }

        // 3. Проверка статуса рейса
        if (boardingPass.getTicket() != null &&
                boardingPass.getTicket().getFlight() != null &&
                boardingPass.getTicket().getFlight().getStatus() !=
                        rut.miit.airportweb.dao.entity.FlightEntity.FlightStatus.BOARDING) {
            message.append("Рейс не в статусе посадки. ");
            isReady = false;
        }

        // 4. Проверка статуса билета
        if (boardingPass.getTicket() != null &&
                boardingPass.getTicket().getStatus() != TicketEntity.TicketStatus.CHECKED_IN) {
            message.append("Пассажир не зарегистрирован на рейс. ");
            isReady = false;
        }

        // 5. Проверка, не посажен ли уже
        if (Boolean.TRUE.equals(boardingPass.getBoarded())) {
            message.append("Пассажир уже посажен. ");
            isReady = false;
        }

        // Если все проверки пройдены
        if (isReady) {
            message.append("Пассажир готов к посадке.");
        }

        BoardingPassDto boardingPassDto = BoardingPassMapper.map(boardingPass);

        return new BoardingReadinessCheck(isReady, message.toString(),
                boardingPassDto, passportVerified, luggageVerified);
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    /**
     * Проверяет все требования перед посадкой пассажира
     */
    private void checkBoardingRequirements(BoardingPassEntity boardingPass) {
        StringBuilder errorMessage = new StringBuilder();

        // 1. Проверка паспорта
        if (!Boolean.TRUE.equals(boardingPass.getPassportVerified())) {
            errorMessage.append("Паспорт не проверен. ");
        }

        // 2. Проверка багажа
        if (!Boolean.TRUE.equals(boardingPass.getLuggageVerified())) {
            errorMessage.append("Багаж не проверен. ");
        }

        // 3. Проверка статуса рейса
        if (boardingPass.getTicket() == null ||
                boardingPass.getTicket().getFlight() == null ||
                boardingPass.getTicket().getFlight().getStatus() !=
                        rut.miit.airportweb.dao.entity.FlightEntity.FlightStatus.BOARDING) {
            errorMessage.append("Рейс не в статусе посадки. ");
        }

        // 4. Проверка статуса билета
        if (boardingPass.getTicket() == null ||
                boardingPass.getTicket().getStatus() != TicketEntity.TicketStatus.CHECKED_IN) {
            errorMessage.append("Пассажир не зарегистрирован на рейс. ");
        }

        // 5. Проверка, не посажен ли уже
        if (Boolean.TRUE.equals(boardingPass.getBoarded())) {
            errorMessage.append("Пассажир уже посажен. ");
        }

        // Если есть ошибки, бросаем исключение
        if (errorMessage.length() > 0) {
            throw new NotPermittedOperation(
                    "Cannot board passenger: " + errorMessage.toString());
        }
    }

    // ========== ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ (не в интерфейсе, но полезные) ==========

    /**
     * Получить посадочные талоны для рейса
     */
    @Transactional(readOnly = true)
    public java.util.List<BoardingPassDto> getBoardingPassesByFlight(String flightNumber) {
        log.debug("Getting boarding passes for flight: {}", flightNumber);

        // В реальном приложении здесь был бы запрос к репозиторию
        // Пока возвращаем пустой список
        return java.util.List.of();
    }

    /**
     * Получить посадочные талоны, готовые к посадке
     */
    @Transactional(readOnly = true)
    public java.util.List<BoardingPassDto> getReadyForBoarding(String flightNumber) {
        log.debug("Getting boarding passes ready for boarding for flight: {}", flightNumber);

        // В реальном приложении здесь была бы логика фильтрации
        // Пока возвращаем пустой список
        return java.util.List.of();
    }

    /**
     * Получить статистику по посадке для рейса
     */
    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getBoardingStatistics(String flightNumber) {
        log.debug("Getting boarding statistics for flight: {}", flightNumber);

        java.util.Map<String, Long> statistics = new java.util.HashMap<>();

        // В реальном приложении здесь были бы запросы к БД
        statistics.put("total", 0L);
        statistics.put("checkedIn", 0L);
        statistics.put("passportVerified", 0L);
        statistics.put("luggageVerified", 0L);
        statistics.put("boarded", 0L);
        statistics.put("readyForBoarding", 0L);

        return statistics;
    }
}