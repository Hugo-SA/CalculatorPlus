package br.edu.calc.plus.CalculatorPlus.UnitTests;
import br.edu.calc.plus.CalculatorPlusApplication;
import br.edu.calc.plus.domain.dto.UserDTO;
import br.edu.calc.plus.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

 @SpringBootTest(classes = CalculatorPlusApplication.class)//@SpringBootTest
class UsuarioServiceTest {
     @Autowired
 private UsuarioService usuarioService;

     @Test
     void testLoginDuplicado() {
         UserDTO user1 = new UserDTO("Ana", "loginEspecial", "Senha123", "Cidade", "ana@exemplo.com", LocalDate.of(2000, 1, 1));
         UserDTO user2 = new UserDTO("Bruno", "loginEspecial", "Senha321", "Cidade", "bruno@exemplo.com", LocalDate.of(1998, 5, 20));

         usuarioService.save(user1);
         usuarioService.save(user2);
     }
}