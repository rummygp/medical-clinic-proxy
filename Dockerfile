FROM openjdk:21-jdk
COPY target/medical-clinic-proxy-0.0.1-SNAPSHOT.jar medical-clinic-proxy-app.jar
ENTRYPOINT ["java", "-jar", "/medical-clinic-proxy-app.jar"]