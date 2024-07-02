## REST Proxy Stub

### Описание
swagger-ui: http://path-to-stub/swagger-ui.html  

### Пример
См. postman collection в resources/postman  
(для данных из "Set correct data list" при каждом последующем запросе "GET data" будет возвращаться следующий элемент
массива "responses")

### application.yaml
- zuul.routes - маршруты, по которым будет проксирование к другим заглушкам, если не установлены свои данные для ответа*
- prefix.teams - префиксы команд, которые дают возможность для разных сервисов, работающих с одними и теми же
эндпоинтами, устанавливать в заглушку различные данные независимо друг от друга*

### запуск сервиса в docker локально
- mvn clean install
- docker build -t rest-proxy-stub .
- docker compose up
- Your program changed? Rebuild the image!: docker rmi rest-proxy-stub, docker build -t rest-proxy-stub .
- Invoke your program inside a container: docker run --name rest-proxy-stub -p 24224:8888 rest-proxy-stub

### docker-compose
- что бы создать локально БД нужно открыть терминал в root папке этого проекта и ввести команду: docker compose up

### запуск perfomance тестов
- $ disable maven profile application, enable benchmark
- $ mvn clean install -DskipTests
- $ в корневой папке проекта: C:\Users\user_folder\.jdks\azul-21.0.1\bin\java.exe -XX:+UseZGC -jar target/benchmarks.jar

