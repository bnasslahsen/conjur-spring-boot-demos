spring:
  datasource:
    url: {{ secret "url" }}
    username: {{ secret "username" }}
    password: {{ secret "password" }}