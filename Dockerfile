FROM openjdk:8-jdk-alpine

RUN apk update && apk add curl tzdata

RUN mkdir -p -m 0775 /apps/jars
RUN mkdir -p -m 0775 /apps/docker/logs/archive
RUN mkdir -p -m 0775 /apps/docker/ssl

WORKDIR /apps/jars

ADD ./build/libs/case-study*.jar /apps/jars/case-study.jar

CMD java -jar -Xmx1g -Dserver.port=8080 /apps/jars/case-study.jar
EXPOSE 8080
