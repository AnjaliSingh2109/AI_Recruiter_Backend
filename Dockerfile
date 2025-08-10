# FROM eclipse-temurin:17-jdk
#
# WORKDIR /app
#
# COPY . .
#
# RUN ./mvnw clean package -DskipTests
#
# CMD ["java", "-jar", "target/ai-recruiter-0.0.1-SNAPSHOT.jar"]

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/ai-recruiter-0.0.1-SNAPSHOT.jar"]
