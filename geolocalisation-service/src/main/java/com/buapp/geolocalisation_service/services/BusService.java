package com.buapp.geolocalisation_service.services;

import com.buapp.geolocalisation_service.models.Bus;
import com.buapp.geolocalisation_service.repositories.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public Bus createBus(Bus bus) {
        return busRepository.save(bus);
    }

    public Bus getBus(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with id = " + id));
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Bus updateBus(Long id, Bus updatedBus) {
        Bus bus = getBus(id); // throws if not found

        bus.setMatricule(updatedBus.getMatricule());
        bus.setDescription(updatedBus.getDescription());
        bus.setTrajetId(updatedBus.getTrajetId());

        return busRepository.save(bus);
    }

    public void deleteBus(Long id) {
        Bus bus = getBus(id); // throws if not found
        busRepository.delete(bus);
    }
}
