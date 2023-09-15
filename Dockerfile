FROM openjdk:17-alpine AS builder

COPY . /tmp
WORKDIR /tmp

RUN sed -i 's/\r&//' ./gradlew
RUN ./gradlew build

FROM openjdk:17-alpine
COPY --from=builder /tmp/build/libs/*.jar ./

CMD ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
