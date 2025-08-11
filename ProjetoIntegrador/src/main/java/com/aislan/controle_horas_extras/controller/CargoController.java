package com.aislan.controle_horas_extras.controller;

import com.aislan.controle_horas_extras.data.CargoEntity;
import com.aislan.controle_horas_extras.service.CargoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cargo")
public class CargoController {
    @Autowired
    CargoService cargoService;

    @PostMapping("/adicionar")
    public ResponseEntity<CargoEntity> addCargo(@Valid @RequestBody CargoEntity car) {
        CargoEntity cargo = cargoService.criarCargo(car);
        return new ResponseEntity<>(cargo, HttpStatus.OK);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<List> getAllFuncionarios() {
        List<CargoEntity> cargos = cargoService.listarTodosCargos();
        return new ResponseEntity<>(cargos, HttpStatus.OK);
    }
}
