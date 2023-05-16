package com.fggang.pic;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class GeneratePic {
    private ChromeOptions options = new ChromeOptions();
    private WebDriver driver = null;

    private int i = 0;

    public GeneratePic() {
        // 设置 Chromium 的可执行文件路径
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\Desktop\\Test\\chromedriver.exe");

        // 创建一个 ChromeOptions 对象，启用 headless 模式和禁用 GPU

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        // 创建一个 ChromeDriver 对象
        driver = new ChromeDriver(options);
    }

    public BufferedImage getHtmltoPng(BufferedImage captureScreen) throws Exception {
        //更新HTML页面
        UpdataHTML updateHTML = new UpdataHTML();
        updateHTML.updateMarket("", "");

        // 加载 HTML 页面
        driver.get("file:///C:/Users/Administrator/Desktop/TestOcr/pages.html");

        // 截取页面截图，并保存到指定文件
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(screenshotFile);

        if (++i % 100 == 0) {
            // 关闭浏览器驱动程序
            driver.quit();

            // 设置 Chromium 的可执行文件路径
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrator\\Desktop\\Test\\chromedriver.exe");

            // 创建一个 ChromeOptions 对象，启用 headless 模式和禁用 GPU

            options.addArguments("--headless");
            options.addArguments("--disable-gpu");

            // 创建一个 ChromeDriver 对象
            driver = new ChromeDriver(options);

        }
        return image;

    }

}
