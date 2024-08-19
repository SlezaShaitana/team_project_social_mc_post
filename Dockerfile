FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из предыдущего этапа
COPY target/mc-post.jar myapp.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8091

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "myapp.jar"]