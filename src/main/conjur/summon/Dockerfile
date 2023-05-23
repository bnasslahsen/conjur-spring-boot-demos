FROM bnasslahsen/conjur-spring-boot-demos

COPY src/main/conjur/summon/summon /usr/local/bin/summon
COPY src/main/conjur/summon/summon-conjur /usr/local/lib/summon/

EXPOSE 8080
ENTRYPOINT ["summon", "--provider", "summon-conjur" ,"-f", "/etc/summon/secrets.yml", "/cnb/process/web" ]
