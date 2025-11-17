package com.pingyu.codematebackend.service;

import java.util.List;

public interface CrawlerService {

    /**
     * 【“案子 15.1” - 已侦破】
     * 抓取“豆瓣读书 Top 250”的标题
     * @return 书名列表
     */
    List<String> crawlDoubanBookTitles();

    /**
     * 【【【“案子 15.3” - 新增！】】】
     * 抓取“我们自己”的“动态”伙伴匹配页
     * @return 页面上“真实渲染”出的“用户名”列表
     */
    List<String> crawlDynamicMatchPage();
}