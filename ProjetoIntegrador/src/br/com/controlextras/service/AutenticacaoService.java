package br.com.controlextras.service;

import br.com.controlextras.data.FuncionarioDAO;
import br.com.controlextras.service.criptografia.CriptografiaService;


public class AutenticacaoService {
    private FuncionarioDAO funcionarioDao;
    private CriptografiaService criptoService;

    public AutenticacaoService(FuncionarioDAO funcionarioDao, CriptografiaService criptoService) {
        this.funcionarioDao = funcionarioDao;
        this.criptoService = criptoService;
    }

    public boolean autenticar(String cpf, String senhaDigitada) {
        String hashBanco = funcionarioDao.buscarHashSenha(cpf);

        if (hashBanco != null) {
            String hashDigitada = criptoService.criptografar(senhaDigitada);
            return hashBanco.equals(hashDigitada);
        }

        return false;
    }
}
