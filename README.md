## REST Proxy Stub

### Описание

swagger-ui: http://path-to-stub/swagger-ui.html  
confluence: https://wiki.corp.dev.vtb/display/RKOSMB/REST-proxy-stub

### Пример

См. postman collection в resources/postman  
(для данных из "Set correct data list" при каждом последующем запросе "GET data" будет возвращаться следующий элемент
массива "responses")

### application.yaml

- zuul.routes - маршруты, по которым будет проксирование к другим заглушкам, если не установлены свои данные для ответа*
- prefix.teams - префиксы команд, которые дают возможность для разных сервисов, работающих с одними и теми же
  эндпоинтами, устанавливать в заглушку различные данные независимо друг от друга*

### запуск в docker локально
- mvn clean install
- docker compose up -d

### иногда докер поднимает старый image, по этому нужно удалить image вручную
- docker rmi rest-proxy-stub

### удаление docker containers локально
- docker compose down -v

### docker-compose
- что бы создаль локально БД нужно открыть терминал в root папке этого проекта и ввести команду: docker compose up

### запуск perfomance тестов
- $ mvn clean install -DskipTests
- $ java -jar target/benchmarks.jar
или:
- $ java -Djmh.perfasm.saveLog=true -jar rest-proxy-stub-1.0.jar ".*Benchmark.*" -wi 3 -i 3 -r 1 -f 1 -prof perfasm

### запуск тестов производительности с помощью maven
- mvn jmh:benchmark -Djmh.benchmarks=DaoBenchmark -Djmh.wi=3 -Djmh.i=3 -Djmh.r=1 -Djmh.f=1 -Djmh.perfasm.saveLog=true

[*] Значения для стрима Онбординг. Заменить на свои