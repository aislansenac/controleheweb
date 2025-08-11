package com.aislan.controle_horas_extras.controller;

import com.aislan.controle_horas_extras.data.FuncionarioEntity;
import com.aislan.controle_horas_extras.data.FuncionarioHoraExtraEntity;
import com.aislan.controle_horas_extras.data.HoraExtraEntity;
import com.aislan.controle_horas_extras.service.HoraExtraService;
import jakarta.validation.Valid;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/horaextra")
public class HoraExtraController {

    @Autowired
    HoraExtraService heService;

    @PostMapping("/adicionar")
    public ResponseEntity<HoraExtraEntity> addHoraExtra(@Valid @RequestBody HoraExtraEntity he) {
        HoraExtraEntity horaextra = heService.criarHoraExtra(he);
        return new ResponseEntity<>(horaextra, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List> getAllHoraExtras() {
        List<HoraExtraEntity> horaextras = heService.listarTodasHoraExtras();
        return new ResponseEntity<>(horaextras, HttpStatus.OK);
    }

    @PostMapping("/inscrever")
    public ResponseEntity<FuncionarioHoraExtraEntity> addHoraExtra(@Valid @RequestBody FuncionarioHoraExtraEntity funcHe) {
        FuncionarioHoraExtraEntity horaextra = heService.inscreverFuncionario(funcHe);
        return new ResponseEntity<>(horaextra, HttpStatus.OK);
    }

    @GetMapping("/listar-nao-verificadas")
    public ResponseEntity<List<HoraExtraEntity>> listarHorasNaoVerificadas() {
        List<HoraExtraEntity> horasNaoVerificadas = heService.listarTodasNaoVerificadas();
        return ResponseEntity.ok(horasNaoVerificadas);
    }

    @GetMapping("/funcionarios-por-horas")
    public ResponseEntity<List<Map<String, Object>>> listarFuncionariosPorTotalHoras() {
        List<Object[]> resultados = heService.listarHoraTotalPorFuncionario();
        List<Map<String, Object>> resposta = resultados.stream().map(linha -> {
            FuncionarioEntity funcionario = (FuncionarioEntity) linha[0];
            Time totalHoras = (Time) linha[1];

            Map<String, Object> item = new HashMap<>();
            item.put("id", funcionario.getId());
            item.put("cpf", funcionario.getCpf());
            item.put("nome", funcionario.getNome());
            item.put("data", funcionario.getDataNascimento());
            item.put("salario", funcionario.getSalario());
            item.put("cargo", funcionario.getCargo().getTipo());
            item.put("totalHoras", totalHoras);

            return item;
        }).toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/listar-disponiveis/{funcionarioId}")
    public ResponseEntity<List<HoraExtraEntity>> listarHorasDisponiveis(@PathVariable Integer funcionarioId) {
        List<HoraExtraEntity> todas = heService.listarTodasHoraExtras();

        List<HoraExtraEntity> disponiveis = todas.stream()
                .filter(he -> he.getFuncionariosInscritos().stream()
                .noneMatch(fhe -> fhe.getFuncionario().getId().equals(funcionarioId)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(disponiveis);
    }

    @GetMapping("/listar-inscritas/{funcionarioId}")
    public ResponseEntity<List<HoraExtraEntity>> listarHorasInscritas(@PathVariable Integer funcionarioId) {
        List<HoraExtraEntity> todas = heService.listarTodasHoraExtras();

        List<HoraExtraEntity> inscritas = todas.stream()
                .filter(he -> he.getFuncionariosInscritos().stream()
                .anyMatch(fhe -> fhe.getFuncionario().getId().equals(funcionarioId)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(inscritas);
    }

    @PatchMapping("/aprovar/{heId}")
    public ResponseEntity<List<HoraExtraEntity>> aprovarHorasExtras(
            @PathVariable Integer heId,
            @RequestBody Map<String, Boolean> payload) {
        Boolean aprovada = payload.get("aprovada");
        Boolean verificada = payload.get("verificada");
        heService.aprovarHoraExtra(heId, verificada, aprovada);
        return ResponseEntity.ok().build();
    }
}
