package br.com.controlextras.service.criptografia;

public interface CriptografiaService {
    String criptografar(String texto);
    boolean verificarSenha(String senha, String hash);
}
