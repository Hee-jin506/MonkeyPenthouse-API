package com.monkeypenthouse.core.component;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OrderIdGenerator {

    private final CacheManager cacheManager;

    // 레디스에 OrderIdSerialNum 값 싱크
    @PostConstruct
    @Transactional(readOnly = true)
    private void loadOrderIdSerialNumOnRedis() {
        if (cacheManager.getOrderIdSerialNumFromCache().isEmpty()) {
            cacheManager.setOrderIdSerialNum(cacheManager.getOrderIdSerialNum());
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void refreshIdSerialNumTo1() {
        cacheManager.setOrderIdSerialNum(1L);
    }

    // Thread-Safe Method
    public String generate() {

        CacheManager.LockWithTimeOut lockWithTimeOut = cacheManager.tryLockOnOrderIdSerialNum(2, 1);

        Long value = cacheManager.getOrderIdSerialNum();

        cacheManager.setOrderIdSerialNum(value);

        cacheManager.unlock(lockWithTimeOut.getRLock());

        return LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyMMdd")) +
                String.format("%06d", value
                );
    }




}
