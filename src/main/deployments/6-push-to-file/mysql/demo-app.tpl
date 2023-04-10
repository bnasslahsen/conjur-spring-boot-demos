spring:
  datasource:
    url: {{ secret "mysql-url" }}
    username: {{ secret "mysql-username" }}
    password: {{ secret "mysql-password" }}
  jpa:
    hibernate:
      ddl-auto: update  
 
management:
  endpoints:
    web:
      exposure:
        include: health, refresh

springdoc:
  swagger-ui:
    use-root-path: true
    display-request-duration: true
    tags-sorter: alpha
    
logging:
  level:
    com.cyberark: DEBUG
    org.cyberark: DEBUG