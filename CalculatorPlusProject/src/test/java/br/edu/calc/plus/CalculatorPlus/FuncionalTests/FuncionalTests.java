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
        driver.findElement(By.id("cpRepetirSenha")).sendKeys("Senha123");
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
        driver.findElement(By.id("cpRepetirSenha")).clear();
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
    void deveDarErroSenhaMenorQueSeis() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).sendKeys("Teste Senha");
        driver.findElement(By.id("cpLogin")).sendKeys("selenium02");
        driver.findElement(By.id("cpEmail")).sendKeys("selenium02@email.com");
        driver.findElement(By.id("cpSenha")).sendKeys("Senha");
        driver.findElement(By.id("cpRepetirSenha")).sendKeys("Senha");
        driver.findElement(By.id("cpCidade")).sendKeys("Cidade");
        driver.findElement(By.id("cpData")).sendKeys("2000-01-01");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getCurrentUrl().contains("/user"),
                "Deve permanecer na página de cadastro após erro");
    }

    // 4. Cadastro com login já existente
    @Test
    @Order(4)
    void deveImpedirSenhaRepetidaInvalida() {
        driver.get(BASE_URL + "/user");
        driver.findElement(By.id("cpNome")).sendKeys("Teste Login");
        driver.findElement(By.id("cpLogin")).sendKeys("selenium01234567"); // login maior que 16 digitos
        driver.findElement(By.id("cpEmail")).sendKeys("selenium03@email.com");
        driver.findElement(By.id("cpSenha")).sendKeys("Senha123");
        driver.findElement(By.id("cpRepetirSenha")).sendKeys("SenhaABC");
        driver.findElement(By.id("cpCidade")).sendKeys("Cidade");
        driver.findElement(By.id("cpData")).sendKeys("2000-01-01");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getPageSource().contains("As senhas não coincidem"));
    }

    // 5. Login com usuário cadastrado
    @Test
    @Order(5)
    void VerificaPaginaHome() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("selenium01");
        driver.findElement(By.id("password")).sendKeys("Senha123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleContains("Calculator")); // Ajuste para o título real
        Assertions.assertTrue(driver.getTitle().contains("Calculator"));
      }

    // 6. Participação na competição diária
    @Test
    @Order(6)
    void usuarioPodeIniciarNovaCompeticao() {
        // Login
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("selenium01");
        driver.findElement(By.id("password")).sendKeys("Senha123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Aguarda a home carregar
        WebDriverWait wait = new WebDriverWait(driver, 10);
        // Clica para iniciar nova competição/partida
        // Ajuste o seletor conforme seu botão real!
        driver.findElement(By.cssSelector("a[href='/competicao']")).click();

        // Aguarda a tela da competição (ajuste o seletor/texto conforme seu HTML)
        driver.findElement(By.cssSelector("a[href='/competicao/new']")).click();

        // Valida que a tela de competição foi aberta
        Assertions.assertTrue(driver.getCurrentUrl().contains("/competicao/new"), "Usuário deve estar na tela de competição após iniciar");

        // (Opcional) Validar elemento exclusivo da competição, ex: título
        // wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tituloCompeticao")));
        // Assertions.assertTrue(driver.findElement(By.id("tituloCompeticao")).isDisplayed());
    }

    // 7. Visualização dos resultados
    @Test
    @Order(7)
    void visualizacaoResultados() {// Login
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("JAJA5");
        driver.findElement(By.id("password")).sendKeys("jajaja");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Aguarda a home carregar
        WebDriverWait wait = new WebDriverWait(driver, 10);
        // Clica para iniciar nova competição/partida
        // Ajuste o seletor conforme seu botão real!
        driver.findElement(By.cssSelector("a[href='/competicao']")).click();

        // Aguarda a tela da competição (ajuste o seletor/texto conforme seu HTML)
        driver.findElement(By.cssSelector("a[href='/competicao/new']")).click();

        driver.findElement(By.name("cpResposta")).sendKeys("1008");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("440");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("1");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("68");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("9");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("0.8");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("29");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("420");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("-3");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.name("cpResposta")).sendKeys("2.81");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Assertions.assertTrue(driver.getCurrentUrl().contains("/detalhe"), "Usuário deve estar na tela de competição finalizada");
    }

    // 8. Impedimento de nova participação no mesmo dia
    @Test
    @Order(8)
    void impedimentoNovaCompeticaoMesmoDia() {

        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("username")).sendKeys("JAJA5");
        driver.findElement(By.id("password")).sendKeys("jajaja");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Aguarda a home carregar
        WebDriverWait wait = new WebDriverWait(driver, 10);
        // Clica para iniciar nova competição/partida
        // Ajuste o seletor conforme seu botão real!
        driver.findElement(By.cssSelector("a[href='/competicao']")).click();
        // Aguarda a tela da competição (ajuste o seletor/texto conforme seu HTML)
        driver.findElement(By.cssSelector("a[href='/competicao/new']")).click();
        Assertions.assertFalse(driver.getCurrentUrl().contains("/competicao/new"), "Usuário deve estar na tela de competição finalizada");

    }
}