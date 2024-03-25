FROM openjdk:11.0.12-jre
COPY target/rest-proxy-stub-1.0-SNAPSHOT.jar /usr/local/service/
ENTRYPOINT ["java", "-jar", "-Dliquibase.hub.mode=off", "/usr/local/service/rest-proxy-stub-1.0-SNAPSHOT.jar"]
#dev2
