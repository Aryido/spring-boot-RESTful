FROM openjdk:8-jdk-alpine
COPY ./target/*.jar /workspace/mydocker/demo.jar
EXPOSE 8080
WORKDIR  /workspace/mydocker
ENTRYPOINT ["java","-jar","demo.jar"]