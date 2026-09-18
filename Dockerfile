# Hospital Management System — Java 17, no external dependencies
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY backend/ backend/
COPY frontend/ frontend/
COPY database/ database/
RUN mkdir -p out && javac -d out $(find backend/src/main/java frontend/src/main/java -name "*.java")
CMD ["java", "-cp", "out", "com.hospital.Main"]
