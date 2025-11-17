package com.pingyu.codematebackend.service.impl;

import com.pingyu.codematebackend.service.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver; // <-- 我们只需要这个
import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.remote.RemoteWebDriver; // <-- 【已移除】不再需要
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
// import java.net.MalformedURLException; // <-- 【已移除】不再需要
// import java.net.URL; // <-- 【已移除】不再需要
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 【“案子 15.2” - “分页”修复·蓝图】
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    // “受害者”的“基础”地址 (不带 ?start=)
    private static final String DOUBAN_BASE_URL = "https://book.douban.com/top250";
    // “每页”的“战利品”数量
    private static final int ITEMS_PER_PAGE = 25;
    // “总共”的“页数”
    private static final int TOTAL_PAGES = 10; // (250 / 25 = 10)

    @Override
    public List<String> crawlDoubanBookTitles() {
        System.out.println("【案子 15.2】““分页”抓取”行动开始！目标：" + DOUBAN_BASE_URL);

        // (用于“存放”“所有”页的“战利品”)
        List<String> allTitles = new ArrayList<>();

        // 【【【“案子 15.2” - 核心修复：“循环” 10 次！】】】
        for (int i = 0; i < TOTAL_PAGES; i++) {

            // 1. “计算”“分页”参数
            int start = i * ITEMS_PER_PAGE; // (i=0 -> 0; i=1 -> 25; i=2 -> 50...)

            // 2. “组装”“当页”的“目标” URL
            String pageUrl = DOUBAN_BASE_URL + "?start=" + start;
            System.out.println("【案子 15.2】正在“搜查”第 " + (i + 1) + " 页: " + pageUrl);

            try {
                // 3. 【核心 1/3：连接 (Connect) ！】
                Document doc = Jsoup.connect(pageUrl)
                        .timeout(10000) // (网速慢，超时时间设“10 秒”)
                        // 【【【“反爬”策略 1/2：“伪装”！】】】
                        // (告诉“豆瓣”，我“不是” Java，我“是”一个“Windows 上的 Chrome 浏览器”)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .get();

                // 4. 【核心 2/3：筛选 (Select) ！】
                Elements titleLinks = doc.select("div.pl2 > a");

                // 5. 【核心 3/3：提取 (Extract) ！】
                for (Element link : titleLinks) {
                    String title = link.attr("title");
                    if (title != null && !title.isEmpty()) {
                        System.out.println("  【案子 15.2】“战利品” +1: " + title);
                        allTitles.add(title); // (“添加”到“总”列表中)
                    }
                }

                // 【【【“反爬”策略 2/2：“礼貌”！】】】
                // (我们““必须””在“搜查”完“一个”房间后，“休息”一下)
                // (防止““过度”抓取”被“豆瓣”的“安保”系统“封 IP”！)
                Thread.sleep(1000 + (long)(Math.random() * 1000)); // (休息 1-2 秒)

            } catch (IOException e) {
                // “案发” (e.g., “网络超时”)
                e.printStackTrace();
                System.err.println("【案子 15.2】“搜查”第 " + (i + 1) + " 页“失败”！" + e.getMessage());
                // (即使“失败”，我们也“继续”搜查“下一页”)
                continue;
            } catch (InterruptedException e) {
                // “案发” (e.g., “休息”被打断)
                e.printStackTrace();
                Thread.currentThread().interrupt(); // (“礼貌地”“重置”“打断”状态)
            }
        }

        System.out.println("【案子 15.2】“搜查”完毕！共“缴获”战利品: " + allTitles.size() + " 件！");
        return allTitles;
    }
    /**
     * 【【【“案子 15.3” - “死锁”排查·第二振 ！】】】
     * (我们“放弃”爬取“自己”的前端)
     * (我们“测试”爬取一个“外部”网站，来验证 Selenium 本身是否工作)
     */
    @Override
    public List<String> crawlDynamicMatchPage() {
        System.out.println("【案子 15.3 - 第二振】“对照实验”开始！目标：Baidu.com");

        // 【1. “配置“狙击枪””】
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        WebDriver driver = null;
        List<String> results = new ArrayList<>(); // (我们只抓一个元素来测试)

        try {
            // 【2. “自动”模式启动】
            driver = new ChromeDriver(options);

            // “受害者”地址 (【【【关键修改】】】)
            String pageUrl = "https://www.baidu.com"; // <-- 改为“百度”
            driver.get(pageUrl);

            // 【3. ““等待”百度“渲染”！”】
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // (等待““百度一下””按钮出现，它的 ID 是 "su")
            By targetElement = By.id("su"); // <-- 改为百度的元素 ID
            wait.until(ExpectedConditions.presenceOfElementLocated(targetElement));

            System.out.println("【案子 15.3 - 第二振】“外部”网站元素“渲染”完毕！开始“提取”...");

            // 【4. “提取“最终”战利品”】
            WebElement element = driver.findElement(targetElement);
            String buttonText = element.getAttribute("value"); // (获取按钮上的文字)

            System.out.println("  【案子 15.3 - 第二振】“战利品” +1: " + buttonText);
            results.add(buttonText); // (应该会添加 "百度一下")

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("【案子 15.3 - 第二振】“对照实验”“失败”！" + e.getMessage());
        } finally {
            // 【5. ““清理”现场”】
            if (driver != null) {
                driver.quit();
            }
        }

        System.out.println("【案子 15.3 - 第二振】“搜查”完毕！共“缴获”战利品: " + results.size() + " 件！");
        return results;
    }
}