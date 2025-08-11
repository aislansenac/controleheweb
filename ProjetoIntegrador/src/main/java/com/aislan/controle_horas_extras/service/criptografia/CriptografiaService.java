package com.aislan.controle_horas_extras.service.criptografia;


public interface CriptografiaService {
    String criptografar(String texto);
    boolean verificarSenha(String senha, String hash);
}
