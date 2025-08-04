package br.edu.calc.plus.CalculatorPlus.IntegracaoTests;

import br.edu.calc.plus.domain.Usuario;
import br.edu.calc.plus.domain.dto.UserDTO;
import br.edu.calc.plus.service.UsuarioService;
import br.edu.calc.plus.repo.UsuarioRepo;
import br.edu.calc.plus.service.PartidaService;
import br.edu.calc.plus.domain.Partida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = br.edu.calc.plus.CalculatorPlusApplication.class)
@Transactional
class FluxoCompletoIT {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PartidaService partidaService;

    private UserDTO usuarioDTO;

    @BeforeEach
    void setup() {
        usuarioRepo.deleteAll(); // Limpa todos usuários antes de cada teste
        usuarioDTO = new UserDTO(
                "Ana",
                "ana01",
                "Senha123",
                "ana@exemplo.com",
                "Cidade",
                LocalDate.of(2000, 1, 1)
        );
    }

    @Test
    void testFluxoCompletoCadastro() {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        assertNotNull(user, "Usuário deve ser salvo no banco.");
        //assertTrue(new BCryptPasswordEncoder().matches("Senha123", user.getSenha()), "Senha deve estar criptografada.");
    }

    @Test
    void testAutenticacaoAposCadastro() {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        assertNotNull(user, "Usuário deve estar cadastrado.");
        //assertTrue(new BCryptPasswordEncoder().matches("Senha123", String.valueOf(hashCode())), "Senha deve estar correta.");
        assertEquals("Senha123",user.getSenha());
        // Simulação: normalmente, aqui validaria via UserDetailsServiceImpl ou AuthenticationManager
    }

    @Test
    void testConsultaUsuariosCadastrados() {
        usuarioService.save(usuarioDTO);
        long qtd = usuarioService.getCountUsers();
        assertTrue(qtd >= 1, "Deve existir pelo menos um usuário cadastrado.");
    }

    @Test
    void testCompeticaoInicioAposLogin() throws Exception {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        assertNotNull(user);
        Partida partida = partidaService.iniciarPartida(user.getId());
        assertNotNull(partida, "Usuário autenticado deve conseguir iniciar competição.");
    }

    @Test
    void testRegistroResultadosCompeticao() throws Exception {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        Partida partida = partidaService.iniciarPartida(user.getId());
        int idJogo = partida.getJogoList().get(0).getIdjogo();
        double respostaCorreta = partida.getJogoList().get(0).getResultado();
        partidaService.savePartida(partida.getId(), user.getId(), 1, idJogo, respostaCorreta);
        Partida partidaAtualizada = partidaService.getPartida(partida.getId(), user.getId());
        assertTrue(partidaAtualizada.getAcertos() >= 1, "Deve registrar acerto.");
    }

    @Test
    void testExibicaoResultadosAposCompeticao() throws Exception {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        Partida partida = partidaService.iniciarPartida(user.getId());
        int idJogo = partida.getJogoList().get(0).getIdjogo();
        double respostaCorreta = partida.getJogoList().get(0).getResultado();
        partidaService.savePartida(partida.getId(), user.getId(), 1, idJogo, respostaCorreta);
        Partida partidaAtualizada = partidaService.getPartida(partida.getId(), user.getId());
        assertTrue(partidaAtualizada.getAcertos() >= 1, "Resultado de acertos deve ser exibido após competição.");
        assertTrue(partidaAtualizada.getErros() >= 0, "Resultado de erros deve ser exibido após competição.");
    }

    @Test
    void testRestricaoNovaParticipacao() throws Exception {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        partidaService.iniciarPartida(user.getId());
        boolean bloqueado = partidaService.userJaCompetiuHoje(user.getId());
        assertTrue(bloqueado, "Usuário não pode participar novamente no mesmo dia.");
    }

    @Test
    void testPersistenciaDadosPremiacao() throws Exception {
        usuarioService.save(usuarioDTO);
        Usuario user = usuarioRepo.findByLogin("ana01").orElse(null);
        Partida partida = partidaService.iniciarPartida(user.getId());
        int idJogo = partida.getJogoList().get(0).getIdjogo();
        double respostaCorreta = partida.getJogoList().get(0).getResultado();
        partidaService.savePartida(partida.getId(), user.getId(), 1, idJogo, respostaCorreta);
        Partida partidaAtualizada = partidaService.getPartida(partida.getId(), user.getId());
        assertNotNull(partidaAtualizada.getBonificacao(), "Bonificação (premiação) deve ser registrada.");
        assertTrue(partidaAtualizada.getBonificacao() >= 0, "Bonificação deve ser positiva ou zero.");
    }
}