FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

# Копируем файлы с исходным кодом
COPY pom.xml .
COPY src ./src

# Собираем приложение с помощью Maven
RUN mvn clean package -DskipTests

# Второй этап сборки для создания финального образа
FROM openjdk:17-alpine
WORKDIR /app

# Копируем собранные файлы из предыдущего этапа
COPY --from=build /app/target/*.jar ./app.jar

# Запускаем приложение
CMD java -jar app.jar