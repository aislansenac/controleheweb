package com.aislan.controle_horas_extras.service;

import com.aislan.controle_horas_extras.data.FuncionarioHoraExtraEntity;
import com.aislan.controle_horas_extras.data.HoraExtraEntity;
import com.aislan.controle_horas_extras.exception.ResourceNotFoundException;
import com.aislan.controle_horas_extras.repository.FuncionarioHoraExtraRepository;
import com.aislan.controle_horas_extras.repository.HoraExtraRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoraExtraService {
    @Autowired
    HoraExtraRepository heRepository;
    
     @Autowired
     FuncionarioHoraExtraRepository funcHeRepository;
     
    public HoraExtraEntity criarHoraExtra(HoraExtraEntity horaextra) {
        horaextra.setId(null);
        heRepository.save(horaextra);
        return horaextra;
    }
    
    public List<HoraExtraEntity> listarTodasHoraExtras() {
        return heRepository.findAll();
    }
    
    public FuncionarioHoraExtraEntity inscreverFuncionario(FuncionarioHoraExtraEntity funcHe) {
        funcHe.setId(null);
        funcHeRepository.save(funcHe);
        return funcHe;
    }
    
    public List<HoraExtraEntity> listarTodasNaoVerificadas() {
        return funcHeRepository.findHoraExtrasNaoVerificadas();
    }
    
    public void aprovarHoraExtra(Integer id, boolean verificada, boolean aprovada) {
        FuncionarioHoraExtraEntity fche = getFuncHoraExtraId(id);
        fche.setVerificada(verificada);
        fche.setAprovada(aprovada);
        funcHeRepository.save(fche);
    }
    
    public FuncionarioHoraExtraEntity getFuncHoraExtraId(Integer fheId) {
        FuncionarioHoraExtraEntity funcHoraHe = (FuncionarioHoraExtraEntity) funcHeRepository.
                findById(fheId).orElseThrow(() -> new ResourceNotFoundException("FUNCHE não encontrada " + fheId));
        return funcHoraHe;
    }
    
    public List<Object[]> listarHoraTotalPorFuncionario() {
        return funcHeRepository.listarFuncionariosPorTotalHoras();
    }
}
