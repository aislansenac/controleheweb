package com.aislan.controle_horas_extras.service;

import com.aislan.controle_horas_extras.data.FuncionarioEntity;
import com.aislan.controle_horas_extras.funcao.Criptografia;
import com.aislan.controle_horas_extras.repository.FuncionarioRespository;
import com.aislan.controle_horas_extras.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class FuncionarioService {

    @Autowired
    FuncionarioRespository funcionarioRepository;

    public FuncionarioEntity criarFuncionario(FuncionarioEntity funcionario) {
        String senhaCriptografada = Criptografia.getMD5(funcionario.getSenha());

        funcionario.setId(null);
        funcionario.setSenha(senhaCriptografada);
        funcionarioRepository.save(funcionario);
        return funcionario;
    }

    public FuncionarioEntity pesquisarFuncionarioPorNome(String nome) {
        return funcionarioRepository.findByNome(nome);
    }

    public FuncionarioEntity pesquisarFuncionarioPorCpf(String cpf) {
        return funcionarioRepository.findByCpf(cpf);
    }

    public List<FuncionarioEntity> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }
    public List<FuncionarioEntity> listarTodosFuncionarios(Integer funId) {
        return funcionarioRepository.findAll().stream()
        .filter(f -> !f.getId().equals(funId))
        .collect(Collectors.toList());
    }
    public FuncionarioEntity atualizarFuncionario(Integer funcId, FuncionarioEntity funcRequest) {
        FuncionarioEntity funcionario = getFuncionarioId(funcId);
        funcionario.setNome(funcRequest.getNome());
        funcionario.setSalario(funcRequest.getSalario());
        funcionario.setCpf(funcRequest.getCpf());
        funcionario.setDataNascimento(funcRequest.getDataNascimento());
        funcionario.setCargo(funcRequest.getCargo());
        funcionarioRepository.save(funcionario);

        return funcionarioRepository.save(funcionario);
    }

    public void atualizarStatusAtivo(Integer id, boolean novoStatus) {
        FuncionarioEntity funcionario = getFuncionarioId(id);
        funcionario.setAtivo(novoStatus);
        funcionarioRepository.save(funcionario);
    }
    
    public void alterarSenha(Integer id, String novaSenha) {
        FuncionarioEntity funcionario = getFuncionarioId(id);
        String senhaCriptografada = Criptografia.getMD5(novaSenha);
        funcionario.setSenha(senhaCriptografada);
        funcionarioRepository.save(funcionario);
    }
    
    public FuncionarioEntity getFuncionarioId(Integer funcId) {
        FuncionarioEntity funcionario = (FuncionarioEntity) funcionarioRepository.
                findById(funcId).orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado " + funcId));
        return funcionario;
    }
}
