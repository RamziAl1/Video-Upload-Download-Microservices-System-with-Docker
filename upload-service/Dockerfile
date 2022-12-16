FROM tomcat:9.0.8-jre8-alpine
RUN rm -rvf /usr/local/tomcat/webapps/ROOT
ADD  /target/upload-service.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
EXPOSE 8080


