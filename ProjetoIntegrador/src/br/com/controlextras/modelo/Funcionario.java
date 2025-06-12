package br.com.controlextras.modelo;

public class Funcionario extends Usuario{
    private Cargo cargo;
    public Funcionario() {
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }    

    public boolean validarSenha(String senhaCriptografada) {
        return this.senha.equals(senhaCriptografada);
    }
}
