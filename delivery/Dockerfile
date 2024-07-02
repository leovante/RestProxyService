FROM onb-docker.nexus-ci.corp.dev.vtb/ubi8/openjdk-11:1.3-6
COPY target/rest-proxy-stub-*.jar /opt/app/app.jar
USER root
EXPOSE 8080/tcp
CMD ["java", "-jar", "/opt/app/app.jar"]