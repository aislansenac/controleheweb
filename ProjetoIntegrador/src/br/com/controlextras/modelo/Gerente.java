package br.com.controlextras.modelo;

public class Gerente extends Coordenador{
    
    public Gerente() {    
    }
    public Gerente(Funcionario f) {
        this.setId(f.getId());
        this.setNome(f.getNome());
        this.setCpf(f.getCpf());
        this.setDataNascimento(f.getDataNascimento());
        this.setSalario(f.getSalario());
        this.setSenha(f.getSenha());
        this.setCargo(f.getCargo());
        this.setAtivo(f.isAtivo());
    }
}
