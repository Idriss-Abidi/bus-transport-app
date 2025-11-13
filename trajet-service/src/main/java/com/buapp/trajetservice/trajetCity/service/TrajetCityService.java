package com.buapp.trajetservice.trajetCity.service;

import com.buapp.trajetservice.trajetCity.dto.TrajetCityRequestDTO;
import com.buapp.trajetservice.trajetCity.dto.TrajetCityResponseDTO;
import com.buapp.trajetservice.trajetCity.mapper.TrajetCityMapper;
import com.buapp.trajetservice.trajetCity.model.TrajetCity;
import com.buapp.trajetservice.trajetCity.repository.TrajetCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrajetCityService {

    private final TrajetCityRepository trajetCityRepository;
    private final TrajetCityMapper trajetCityMapper;

    public TrajetCityResponseDTO create(TrajetCityRequestDTO dto) {
        TrajetCity entity = trajetCityMapper.toEntity(dto);
        TrajetCity saved = trajetCityRepository.save(entity);
        return trajetCityMapper.toDTO(saved);
    }

    public List<TrajetCityResponseDTO> listAll() {
        return trajetCityRepository.findAll().stream().map(trajetCityMapper::toDTO).collect(Collectors.toList());
    }

    public TrajetCityResponseDTO getById(Long id) {
        TrajetCity c = trajetCityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        return trajetCityMapper.toDTO(c);
    }

    @Transactional
    public TrajetCityResponseDTO update(Long id, TrajetCityRequestDTO dto) {
        TrajetCity existing = trajetCityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
        existing.setCityName(dto.getCityName());
        existing.setPriceInDhs(dto.getPriceInDhs());
        // repository save not strictly necessary due to transactional dirty checking
        TrajetCity saved = trajetCityRepository.save(existing);
        return trajetCityMapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!trajetCityRepository.existsById(id)) throw new RuntimeException("City not found");
        trajetCityRepository.deleteById(id);
    }
}
