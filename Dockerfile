### build stage ###
FROM openjdk:17-alpine AS builder

# set arg
ARG WORKSPACE=/home/spring-docker
ARG BUILD_TARGET=${WORKSPACE}/build/libs
WORKDIR ${WORKSPACE}

# copy code & build
COPY . .
RUN ./gradlew clean bootJar

# unpack jar
WORKDIR ${BUILD_TARGET}
RUN jar -xf *.jar


### create image stage ###
FROM openjdk:17-alpine

ARG WORKSPACE=/home/spring-docker
ARG BUILD_TARGET=${WORKSPACE}/build/libs
ARG DEPLOY_PATH=${WORKSPACE}/deploy

# copy from build stage
COPY --from=builder ${BUILD_TARGET}/org ${DEPLOY_PATH}/org
COPY --from=builder ${BUILD_TARGET}/BOOT-INF/lib ${DEPLOY_PATH}/BOOT-INF/lib
COPY --from=builder ${BUILD_TARGET}/META-INF ${DEPLOY_PATH}/META-INF
COPY --from=builder ${BUILD_TARGET}/BOOT-INF/classes ${DEPLOY_PATH}/BOOT-INF/classes

WORKDIR ${DEPLOY_PATH}
ENV spring.profiles.active local

EXPOSE 8080/tcp
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
