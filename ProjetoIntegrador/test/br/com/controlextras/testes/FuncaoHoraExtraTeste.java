package br.com.controlextras.testes;

import br.com.controlextras.modelo.Funcionario;
import br.com.controlextras.modelo.HoraExtra;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class FuncaoHoraExtraTeste {
    private HoraExtra horaExtra;
    
    public FuncaoHoraExtraTeste() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        LocalDate data = LocalDate.of(2025, 7, 29);
        LocalTime inicio = LocalTime.of(18, 0);
        LocalTime fim = LocalTime.of(20, 30);

        Funcionario criador = new Funcionario();
        criador.setNome("Aislan");

        horaExtra = new HoraExtra(data, inicio, fim, criador, "Hora extra para repor estoque");
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void testDuracaoHoraExtra_valido() {
        Duration duracao = horaExtra.getDuracao();
        assertEquals("Tempo diferente de 150 minutos", Duration.ofMinutes(150), duracao);
    }
    
    @Test
    public void testCriadorNome_valido() {
        assertEquals("Aislan", horaExtra.getCriador().getNome());
    }
    
    @Test
    public void testCriadorNome_invalido() {
        assertEquals("Criador esperado é Aislan", "Maria", horaExtra.getCriador().getNome());
    }
}
