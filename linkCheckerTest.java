package link_Checker;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LinkCheckerTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://google.com"); // Change this to your target website
    }

    @Test
    public void checkBrokenLinks() {
        List<WebElement> links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && !url.isEmpty()) {
                checkLink(url);
            }
        }
    }

    public void checkLink(String linkUrl) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(linkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode >= 400) {
                System.out.println("❌ Broken Link: " + linkUrl);
            } else {
                System.out.println("✅ Working Link: " + linkUrl);
            }
        } catch (IOException e) {
            System.out.println("❌ Error checking link: " + linkUrl);
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
