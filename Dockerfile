FROM openjdk:11-jre-slim

ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ARG IMAGE_VERSION
ENV IMAGE_VERSION=$IMAGE_VERSION

COPY ./build/libs/kotlin-spring-webflux-*.jar app.jar
COPY ./config/application-dev.yml application-deploy.yml

EXPOSE 8080

CMD ["java","-Dimage.version=${IMAGE_VERSION}", "-jar", "app.jar", "--spring.profiles.active=deploy"]
