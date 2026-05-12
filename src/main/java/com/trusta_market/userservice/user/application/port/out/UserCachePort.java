package com.trusta_market.userservice.user.application.port.out;

import java.util.Optional;

/**
 * 유저 관련 캐시 작업을 처리하는 아웃바운드 포트 인터페이스.
 * Redis 등의 캐시 저장소 연동을 추상화합니다.
 */
public interface UserCachePort {

    /**
     * 캐시 데이터 조회
     * @param key 조회할 캐시 키. null일 수 없습니다.
     * @param type 조회할 데이터의 타입 클래스.
     * @return 캐시에 데이터가 존재하고 타입이 일치하면 Optional 담아 반환, 그렇지 않으면 Optional.empty() 반환.
     * @param <T> 반환할 데이터의 제네릭 타입.
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * 캐시 데이터 저장
     * @param key 저장할 캐시 키. null일 수 없습니다.
     * @param value 저장할 값. null일 경우 동작은 구현체에 따라 다르나 보통 저장하지 않거나 삭제로 간주함.
     * @param ttlSeconds 데이터의 생존 기간(초). 음수일 경우 즉시 만료되거나 무시될 수 있음.
     */
    void put(String key, Object value, long ttlSeconds);

    /**
     * 캐시 데이터 삭제
     * @param key 삭제할 캐시 키. null일 수 없습니다.
     */
    void evict(String key);

    /**
     * 접두사를 이용한 캐시 데이터 일괄 삭제
     * @param prefix 삭제할 키의 접두사. 정확히 이 접두사로 시작하는 모든 키를 삭제함. null 혹은 빈 문자열일 경우 동작하지 않음.
     */
    void evictByPrefix(String prefix);
}
