FROM adoptopenjdk/openjdk11:alpine-jre
RUN apk --no-cache add curl
WORKDIR /app
COPY target/rectangle-problem-0.0.1-SNAPSHOT.jar /app/
CMD java -jar rectangle-problem-0.0.1-SNAPSHOT.jar