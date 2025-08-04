package br.edu.calc.plus.CalculatorPlus.FuncionalTests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.util.concurrent.TimeUnit;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FuncionalTests {

    private static WebDriver driver;
    private static final String BASE_URL = "http://localhost:9000"; // ajuste conforme porta do seu projeto

    @BeforeAll
    static void setUp() {
        // Caminho do chromedriver
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    // 1. Cadastro de usuário com dados válidos
    @Test
    @Order(1)
    void cadastroUsuarioValido() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).sendKeys("Teste Selenium");
        driver.findElement(By.id("cpLogin")).sendKeys("selenium01");
        driver.findElement(By.id("cpEmail")).sendKeys("selenium01@email.com");
        driver.findElement(By.id("cpSenha")).sendKeys("Senha123");
        driver.findElement(By.id("cpCidade")).sendKeys("Cidade");
        driver.findElement(By.id("cpData")).sendKeys("2000-01-01");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains("/home"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"), "Usuário deve ser redirecionado após cadastro");
    }

    // 2. Cadastro com campos obrigatórios faltando
    @Test
    @Order(2)
    void cadastroCamposObrigatoriosFaltando() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).clear();
        driver.findElement(By.id("cpLogin")).clear();
        driver.findElement(By.id("cpEmail")).clear();
        driver.findElement(By.id("cpSenha")).clear();
        driver.findElement(By.id("cpCidade")).clear();
        driver.findElement(By.id("cpData")).clear();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // valida se a página não saiu do cadastro
        Assertions.assertTrue(driver.getCurrentUrl().contains("/user"),
                "Deve permanecer na página de cadastro após erro");
}

    // 3. Cadastro com senhas diferentes
    @Test
    @Order(3)
    void cadastroSenhasDiferentes() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).sendKeys("Teste Senha");
        driver.findElement(By.id("cpLogin")).sendKeys("selenium02");
        driver.findElement(By.id("cpEmail")).sendKeys("selenium02@email.com");
        driver.findElement(By.id("cpSenha")).sendKeys("Senha123");
        driver.findElement(By.id("cpCidade")).sendKeys("Cidade");
        driver.findElement(By.id("cpNascimento")).sendKeys("2000-01-01");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getPageSource().contains("Senhas não coincidem"),
                "Deve exibir alerta de senhas diferentes");
    }

    // 4. Cadastro com login já existente
    @Test
    @Order(4)
    void cadastroLoginJaExistente() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).sendKeys("Teste Login");
        driver.findElement(By.id("cpLogin")).sendKeys("selenium01"); // já cadastrado no teste 1
        driver.findElement(By.id("cpEmail")).sendKeys("selenium03@email.com");
        driver.findElement(By.id("cpSenha")).sendKeys("Senha123");
        driver.findElement(By.id("cpCidade")).sendKeys("Cidade");
        driver.findElement(By.id("cpNascimento")).sendKeys("2000-01-01");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getPageSource().contains("Login já está em uso"),
                "Deve exibir mensagem de login já existente");
    }

    // 5. Login com usuário cadastrado
    @Test
    @Order(5)
    void loginUsuarioCadastrado() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("selenium01");
        driver.findElement(By.id("password")).sendKeys("Senha123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains("/home"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/home"), "Usuário deve acessar a home após login");
    }

    // 6. Participação na competição diária
    @Test
    @Order(6)
    void participacaoCompeticaoDiaria() {
        loginUsuarioCadastrado(); // garante login
        driver.get(BASE_URL + "/competicao/new");
        Assertions.assertTrue(driver.getPageSource().contains("Pergunta") || driver.getPageSource().contains("jogar"),
                "Deve iniciar prova diária");
        // Simule envio de respostas se o fluxo for por formulário
        // driver.findElement(By.name("cpResposta")).sendKeys("42");
        // driver.findElement(By.cssSelector("button[type='submit']")).click();
        // Repita até finalizar
    }

    // 7. Visualização dos resultados
    @Test
    @Order(7)
    void visualizacaoResultados() {
        loginUsuarioCadastrado(); // garante login
        // Supondo que o id da última competição seja acessível
        driver.get(BASE_URL + "/competicao");
        Assertions.assertTrue(driver.getPageSource().contains("Acertos"), "Deve mostrar acertos");
        Assertions.assertTrue(driver.getPageSource().contains("Erros"), "Deve mostrar erros");
        Assertions.assertTrue(driver.getPageSource().contains("Pontuação") || driver.getPageSource().contains("Premiação"), "Deve mostrar premiação");
    }

    // 8. Impedimento de nova participação no mesmo dia
    @Test
    @Order(8)
    void impedimentoNovaCompeticaoMesmoDia() {
        loginUsuarioCadastrado(); // garante login
        driver.get(BASE_URL + "/competicao/new");
        driver.get(BASE_URL + "/competicao/new"); // tenta iniciar novamente
        Assertions.assertTrue(driver.getPageSource().contains("Já Participou da competição hoje"),
                "Usuário não pode repetir a prova no mesmo dia");
    }
}