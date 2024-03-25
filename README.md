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
- Build your image: docker build -t rest-proxy-stub .
- Invoke your program inside a container: docker run --name rest-proxy-stub -p 24224:8888 rest-proxy-stub
- Your program changed? Rebuild the image!: docker rmi rest-proxy-stub, docker build -t rest-proxy-stub .

[*] Значения для стрима Онбординг. Заменить на свои