FROM openjdk:21-jdk-slim
LABEL authors="zhj"
WORKDIR /app
# Add non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Copy jar 
COPY target/hsbcdemo-0.0.1-SNAPSHOT.jar app.jar
# Create volume for logs
VOLUME /app/logs

# Expose port
EXPOSE 8088
# Run application
ENTRYPOINT ["java","-jar","/app.jar"]