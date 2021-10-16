package com.onemsg.sunday.service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class VerificationCode {
    
    public final int id;
    public final String user;  
    public final String service;
    public final int data;
    public final long timestramp = System.currentTimeMillis();
    public final long duration;

    /** 验证码默认过期时间，60_000毫秒  */ 
    public static final long DEFAULT_DURATION = 60_000L;
    public static final BiFunction<String,String,Integer> ID_GENERATOR = Objects::hash;

    private VerificationCode(String user, String service, int data, long duration){
        this.user = user;
        this.service = service;
        this.data = data;
        this.duration = duration;
        id = ID_GENERATOR.apply(user, service);
    }

    private static Cache<Integer, VerificationCode> store = Caffeine.newBuilder()
        .maximumSize(100_000)
        .expireAfterWrite(Duration.ofSeconds(600))
        .build();
    
    public static VerificationCode create(String user, String service, long duration){
        Objects.requireNonNull(user);
        Objects.requireNonNull(service);

        int data = generateCode();
        VerificationCode code = new VerificationCode(user, service, data, duration);
        store.put(code.id, code);
        return code;
    }

    public static VerificationCode create(String user, String service) {
        return create(user, service, DEFAULT_DURATION);
    }

    
    public static VerificationCode get(String user, String service){
        Objects.requireNonNull(user);
        Objects.requireNonNull(service);

        return store.getIfPresent(ID_GENERATOR.apply(user, service));
    }


    public static VerifiedState verify(String user, String service, int code){
        Objects.requireNonNull(user);
        Objects.requireNonNull(service);

        int id = ID_GENERATOR.apply(user, service);
        var vcode = store.getIfPresent(id);
        
        if( vcode == null) return VerifiedState.NOT_FOUND;

        if( System.currentTimeMillis() > vcode.timestramp + vcode.duration)
            return VerifiedState.EXPIRED;
        
        return vcode.data == code ? VerifiedState.SUCCESS : VerifiedState.FAIL;
    }


    static final int generateCode(){
        return ThreadLocalRandom.current().nextInt(100_000, 999_999);
    }


    public enum VerifiedState {
        /** 验证正确 */
        SUCCESS,
        /** 验证错误 */
        FAIL,
        /** 验证码过期 */
        EXPIRED,
        /** 验证码不存在 */
        NOT_FOUND
    }
}
