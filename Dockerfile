# Sử dụng image Tomcat 10 với JDK 17
FROM tomcat:10-jdk17

# Đặt thư mục làm việc trong container
WORKDIR /usr/local/tomcat/webapps/

# Copy file .war vào thư mục webapps của Tomcat
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose cổng 8080 để chạy ứng dụng
EXPOSE 8080

# Chạy Tomcat khi container khởi động
CMD ["catalina.sh", "run"]