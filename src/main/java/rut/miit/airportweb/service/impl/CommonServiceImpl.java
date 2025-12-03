package rut.miit.airportweb.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.airportweb.dao.repository.FlightRepository;
import rut.miit.airportweb.dto.FlightDto;
import rut.miit.airportweb.mapper.FlightMapper;
import rut.miit.airportweb.service.CommonService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    private final FlightRepository flightRepository;

    @Override
    public List<FlightDto> getFlightsList() {
        return this.flightRepository.findAll()
                .stream()
                .map(FlightMapper::map)
                .toList();
    }
}
