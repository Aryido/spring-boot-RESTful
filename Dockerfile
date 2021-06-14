FROM adoptopenjdk/openjdk11:latest
COPY *.jar /workspace/mydocker/demo.jar
WORKDIR  /workspace/mydocker

EXPOSE 8080

ENTRYPOINT ["java","-jar","demo.jar"]