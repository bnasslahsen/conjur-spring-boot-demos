FROM eclipse-temurin:17-jre-alpine
COPY target/conjur-spring-boot-demos-1.0.jar /app/conjur-spring-boot-demos-1.0.jar

COPY src/main/conjur/summon/summon /usr/local/bin
COPY src/main/conjur/summon/summon-conjur /usr/local/lib/summon/

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/conjur-spring-boot-demos-1.0.jar" ]