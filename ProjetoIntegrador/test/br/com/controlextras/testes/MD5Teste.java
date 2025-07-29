package br.com.controlextras.testes;

import br.com.controlextras.service.criptografia.MD5CriptografiaService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class MD5Teste {
    private final MD5CriptografiaService service = new MD5CriptografiaService();
    public MD5Teste() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testVerificarSenha_Correta() {
        String senhaOriginal = "minhaSenha123";
        String hash = service.criptografar(senhaOriginal);

        boolean resultado = service.verificarSenha("minhaSenha123", hash);
        Assert.assertTrue("A senha deveria é válida.", resultado);
    }
    
    @Test
    public void testVerificarSenha_Incorreta() {
        String senhaOriginal = "senhaerrada";
        String hash = service.criptografar(senhaOriginal);

        boolean resultado = service.verificarSenha("minhaSenha123", hash);
        Assert.assertTrue("A senha deveria é inválida.", resultado);
    }
}
