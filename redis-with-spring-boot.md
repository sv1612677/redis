
- [Sring boot starter data redis](#sring-boot-starter-data-redis)
  - [Dependency](#dependency)
  - [Jedis and Lettuces](#jedis-and-lettuces)
  - [Config](#config)
  - [Spring boot starter redis cache](#spring-boot-starter-redis-cache)
    - [@Cacheable](#cacheable)
    - [@CacheEvict](#cacheevict)
    - [@CachePut](#cacheput)
    - [Multi config cache @Caching](#multi-config-cache-caching)
  - [Operation on Entity](#operation-on-entity)
  - [Demo Spring boot starter data redis](#demo-spring-boot-starter-data-redis)
- [redisson-spring-boot-starter](#redisson-spring-boot-starter)
  - [Dependency](#dependency-1)
  - [Setting](#setting)
  - [DEMO](#demo)
- [Redis cluster](#redis-cluster)
- [Redis Sentinel](#redis-sentinel)

## Sring boot starter data redis

### Dependency

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
```

nó chứa các dependency con như spring-data-redis, reids-driver là lecctuce và spring-boot-starter

### Jedis and Lettuces

Redis và Lettuce đều là driver để sử dụng redis trong gói  spring-boot-starter-data-redis   .
- Jedis: không đảm bảo thread-safe khi application muốn dùng một instance cho nhiều thread, để đảm bảo thì Jedis tiếp cận với hướng là connection pool, mỗi thead sử  dụng một instance của jedis, làm tăng chi phí kết nối với redis server.
- Còn đối với lettuce thì có thể sử dụng một instance cho môi trường multi thread, do đó chỉ cần một instance lettuce kết nối với redis server ta cũng có thể đảm bảo thread-safe, ta có thể cấu hình việc instance lecttuce có thể tương tác với bao nhiêu client một lúc tối đa. Do đó redis sử dụng lettuce làm default driver để kết nối với redis.

Mặc định trong spring-boot-starter-data-redis sử dụng lecctuce làm driver default, và để cấu hình lecctuce thì ta sử dụng các thuộc tính trong application.properties

```java
spring.redis.lettuce.pool.max-active=7 
spring.redis.lettuce.pool.max-idle=7
spring.redis.lettuce.pool.min-idle=2
spring.redis.lettuce.pool.max-wait=-1ms  
spring.redis.lettuce.shutdown-timeout=200ms
```

để có thể sử dụng jedis ta cấu hình trong dependency nnhuw sau
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
  <exclusions>
    <exclusion>
	 <groupId>io.lettuce</groupId>
	 <artifactId>lettuce-core</artifactId>
    </exclusion>
  </exclusions>		    
</dependency>		
<dependency>
  <groupId>redis.clients</groupId>
  <artifactId>jedis</artifactId>
</dependency>
```

và cấu hình trên properties các thuộc tính của jedis
```java
spring.redis.jedis.pool.max-active=7 
spring.redis.jedis.pool.max-idle=7
spring.redis.jedis.pool.min-idle=2
spring.redis.jedis.pool.max-wait=-1ms 
```

### Config

Trong application-properties thì ta có thể cấu hình redis thông qua `spring.redis.*` để cấu hình port, server, max-connect, time-live,...

```sh
spring.redis.database=0 # Database index used by the connection factory.
spring.redis.host=localhost # Redis server host.
spring.redis.password= # Login password of the redis server.
spring.redis.pool.max-active=8 # Max number of connections that can be allocated by the pool at a given time. Use a negative value for no limit.
spring.redis.pool.max-idle=8 # Max number of "idle" connections in the pool. Use a negative value to indicate an unlimited number of idle connections.
spring.redis.pool.max-wait=-1 # Maximum amount of time (in milliseconds) a connection allocation should block before throwing an exception when the pool is exhausted. Use a negative value to block indefinitely.
spring.redis.pool.min-idle=0 # Target for the minimum number of idle connections to maintain in the pool. This setting only has an effect if it is positive.
spring.redis.port=6379 # Redis server port.
spring.redis.sentinel.master= # Name of Redis server.
spring.redis.sentinel.nodes= # Comma-separated list of host:port pairs.
spring.redis.timeout=0 # Connection timeout in milliseconds. 
```

### Spring boot starter redis cache

Ta sử dụng `@EnableCaching` trong file config hoặc application để có thể sử  dụng redis như bộ nhớ cache bằng cách sử dụng các annotation sau trên các api của controller

#### @Cacheable

```java
    @Cacheable(value = "users", key = "#userId")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID {}.", userId);
        return userService.findUserById(Long.valueOf(userId));
  }
```

khi gọi đên api này, đầu tiên sẽ check xem trong redis đã lưu cache lại chưa, nếu chưa thì nó sẽ gọi method getUser, và sẽ caching lại kết quả đó dưới redis, ở đây là cả Object user. Dưới redis, lưu dưới dạng string, key là tên được quy định trong `value`, còn giá trị là mã byte đã được decode
![x](image/cache_get.png)

#### @CacheEvict

có chức năng remove các entry trong redis thông qua cacheName hoặc value, thường được dùng cho các controller delete. @cacheEvict có thể đươc sử dụng để xóa một, nhiều hay toàn bộ entry.

```java
    @CacheEvict(value = "users", key = "#userId")
    @DeleteMapping("/{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        LOG.info("deleting person with id {}", userId);
        userService.deleteUserById(userId);
    }
```

Ở ví dụ trên, khi gọi api này, redis sẽ xóa dữ liệu trong database và xóa dữ liệu cache ở cả redis nếu đã được cache trước đó key là userId, hoặc có thể  xóa hết entry thông qua attribute khác như ` allEntries =true`.

#### @CachePut

dùng khi dữ liệu được thay đổi, thì cần cập nhật trong entry dưới redis để persist giữ cache và data, khi khai báo nó sẽ tự động cập nhật mà không cần thêm bất kì dòng code nào vào phương thức.

```java
    @CachePut( value = "users", key = "#user.id")
    @PutMapping("/update")
    public User updatePersonByID(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }
```

khi gọi phương thức update user, với key là userId thì khi thực hiện phương thức nó sẽ cập nhật trên cả redis.

#### Multi config cache @Caching

với việc cấu hình multi annotation cache trên các method ta sử dụng @caching, ví dụ cụ thể sau đây
```java

@Caching(evict = { 
  @CacheEvict("addresses"), 
  @CacheEvict(value="directory", key="#customer.name") })
public String getAddress(Customer customer) {...}
```

### Operation on Entity

Ngoài việc thực hiện các việc cache trên thông qua các method trên controller, ta có thể  thực hiện các operation của redis trên các entity thông qua RedisTemplate, hoặc có thể dựa vào operation trên datatype cụ thể như `StringRedisTemplate`, `HashOperations`,...

```java
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        return connectionFactory;
    }
    @Bean
    public RedisTemplate<String, User> redisTemplate() {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
```

Đầu tiên ta khởi tạo redisConnectionFactory, ở đây ta có thể config các cấu hình của mình thông qua constructor của lecttuce, ở đây ta có thê cấu hình port, cluster,...

với redis-template, tùy thuộc vào operation mà ta sử dụng trên java, thì nó sẽ mapping tương đuongư với kiểu dữ liệu trên redis, nhưng ở dưới redis chỉ luuư dưới dạng byte cho cả key và value.

### Demo Spring boot starter data redis

Project tạo ra các api để tìm người dùng, xóa, cập nhật. Thông qua việc gọi trên controller, nhờ sử dụng các annotation cho việc cache thì nó sẽ mapping, lưu cache và cập nhật cache với các api tương ứng được gọi.

Chi tiết nằm trong folder `spring-boot-starter-data-redis` ở tầng controller sử dụng các annotaion như @CahcheEvict, @CachePut, @Cacheable.

Bên cạnh đó còn sử dụng redistemplate để có thể thực hiện các operation với các entity và lưu chúng dưới redis.

Chi tiết nằm trong folder `spring-boot-starter-data-redis` ở tầng dao sử dụng hashOperation để handle giữa entity và data lưu trong redis.

## redisson-spring-boot-starter

### Dependency

Để sử dụng gói redission-spring-boot-starter, thì ta cần thêm dependency như sau
 
```xml
    <dependency>
         <groupId>org.redisson</groupId>
         <artifactId>redisson-spring-boot-starter</artifactId>
         <version>3.11.5</version>
     </dependency>
```
với dependency này, spring boot sẽ tự động cấu hình bean Redission client, nếu muốn sử dụng ta chỉ cần autowire bean đó vào

### Setting 

Có thể cấu hình redission trên file application.properties với các thông tin như host, port, password, ssl, timeout, cluster,...

```java
spring.redis.database=
spring.redis.host=
spring.redis.port=
spring.redis.password=
spring.redis.ssl=
spring.redis.timeout=
spring.redis.cluster.nodes=
spring.redis.sentinel.master=
spring.redis.sentinel.nodes=
```

Mặc định spring boot sẽ cấu hình mặc định là localhost và port là 6379.

### DEMO 

demo về phần này em giải quyết bài tập count ở tuần trước nhưng sử dụng redission client được cung cấp bởi spring, và đã refactor code lại theo chuẩn như anh đã review trước đó, cụ thể nằm trong folder `redission-springboot-starter`.

## Redis cluster

là một data sharding cho việc automatic management, handling failover and replication.

Với redis cluster thì dữ liệu sẽ được chia ra nhiều node, mỗi node sẽ giữ một subset data. slave replicate dữ liệu của mỗi master mà nó phụ thuộc, khi master die thì slave sẽ được tự động chuyển lên thành master mới.

Ta không cần cấu hình việc qủan lí lúc master fail khi sử dụng cluster.

## Redis Sentinel

Redis sentinel cung cấp một giải pháp High Available cho cụm triển khai Redis. Nghĩa là, khi có một hoặc một số Redis instance down, thì cụm Redis của bạn vẫn hoạt động tốt. bằng cách
- chọn một slave đang chaỵ, và chuyển nó lên làm master
- Thay dodoir cấu hình của slave được chọn lên làm master
- Thay đổi laị file cấu hình trên các slave còn lại

Các sentinel liên lạc với nhau, cùng quyết định xem khi nào master là unreachable dựa vào chỉ số `quorum value`, khi chỉ số này lớn hơn quá nữa thì mới có hiệu lực. chỉ số này dùng để phát hiện ra master khi nào fail.

ví dụ mô hình như sau
![x](https://miro.medium.com/proxy/1*7AfXYNub6eC9j21-JZ-f-A.png)

Khi master down thì sentinel ở các slave sẽ xét xem chỉ số `quorum value` nếu có sự đồng ý ở 2 slave thì sẽ lựa một slave lên làm master và reconfig lại các cấu hình ở các slave như thông tin địa chỉ master.