package com.aislan.controle_horas_extras.service;
import com.aislan.controle_horas_extras.data.CargoEntity;
import com.aislan.controle_horas_extras.repository.CargoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargoService {
    @Autowired
    CargoRepository cargoRepository;
    
    public CargoEntity criarCargo(CargoEntity cargo) {
        cargo.setId(null);
        cargoRepository.save(cargo);
        return cargo;
    }
    
    public List<CargoEntity> listarTodosCargos() {
        return cargoRepository.findAll();
    }
}
