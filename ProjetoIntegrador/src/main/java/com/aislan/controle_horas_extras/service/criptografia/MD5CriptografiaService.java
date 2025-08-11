package com.aislan.controle_horas_extras.service.criptografia;
import com.aislan.controle_horas_extras.funcao.Criptografia;
import org.springframework.stereotype.Service;

@Service
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
