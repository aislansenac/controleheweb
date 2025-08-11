package com.aislan.controle_horas_extras.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "funcionario")
public class FuncionarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "Senha obrigatória")
    @Column(nullable = false)
    private String senha;

    @NotNull(message = "Data de nascimento obrigatória")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "CPF obrigatório")
    @Column(nullable = false)
    private String cpf;

    @NotNull(message = "Salário obrigatório")
    private Double salario;

    @NotNull(message = "Ativo obrigatório")
    private Boolean ativo = true;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id", nullable = false)
    @JsonIgnoreProperties("funcionarios")
    private CargoEntity cargo;
    
    @OneToMany(mappedBy = "criador", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("criador")
    private List<HoraExtraEntity> horasExtrasCriadas;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("funcionario")
    private List<FuncionarioHoraExtraEntity> horasExtrasInscritas;
}
