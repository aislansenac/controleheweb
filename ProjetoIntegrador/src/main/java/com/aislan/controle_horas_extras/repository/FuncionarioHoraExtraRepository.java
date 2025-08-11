package com.aislan.controle_horas_extras.repository;

import com.aislan.controle_horas_extras.data.FuncionarioHoraExtraEntity;
import com.aislan.controle_horas_extras.data.HoraExtraEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioHoraExtraRepository extends JpaRepository<FuncionarioHoraExtraEntity, Integer>{
    @Query("SELECT DISTINCT fhe.horaExtra FROM FuncionarioHoraExtraEntity fhe " +
           "WHERE fhe.verificada = false")
    List<HoraExtraEntity> findHoraExtrasNaoVerificadas();
    
    @Query("SELECT fhe.funcionario, " +
       "FUNCTION('SEC_TO_TIME', SUM(FUNCTION('TIME_TO_SEC', FUNCTION('TIMEDIFF', he.horaTermino, he.horaInicio)))) AS totalHoras " +
       "FROM FuncionarioHoraExtraEntity fhe " +
       "JOIN fhe.horaExtra he " +
       "WHERE fhe.aprovada = true " +
       "GROUP BY fhe.funcionario " +
       "ORDER BY totalHoras DESC")
    List<Object[]> listarFuncionariosPorTotalHoras();

}
