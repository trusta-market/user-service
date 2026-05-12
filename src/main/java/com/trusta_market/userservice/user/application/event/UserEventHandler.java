package com.trusta_market.userservice.user.application.event;

import com.trusta_market.userservice.user.domain.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.scheduling.annotation.Async;

// 유저 도메인 이벤트 핸들러 (유저 생성 후 지갑 생성 등 비동기/분리된 처리 담당)
@Slf4j
@Component
public class UserEventHandler {

    // 추후 이벤트 기반 비동기 지갑 생성 처리를 위해 남겨둠 (현 PR에서는 동기 통신 제거)
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handling UserCreatedEvent for user: {}. Wallet creation should be handled via async message broker.", event.userId().value());
    }
}
