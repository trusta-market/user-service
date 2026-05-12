package com.trusta_market.userservice.user.application.port.out;

import java.util.Optional;

/**
 * 유저 관련 캐시 작업을 처리하는 아웃바운드 포트 인터페이스.
 * Redis 등의 캐시 저장소 연동을 추상화합니다.
 */
public interface UserCachePort {

    // 캐시 데이터 조회
    <T> Optional<T> get(String key, Class<T> type);

    // 캐시 데이터 저장
    void put(String key, Object value, long ttlSeconds);

    // 캐시 데이터 삭제
    void evict(String key);

    // 접두사를 이용한 캐시 데이터 일괄 삭제
    void evictByPrefix(String prefix);
}
