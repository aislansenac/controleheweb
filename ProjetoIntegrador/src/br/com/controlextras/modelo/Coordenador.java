package br.com.controlextras.modelo;

public class Coordenador extends Funcionario{
    public Coordenador() {    
    }
    public Coordenador(Funcionario f) {
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
