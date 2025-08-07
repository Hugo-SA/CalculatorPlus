package br.edu.calc.plus.CalculatorPlus.UnitTests;
import br.edu.calc.plus.domain.*;
import br.edu.calc.plus.domain.dto.UserDTO;
import br.edu.calc.plus.repo.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroTests {

    @Test
    void testSenhaValida() {
        Usuario usuario = new Usuario();
        // Senha válida (>=6, contém maiúscula, minúscula, número, caractere especial)
        usuario.setSenha("Abcde1@");
        assertTrue(usuario.senhaValida());}

    @Test
    void testSenhaInvalida() {
        Usuario usuario = new Usuario();
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

    @Test
    void testLoginInvalido() {
        Usuario usuario = new Usuario();


        // login curto
        usuario.setLogin("log");
        assertFalse(usuario.loginValida()); }

    @Test
    void testSenhaERepetirSenhaIguais() {
        UserDTO userDTO = new UserDTO(
                "Joao",
                "joao123",
                "MinhaSenha@123",
                "joao@exemplo.com",
                "MinhaSenha@123", // repetirSenha igual à senha
                "Cidade",
                LocalDate.of(2001, 5, 21)
        );
        assertEquals(userDTO.getSenha(), userDTO.getRepetirSenha(), "Os campos senha e repetir senha devem ser iguais.");
    }
    @Test
    void testSenhaERepetirSenhaDiferentes() {
        UserDTO userDTO = new UserDTO(
                "Maria",
                "maria123",
                "SenhaForte@2024",
                "maria@exemplo.com",
                "OutraSenha@2024", // repetirSenha diferente
                "Cidade",
                LocalDate.of(2002, 7, 11)
        );
        assertNotEquals(userDTO.getSenha(), userDTO.getRepetirSenha(), "Os campos senha e repetir senha não devem ser diferentes.");
    }

}
