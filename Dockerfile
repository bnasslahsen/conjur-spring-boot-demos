FROM eclipse-temurin:17-jre-alpine
COPY target/conjur-spring-boot-demos-1.0.jar /app/target/conjur-spring-boot-demos-1.0.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/target/conjur-spring-boot-demos-1.0.jar" ]