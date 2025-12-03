package rut.miit.airportweb.service;

import rut.miit.airportweb.dto.BoardingPassCreateDto;
import rut.miit.airportweb.dto.BoardingPassDto;

/**
 * Сервис для управления посадочными талонами
 */
public interface BoardingPassService {

    /**
     * Получить посадочный талон по ID
     * @param id идентификатор посадочного талона
     * @return Optional с посадочным талоном
     */
    BoardingPassDto getBoardingPassById(Integer id);

    /**
     * Получить посадочный талон по номеру билета
     * @param ticketNumber номер билета
     * @return Optional с посадочным талоном
     */
    BoardingPassDto getBoardingPassByTicketNumber(String ticketNumber);

    /**
     * Создать посадочный талон
     * @param boardingPassCreateDto DTO создания посадочного талона
     * @return созданный посадочный талон
     */
    BoardingPassDto createBoardingPass(BoardingPassCreateDto boardingPassCreateDto);

//    /**
//     * Обновить посадочный талон
//     * @param id идентификатор посадочного талона
//     * @param boardingPassUpdateDto DTO обновления посадочного талона
//     * @return обновленный посадочный талон
//     */
//    BoardingPassDto updateBoardingPass(Integer id, BoardingPassUpdateDto boardingPassUpdateDto);

    /**
     * Удалить посадочный талон
     * @param id идентификатор посадочного талона
     */
    void deleteBoardingPass(Integer id);

    /**
     * Обновить статус проверки паспорта
     * @param boardingPassId ID посадочного талона
     * @param passportVerified статус проверки паспорта
     * @param verifiedByUserId ID пользователя, проверившего паспорт
     * @return обновленный посадочный талон
     */
    BoardingPassDto updatePassportVerification(Integer boardingPassId,
                                                         boolean passportVerified,
                                                         Integer verifiedByUserId);

    /**
     * Обновить статус проверки багажа
     * @param boardingPassId ID посадочного талона
     * @param luggageVerified статус проверки багажа
     * @param verifiedByUserId ID пользователя, проверившего багаж
     * @return обновленный посадочный талон
     */
    BoardingPassDto updateLuggageVerification(Integer boardingPassId,
                                                        boolean luggageVerified,
                                                        Integer verifiedByUserId);

    /**
     * Обновить статус посадки
     * @param boardingPassId ID посадочного талона
     * @param boarded статус посадки
     * @return обновленный посадочный талон
     */
    BoardingPassDto updateBoardingStatus(Integer boardingPassId, boolean boarded);

    /**
     * Проверить готовность пассажира к посадке
     * @param boardingPassId ID посадочного талона
     * @return результат проверки
     */
    BoardingReadinessCheck checkBoardingReadiness(Integer boardingPassId);


    /**
     * Проверка готовности к посадке
     */
    class BoardingReadinessCheck {
        private final boolean isReady;
        private final String message;
        private final BoardingPassDto boardingPass;
        private final boolean passportVerified;
        private final boolean luggageVerified;

        public BoardingReadinessCheck(boolean isReady, String message,
                                      BoardingPassDto boardingPass,
                                      boolean passportVerified,
                                      boolean luggageVerified) {
            this.isReady = isReady;
            this.message = message;
            this.boardingPass = boardingPass;
            this.passportVerified = passportVerified;
            this.luggageVerified = luggageVerified;
        }

        // Getters
        public boolean isReady() { return isReady; }
        public String getMessage() { return message; }
        public BoardingPassDto getBoardingPass() { return boardingPass; }
        public boolean isPassportVerified() { return passportVerified; }
        public boolean isLuggageVerified() { return luggageVerified; }
    }

}
