FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD=123456 
ADD start-mysql.sql /docker-entrypoint-initdb.d
EXPOSE 3306