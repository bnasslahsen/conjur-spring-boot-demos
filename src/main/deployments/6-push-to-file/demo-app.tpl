spring:
  datasource:
    url: {{ secret "url" }}
    username: {{ secret "username" }}
    password: {{ secret "password" }}
  jpa:
    hibernate:
      ddl-auto: update   
 
management:
  endpoints:
    web:
      exposure:
        include: health, refresh

springdoc:
  writer-with-order-by-keys: true
  swagger-ui:
    use-root-path: true
    
logging:
  level:
    com.cyberark: DEBUG