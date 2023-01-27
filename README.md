# SpringBootRedis

## Redis DB를 이용하여 SpringBoot Session Cluster 하는 샘플

### Features

* SpringBoot App 이 Redis에 세션을 저장하는 형태
* App1, App2 간 세션클러스터링

### Skills

* SpringBoot
* Redis (Docker로 구현)
* Gradle

### 사전 요구사항

* Redis 1개 구축

-----------

## SpringBoot App 설정

#### build.gradle 에 dependencies 추가

```groovy
dependencies {
    // Redis Session
    implementation 'org.springframework.session:spring-session-data-redis'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```

#### application.yml 수정

```yaml
spring:
  session:
    store-type: redis
  redis:
    host: localhost
    port: 32768
    password: redispw
```

* spring.session.store-type 을 redis 방식으로 지정
* spring.redis 접속 정보를 입력

#### Java Config 파일 생성

```java

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        configuration.setPassword(redisPassword);

        return new LettuceConnectionFactory(configuration);
    }

}
```

------------

## Session Cluster 확인

#### 동일한 도메인 (http://localhost) 으로 접속 후 세션이 공유가 되는지 확인한다.

샘플 모듈 (App1, App2) 를 가동
> `App1 접속` http://localhost:18080
>
> `App2 접속` http://localhost:28080

### 결과

| 도메인                  | 세션 값                                                                                             |
|------------------------|----------------------------------------------------------------------------------------------------|
| http://localhost:18080 | {"UUID":"66c2286d-a4b6-49ae-9f1a-bb2fcd947833","SessionID":"62665a63-f78f-4a31-91de-54b22dd1d90c"} |
| http://localhost:28080 | {"UUID":"66c2286d-a4b6-49ae-9f1a-bb2fcd947833","SessionID":"62665a63-f78f-4a31-91de-54b22dd1d90c"} |

* 브라우저에 출력되는 값이 동일함

----------

## 참고. 세션 저장 정보 확인

Docker로 가동된 Redis 쉘 접속하여 Redis DB에 값 확인

```shell
$ redis-cli
127.0.0.1:6379> auth 패스워드
127.0.0.1:6379> keys *
1) "spring:session:expirations:1674711900000"
2) "spring:session:sessions:expires:62665a63-f78f-4a31-91de-54b22dd1d90c"
3) "spring:session:sessions:62665a63-f78f-4a31-91de-54b22dd1d90c"
```

* spring:session:expirations:(expire time) - expire time에 삭제될 세션 정보(Set 타입)
* spring:session:sessions:expires:(session id) - 해당 세션의 만료 키(String 타입)
* spring:session:sessions:(session id) - 세션 생성 시간, 마지막 세션 조회 시간, 최대 타임아웃, 해당 세션에 저장한 데이터(Hash 타입)