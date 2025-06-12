package br.com.controlextras.modelo;


public class Cargo {
    private int id;
    private String tipo;

    public Cargo(String tipo) {
        this.tipo = tipo;
    }
    
    public Cargo() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {
        return this.tipo;
    }
    
    @Override
    public boolean equals (Object objeto){
        Cargo cargo = (Cargo) objeto;
        return this.id == cargo.getId();
    }
}
