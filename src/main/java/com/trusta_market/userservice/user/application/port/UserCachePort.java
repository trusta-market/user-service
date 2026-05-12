package com.trusta_market.userservice.user.application.port;

import java.util.Optional;

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
