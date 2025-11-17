package com.pingyu.codematebackend.job;

import com.pingyu.codematebackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
// 【【 1. 导入“Redisson 工具” 】】
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit; // 【【 2. 导入“时间单位” 】】

/**
 * 缓存预热“定时闹钟”
 * [已重构为使用 Redisson 分布式锁]
 */
@Component
@Slf4j
public class PreheatCacheJob {

    @Resource
    private UserService userService;

    // 【【 3. 注入“Redisson 客户端” 】】
    @Resource
    private RedissonClient redissonClient;

    private final List<String> hotTags = Arrays.asList("Java", "Python", "Go", "Vue", "React");

    /**
     * 【实战】“缓存预热”任务
     */
    @Scheduled(cron = "0 0 4 * * *") // 每天凌晨4点
    public void doPreheatCache() {

        // 【【 4. 定义“锁”的“全局唯一”名称 】】
        String lockKey = "codemate:preheat:cache:lock";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 【【 5. 尝试“抢占”锁 】】
            // (等待时间=0, 自动释放时间=10秒, 单位=秒)
            // (Redisson 内部会自动“看门狗”续期，10秒只是一个兜底)
            boolean gotLock = lock.tryLock(0, 10, TimeUnit.SECONDS);

            // 【【 6. 检查“笔误” (getLock -> gotLock) 】】
            if (gotLock) {
                log.info("【锁】抢占成功！(线程: {}) 开始“缓存预热”...", Thread.currentThread().getId());

                // --- 核心业务逻辑 ---
                for (String tagName : hotTags) {
                    log.info("【缓存预热】正在预热标签: {}", tagName);
                    userService.searchUsersByTags(Arrays.asList(tagName));
                }
                log.info("【定时任务】完成：“缓存预热”结束。");
                // --- 业务逻辑结束 ---

            } else {
                // (抢锁失败，说明别的服务器正在干活)
                log.info("【锁】抢占失败。(线程: {})，其他服务器正在执行。", Thread.currentThread().getId());
            }

        } catch (InterruptedException e) {
            log.error("【锁】Redisson 定时任务被中断", e);
            // (可选) 重新设置中断状态
            Thread.currentThread().interrupt();
        } finally {
            // 【【 7.（关键）安全“归还”锁 】】
            // 只有当前线程“持有”这个锁时，才去“解锁”
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("【锁】已归还。(线程: {})", Thread.currentThread().getId());
            }
        }
    }
}