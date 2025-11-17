package com.pingyu.codematebackend.controller;

import com.pingyu.codematebackend.common.BaseResponse; // <-- [重构] 导入 BaseResponse
// import com.pingyu.codematebackend.common.ResultUtils; // <-- [重构] 删除 ResultUtils
import com.pingyu.codematebackend.service.CrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【“案子 15.1” - 4/4：“爬虫”指挥官】
 * [已重构为策略 B]
 */
@RestController
@RequestMapping("/crawl")
@Tag(name = "3. “爬虫”管理接口 (CrawlerController)")
public class CrawlerController {

    @Resource
    private CrawlerService crawlerService;

    /**
     * “触发”抓取“豆瓣读书 Top 250”
     */
    @GetMapping("/douban-top250")
    @Operation(summary = "抓取豆瓣读书 Top 250", description = "（“新手”案）演示 Jsoup 抓取“静态”网页")
    public BaseResponse<List<String>> crawlDouban() {

        // 1. “指挥官”调用“实干家”
        List<String> titles = crawlerService.crawlDoubanBookTitles();

        // 2. “指挥官”返回“战利品”
        // [重构] 使用 BaseResponse.success()
        return BaseResponse.success(titles);
    }

    /**
     * “触发”抓取“我们自己”的“动态”前端
     */
    @GetMapping("/crawl-dynamic")
    @Operation(summary = "抓取“动态”伙伴匹配页", description = "（“困难”案）演示 Selenium 抓取“动态” (Vue) 网页")
    public BaseResponse<List<String>> crawlDynamic() {

        // 1. “指挥官”调用“实干家”
        List<String> usernames = crawlerService.crawlDynamicMatchPage();

        // 2. “指挥官”返回“战利品”
        // [重构] 使用 BaseResponse.success()
        return BaseResponse.success(usernames);
    }
}