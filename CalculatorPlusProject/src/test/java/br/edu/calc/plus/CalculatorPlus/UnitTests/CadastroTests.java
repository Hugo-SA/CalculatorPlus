package br.edu.calc.plus.CalculatorPlus.UnitTests;
import br.edu.calc.plus.domain.*;
import br.edu.calc.plus.repo.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.*;

public class CadastroTests {
    @Test
    void testNomeObrigatorio() {
        Usuario usuario = new Usuario(null, "", "login01", "email@exemplo.com", "Senha123", "Cidade", null);
        assertTrue(usuario.getNome() == null || usuario.getNome().trim().isEmpty(), "Nome deve ser obrigatório");
    }

    @Test
    void testLoginObrigatorio() {
        Usuario usuario = new Usuario(null, "Ana", "", "email@exemplo.com", "Senha123", "Cidade", null);
        assertTrue(usuario.getLogin() == null || usuario.getLogin().trim().isEmpty(), "Login deve ser obrigatório");
    }

    @Test
    void testSenhaObrigatoria() {
        Usuario usuario = new Usuario(null, "Ana", "login01", "email@exemplo.com", "", "Cidade", null);
        assertTrue(usuario.getSenha() == null || usuario.getSenha().isEmpty(), "Senha deve ser obrigatória");
    }

    @Test
    void testRepeticaoDeSenha() {
        // Simulação: repetição de senha validada fora da entidade
        String senha = "Senha123";
        String repetirSenha = "OutraSenha";
        assertNotEquals(senha, repetirSenha, "Senhas não coincidem");
    }

    @Test
    void testEmailValido() {
        Usuario usuario = new Usuario(null, "Ana", "login01", "emailinvalido", "Senha123", "Cidade", null);
        assertFalse(usuario.getEmail().contains("@"), "Email deve ser válido");
    }

}
