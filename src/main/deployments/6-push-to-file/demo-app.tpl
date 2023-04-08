spring:
  datasource:
    url: {{ secret "url" }}
    username: {{ secret "username" }}
    password: {{ secret "password" }}
    
 
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
    org.cyberark: DEBUG