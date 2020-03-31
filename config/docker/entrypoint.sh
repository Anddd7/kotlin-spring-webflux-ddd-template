#!/bin/sh

# export environment variables
# export xxx_xxx

# export spring profiles
export SPRING_PROFILES_ACTIVE=deploy,${APP_ENV}

# startup application
java ${JAVA_OPTS} -jar app.jar

exec "$@"
