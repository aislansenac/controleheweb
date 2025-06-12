package br.com.controlextras.aplicacao;

import br.com.controlextras.data.CargoDAO;
import br.com.controlextras.data.FuncHoraExtraDAO;
import br.com.controlextras.data.FuncionarioDAO;
import br.com.controlextras.data.HoraExtraDAO;
import br.com.controlextras.modelo.Cargo;
import br.com.controlextras.modelo.Funcionario;
import br.com.controlextras.modelo.HoraExtra;
import br.com.controlextras.service.AutenticacaoService;
import br.com.controlextras.service.criptografia.CriptografiaService;
import br.com.controlextras.service.criptografia.MD5CriptografiaService;
import java.time.LocalDate;
import java.time.LocalTime;

public class Aplicacao {

    public static void main(String[] args) {
        String cpf = "999.999.999-99";
        String senha = "123456";

        FuncionarioDAO funcionarioDao = new FuncionarioDAO();
        
        /*Aqui mudaria a criptografia */
        CriptografiaService criptoService = new MD5CriptografiaService();
        

        AutenticacaoService authService = new AutenticacaoService(funcionarioDao, criptoService);
        
        /* Verfica se usuário existe*/
        System.out.println(authService.autenticar(cpf, senha) ? "Loga" : "Nao loga");
        

        //CRIA FUNCIONÁRIO
        /*
        Funcionario func = new Funcionario();
        CargoDAO cargoDAO = new CargoDAO();
        Cargo cargo = cargoDAO.getCargo(2);
        func.setNome("NOVO FUNCINÁRIO");
        func.setAtivo(true);
        func.setCpf("123.456.789-11");
        
        func.setCargo(cargo);
        func.setSalario(5000.2);
        func.setDataNascimento(LocalDate.now());
        funcionarioDao.inserir( func, "987654321");
         */
        
        //--------CRIA HORA EXTRA-------
        /*----------INÍCIO--------------
        FuncionarioDAO funcDAO = new FuncionarioDAO();
        Funcionario func = funcDAO.getFuncionario(11);

        if (func != null) {
            HoraExtra he = new HoraExtra();
            he.setCriador(func);
            he.setData(LocalDate.of(2025, 06, 22));
            he.setHoraInicio(LocalTime.of(15, 00));
            he.setHoraFim(LocalTime.of(16, 00));
            he.setObservacao("ESSA HORA FOI CRIADA AGORA");

            HoraExtraDAO heDAO = new HoraExtraDAO();
            heDAO.inserir(he);
        } else {
            System.out.println("ID deste funcionario nao existe");
        }
        ----------------FIM------------------*/
        
    }
}
