FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]