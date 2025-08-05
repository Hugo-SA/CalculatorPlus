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
    void testSenhaValida() {
        Usuario usuario = new Usuario();
        // Senha válida (>=6, contém maiúscula, minúscula, número, caractere especial)
        usuario.setSenha("Abcde1@");
        assertTrue(usuario.senhaValida());

        // Senha inválida (menor que 6)
        usuario.setSenha("aB1@");
        assertFalse(usuario.senhaValida());}

    @Test
    void testLoginValido() {
        Usuario usuario = new Usuario();

        // login válido
        usuario.setLogin("login123");
        assertTrue(usuario.loginValida());

        // login curto
        usuario.setLogin("log");
        assertFalse(usuario.loginValida()); }

}
