package com.aislan.controle_horas_extras.controller;

import com.aislan.controle_horas_extras.data.FuncionarioEntity;
import com.aislan.controle_horas_extras.service.FuncionarioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/funcionario")
public class FuncionarioController {
    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping("/adicionar")
    public ResponseEntity<FuncionarioEntity> addFuncionario(@Valid @RequestBody FuncionarioEntity func) {
        FuncionarioEntity funcionario = funcionarioService.criarFuncionario(func);
        return new ResponseEntity<>(funcionario, HttpStatus.OK);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<List> getAllFuncionarios() {
        List<FuncionarioEntity> funcionarios = funcionarioService.listarTodosFuncionarios();
        return new ResponseEntity<>(funcionarios, HttpStatus.OK);
    }
    
    @GetMapping("/listar/{id}")
    public ResponseEntity<List> getFuncionarioExcetoId(@PathVariable Integer id) {
        List<FuncionarioEntity> funcionarios = funcionarioService.listarTodosFuncionarios(id);
        return new ResponseEntity<>(funcionarios, HttpStatus.OK);
    }
    
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<FuncionarioEntity> atualizarFuncionario(@PathVariable Integer id, @RequestBody FuncionarioEntity func) {
        FuncionarioEntity funAtualizado = funcionarioService.atualizarFuncionario(id, func);
        return new ResponseEntity<>(funAtualizado, HttpStatus.OK);
    }
    
    @PatchMapping("/alterar-senha/{id}")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Integer id, 
            @RequestBody Map<String, String> payload) {
        String novaSenha = payload.get("novaSenha");
        
        if (novaSenha == null) {
            return ResponseEntity.badRequest().build();
        }
        
        funcionarioService.alterarSenha(id, novaSenha); 
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/atualizar-status/{id}")
    public ResponseEntity<Void> atualizarStatusAtivo(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> payload) {

        Boolean novoStatus = payload.get("ativo");

        if (novoStatus == null) {
            return ResponseEntity.badRequest().build();
        }

        funcionarioService.atualizarStatusAtivo(id, novoStatus);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioEntity> buscarPorId(@PathVariable Integer id) {
        FuncionarioEntity funcionario = funcionarioService.getFuncionarioId(id);
        return ResponseEntity.ok(funcionario);
    }
}
