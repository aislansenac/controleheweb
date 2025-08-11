package com.aislan.controle_horas_extras.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "hora_extra")
public class HoraExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Data obrigatória")
    @Column(nullable = false)
    private LocalDate data;
    
    @NotNull(message = "Hora de Início obrigatória")
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    @NotNull(message = "Hora de Término obrigatória")
    @Column(nullable = false)
    private LocalTime horaTermino;
    
    @NotNull(message = "Quantidades de vagas obrigatório")
    @Column(nullable = false)
    private Integer vagas;
    
    @NotBlank(message = "Observação obrigatória")
    @Column(nullable = false)
    private String observacao;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criador_id", nullable = false)
    @JsonIgnoreProperties("horasExtrasCriadas")
    private FuncionarioEntity criador;
    
    @OneToMany(mappedBy = "horaExtra", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("horaExtra")
    private List<FuncionarioHoraExtraEntity> funcionariosInscritos;
}
