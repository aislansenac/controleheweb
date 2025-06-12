package br.com.controlextras.service.criptografia;

import br.com.controlextras.funcao.Criptografia;

public class MD5CriptografiaService implements CriptografiaService{

    @Override
    public String criptografar(String texto) {
        return Criptografia.getMD5(texto);
    }
    
    @Override
    public boolean verificarSenha(String senha, String hash) {
        return criptografar(senha).equals(hash);
    }
}
