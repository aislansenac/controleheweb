package com.aislan.controle_horas_extras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aislan.controle_horas_extras.data.FuncionarioEntity;

@Repository
public interface FuncionarioRespository extends JpaRepository <FuncionarioEntity, Integer> {
    FuncionarioEntity findByNome(String nome);
    FuncionarioEntity findByCpf(String cpf);
}
