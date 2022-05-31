package com.AT;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

public class SmokeTest {

    private WebDriver driver;

    By usernameXPath = By.xpath("//input[@formcontrolname='username']");
    By emailXPath = By.xpath("//input[@formcontrolname='email']");
    By passwordXPath = By.xpath("//input[@formcontrolname='password']");

    private String email;
    private String username;
    private String password;

    @Before
    public void setUp(){

        System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromeDriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://candidatex:qa-is-cool@qa-task.backbasecloud.com/");
    }
    @Before
    public void register() {
        driver.findElement(By.xpath("//a[@href='/register']")).click();

        Random ran = new Random();
        int x = ran.nextInt(89999) + 10000;
        username = "agileuser" + x;
        email = username + "@mail.com";
        password= "password123";

        driver.findElement(usernameXPath).sendKeys(username);
        driver.findElement(emailXPath).sendKeys(email);
        driver.findElement(passwordXPath).sendKeys(password);
        driver.findElement(By.xpath("//button")).click();

        //logout
        WebDriverWait wait = new WebDriverWait(driver, 8);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul > li:nth-child(4) > a")));
        driver.findElement(By.cssSelector("ul > li:nth-child(4) > a")).click();
        driver.findElement(By.xpath("//a[@href='/settings']")).click();
        driver.findElement(By.cssSelector("div > button")).click();
    }

    @Test
    public void login() {
        driver.findElement(By.xpath("//a[@href='/login']")).click();
        driver.findElement(emailXPath).sendKeys(email);
        driver.findElement(passwordXPath).sendKeys(password);
        driver.findElement(By.xpath("//button")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("app-layout-header > nav > div > ul > li:nth-child(1) > a")).isDisplayed());
    }

    @Test
    public void writeArticle() {
        login();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/editor']")));
        driver.findElement(By.xpath("//a[@href='/editor']")).click();
        driver.findElement(By.xpath("//input[@formcontrolname='title']")).sendKeys("New Blog");
        driver.findElement(By.xpath("//input[@formcontrolname='description']")).sendKeys("Selenium test for BootCamp");
        driver.findElement(By.xpath("//textarea[@formcontrolname='body']")).sendKeys("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sit amet est lorem. Nullam vulputate ante eget odio commodo, eget sollicitudin enim consectetur. Curabitur at vestibulum justo, eget dapibus enim. Praesent convallis leo et finibus porttitor. Ut vestibulum maximus mauris, vel consectetur arcu aliquet ut. Cras ac suscipit velit. Mauris luctus felis nibh, in maximus risus posuere vulputate.");
        driver.findElement(By.xpath("//input[@formcontrolname='title']")).sendKeys("New Blog");
        driver.findElement(By.cssSelector("form > fieldset > fieldset:nth-child(4) > input")).sendKeys("Test");

        driver.findElement(By.xpath("//button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-article-page")));
        Assert.assertTrue(driver.findElement(By.cssSelector("app-article-page > div > div.container.page > div.row.article-content")).isDisplayed());
    }

    @Test
    public void addFavorite() {
        login();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.col-md-9 > div > ul > li:nth-child(2) > a")));
        driver.findElement(By.cssSelector("div.col-md-9 > div > ul > li:nth-child(2) > a")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-sm btn-outline-primary']")));
        driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-primary']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-sm btn-primary']")));
        String text = driver.findElement(By.xpath("//button[@class='btn btn-sm btn-primary']")).getText();
        Assert.assertEquals("1", text);
    }

    @Test
    public void update() {
        login();
        String update = "Updating Info";
        WebDriverWait wait = new WebDriverWait(driver, 8);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/profile/"+username+"']")));
        driver.findElement(By.xpath("//a[@href='/profile/"+username+"']")).click();
        driver.findElement(By.xpath("//a[@href='/settings']")).click();
        driver.findElement(By.cssSelector("app-settings-page > div > div > div > div > form > fieldset > fieldset:nth-child(3) > textarea")).sendKeys(update);
        driver.findElement(By.xpath("//button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-profile-page > div > div.user-info > div > div > div > p")));
        String element = driver.findElement(By.cssSelector(" app-profile-page > div > div.user-info > div > div > div > p")).getText();
        Assert.assertEquals( update, element);
    }

    @Test
    public void loginFailed() {
        driver.findElement(By.xpath("//a[@href='/login']")).click();
        driver.findElement(emailXPath).sendKeys("invalid@mail.com");
        driver.findElement(passwordXPath).sendKeys("pass123");
        driver.findElement(By.xpath("//button")).click();
        driver.findElement(By.cssSelector(" app-auth-page > div > div > div > div > app-list-errors > ul"));
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("app-list-errors > ul")));
        Assert.assertTrue(driver.findElement(By.cssSelector("app-list-errors > ul")).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }

}