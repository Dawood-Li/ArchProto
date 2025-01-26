# spring boot app dev env based on Alpine
FROM m.daocloud.io/docker.io/library/alpine:latest

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
    && apk add --no-cache openjdk21-jdk maven

WORKDIR /app

COPY settings-docker.xml /root/.m2/settings.xml

VOLUME [ \
    "/root/.m2", \
    "/app/pom.xml", \
    "/app/src" \
]

CMD ["mvn", "-s", "/usr/share/maven/ref/settings-docker.xml", "spring-boot:run"]
