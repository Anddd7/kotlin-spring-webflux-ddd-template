FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAVA_OPTS
ARG IMAGE_VERSION

ENV JAVA_OPTS=$JAVA_OPTS
ENV IMAGE_VERSION=$IMAGE_VERSION

COPY ./config/docker/entrypoint.sh /usr/local/bin/

COPY ./config/application-dev.yml application-deploy.yml
COPY ./build/libs/kotlin-spring-webflux-ddd-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["entrypoint.sh"]
