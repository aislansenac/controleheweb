package com.aislan.controle_horas_extras.repository;

import com.aislan.controle_horas_extras.data.HoraExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoraExtraRepository extends JpaRepository<HoraExtraEntity, Integer>{
    
}
