package br.com.controlextras.modelo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HoraExtra {
    private int id;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Funcionario criador;
    private String observacao;
    private boolean verificada;     // Se o Coordenador já realizaou a verificação
    private boolean inscrito;      // Se ela já alguém ja se inscreveu
    private boolean aprovada;      // Se foi ou não aprovada pelo Coordenadro  
    
    public HoraExtra(LocalDate data, LocalTime horaInicio, LocalTime horaFim, Funcionario func, String observacao) {
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.criador = func;
        this.observacao = observacao;
        this.inscrito = false;
        this.aprovada = false;
        this.verificada = false;
    }
    public HoraExtra(){
        
    }

    public boolean isInscrito() {
        return inscrito;
    }

    public void setInscrito(boolean inscrito) {
        this.inscrito = inscrito;
    }

    public boolean isAprovada() {
        return aprovada;
    }

    public void setAprovada(boolean aprovada) {
        this.aprovada = aprovada;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
    
    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Funcionario criador) {
        this.criador = criador;
    }
    
    public Duration getDuracao() {
        return Duration.between(horaInicio, horaFim);
        
    }
    public boolean isVerificada() {
        return verificada;
    }

    public void setVerificada(boolean verificada) {
        this.verificada = verificada;
    }
    
    public void detalhe() {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = this.getData().format(formatter);
        System.out.println("****************************************");
        System.out.println("\tData: " + dataFormatada);
        System.out.println("\t" + this.getHoraInicio() + " - " + this.getHoraFim());
        System.out.println("\tCriada por: " + ANSI_GREEN + this.getCriador().getNome() + ANSI_RESET);
        System.out.println("****************************************");
    }
}
