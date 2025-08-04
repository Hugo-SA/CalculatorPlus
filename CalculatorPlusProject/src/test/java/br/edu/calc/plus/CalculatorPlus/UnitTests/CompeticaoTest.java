package br.edu.calc.plus.CalculatorPlus.UnitTests;

import br.edu.calc.plus.domain.Partida;
import br.edu.calc.plus.domain.Usuario;
import br.edu.calc.plus.repo.UsuarioRepo;
import br.edu.calc.plus.service.PartidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(classes = br.edu.calc.plus.CalculatorPlusApplication.class)
class CompeticaoTest {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario(null, "Ana", "ana01", "ana@exemplo.com", "Senha123", "Cidade", LocalDate.of(2000, 1, 1));
        usuarioRepo.save(usuario);
    }

    @Test
    void testBloqueioParticipacaoDiaria() throws Exception {
        partidaService.iniciarPartida(usuario.getId());
        boolean jaCompetiuHoje = partidaService.userJaCompetiuHoje(usuario.getId());
        assertTrue(jaCompetiuHoje, "Usuário deveria estar bloqueado para participar novamente hoje");
    }

    @Test
    void testRegistroAcertosErrosNaPartida() throws Exception {
        Partida partida = partidaService.iniciarPartida(usuario.getId());

        // Simula respostas
        int posicao = 1;
        int idJogo1 = partida.getJogoList().get(0).getIdjogo();
        double valorCorreto = partida.getJogoList().get(0).getResultado();
        double valorErrado = valorCorreto + 1;

        // Resposta correta
        partidaService.savePartida(partida.getId(), usuario.getId(), posicao, idJogo1, valorCorreto);

        // Resposta errada
        int idJogo2 = partida.getJogoList().get(1).getIdjogo();
        partidaService.savePartida(partida.getId(), usuario.getId(), posicao + 1, idJogo2, valorErrado);

        // Recupera a partida pelo serviço, que carrega a lista dentro de uma transação
        Partida partidaAtualizada = partidaService.getPartida(partida.getId(), usuario.getId());

        assertTrue(partidaAtualizada.getAcertos() >= 1, "Deveria ter pelo menos um acerto registrado");
        assertTrue(partidaAtualizada.getErros() >= 1, "Deveria ter pelo menos um erro registrado");
    }
}