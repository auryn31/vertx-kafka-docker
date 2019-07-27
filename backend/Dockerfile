
FROM ubuntu:19.10

RUN apt-get update
RUN apt -y install wget
RUN apt-get -y install default-jdk
RUN apt-get -y install maven
COPY src /server/src
COPY pom.xml /server/pom.xml
COPY .mvn /server/.mvn
COPY mvnw /server/mvnw
COPY mvnw.cmd /server/mvnw.cmd
COPY vertx.iml /server/vertx.iml
RUN mvn install -f /server/pom.xml
WORKDIR /server

CMD java -jar target/vertx-0.0.1-fat.jar