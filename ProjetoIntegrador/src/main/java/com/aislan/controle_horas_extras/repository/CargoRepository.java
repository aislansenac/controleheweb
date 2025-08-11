package com.aislan.controle_horas_extras.repository;

import com.aislan.controle_horas_extras.data.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository <CargoEntity, Integer> {
    
}
