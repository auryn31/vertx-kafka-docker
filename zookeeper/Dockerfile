
FROM ubuntu:19.10

RUN apt-get update
RUN apt -y install wget
RUN apt-get -y install default-jdk
RUN wget https://www-eu.apache.org/dist/kafka/2.2.0/kafka_2.12-2.2.0.tgz
RUN tar -xzf kafka_2.12-2.2.0.tgz
RUN rm kafka_2.12-2.2.0.tgz
WORKDIR /kafka_2.12-2.2.0

CMD [ "bin/zookeeper-server-start.sh", "config/zookeeper.properties" ]