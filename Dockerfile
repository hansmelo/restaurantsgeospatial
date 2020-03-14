FROM openjdk:8-alpine
RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app
COPY target/restaurantsgeospatial-0.0.1-SNAPSHOT.jar $PROJECT_HOME/restaurantsgeospatial-0.0.1-SNAPSHOT.jar
WORKDIR $PROJECT_HOME
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./restaurantsgeospatial-0.0.1-SNAPSHOT.jar"]