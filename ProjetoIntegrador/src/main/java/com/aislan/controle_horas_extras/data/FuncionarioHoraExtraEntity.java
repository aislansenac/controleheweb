package com.aislan.controle_horas_extras.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "funcionarios_horaextras")
public class FuncionarioHoraExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"horasExtrasCriadas", "horasExtrasInscritas", "cargo"})
    private FuncionarioEntity funcionario;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "horaextra_id", nullable = false)
    @JsonIgnoreProperties({"funcionariosInscritos", "criador"})
    private HoraExtraEntity horaExtra;
    
    private Boolean aprovada = false;
    
    private Boolean verificada = false;
}
