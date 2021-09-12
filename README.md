## REST Proxy Stub

### Описание [Не полное. Будет дополнено]
Заглушка позволяет:
- устанавливать ключам вида `endpoint:method` значения, содержащие `status(int < 400, не обязательное*)`,
`body(Object, не обязательное)`- которые далее будут возвращаться в ответ на REST-запрос к этому _endpoint_ с данным _method_;
- устанавливать ключам вида `endpoint:method` значения, содержащие `status(int >= 400, не обязательное**)`,
`message(String, не обязательное***)`- которые в ответ на REST-запрос к этому _endpoint_ с данным _method_ позволят
получить сообщение об ошибке с текстом, переданным в `message`;
- проксировать запросы на `URL:path`, статически определённые в `application.yml(zuul.routes)****`;
- динамически устанавливать `URL:path` для `zuul.routes`;
- динамически удалять ключи и маршруты проксирования;
- добавлять различные `prefix` к `endpoint` для разных автотестов, получая уникальные ключи для своих ответов и не мешая
друг другу, но сохраняя возможность проксирования на одни и те же `URL:path` (реальные сервисы или другие заглушки)

*Статусы ответов по-умолчанию: POST - 201, PUT/PATCH - 204, остальные - 200  
**Статус сообщения об ошибке по-умолчанию - 500  
***Текст сообщения об ошибке по-умолчанию устанавливается в `application.yml (response.error.message)`  
****Приоритет срабатывания ключей/маршрутов: сообщение об ошибке, корректный ответ, проксирование

### Пример

    Команда 1 разрабатывает микросервис - mcs1.  
    Команда 2 разрабатывает микросервис - mcs2.  
    Командам нужно проверить поведение микросервисов на эталонных ответах от endpoint:
    `/path/example` на заглушке, расположенной по адресу: `http://real-stub-address`  
    А также - каждой команде пройти свои уникальные тест-кейсы, подкладывая статусы и ответы в данную заглушку.  
    Например, команде 1 нужно проверить ответ на GET-запрос со статусом 200 (также возможен 203) и телом `{"id": 1}`.  
    Проверить ошибку - 401.

Определяем командам prefix - например, "team1" и "team2".  
При деплое mcs1 указываем путь к этой заглушке: `http://localhost:8080/team1`  
При деплое mcs2 указываем путь к этой заглушке: `http://localhost:8080/team2`*  
_*Заменить http://localhost:8080 на адрес заглушки в Open Shift_.  

В автотестах:
- `curl -v -X POST "http://localhost:8080/stub/proxy?id=team1-example&path=/team1/path/example/**&url=http://real-stub-address/path/example"`
устанавливаем маршрут для проксирования (/stub/proxy) запросов к заглушке по адресу _http://real-stub-address_, где:
    - `id=team1-example` - id правила. Должно быть уникальным. Поэтому начинается с prefix команды;
    - `path=/team1/path/example/**` - путь для проксирования, включая prefix;
    - `url=http://real-stub-address/path/example` - URL для проксирования, без prefix;
    - далее - можем проверять поведение mcs1 на реальной заглушке _http://real-stub-address/path/example_;
    - аналогично - для команды 2, меняя prefix на "team2".
- `curl -v "http://localhost:8080/stub/proxy?id=team1-example"` - вернет ZuulRoute с информацией
об установленном маршруте для _id=team1-example_
- `curl -v -X DELETE "http://localhost:8080/stub/proxy?id=team1-example"` - удалит маршрут для _id=team1-example_

Далее, на примере команды 1, проверяем работу микросервиса на других ответах, подкладывая их в данную заглушку:
- `curl -v -X POST "http://localhost:8080/stub/data?key=/team1/path/example:GET" -H "Content-Type: application/json" -d "{\"id\": 1}"` -
устанавливаем корректный ответ (/stub/data) с body _{"id": 1}_ и статусом по-умолчанию;
- `curl -v "http://localhost:8080/team1/path/example"` - 200 + body;
- `curl -v -X POST "http://localhost:8080/stub/data?key=/team1/path/example:GET&status=203"` -
устанавливаем корректный ответ со статусом 203 (body 2-й раз можем не передавать);
- `curl -v "http://localhost:8080/team1/path/example"` - 203 + body;
- `curl -v -X POST "http://localhost:8080/stub/error?key=/team1/path/example:GET&status=401"` - устанавливаем статус 401 с
сообщением об ошибке по-умолчанию (/stub/error);
- `curl -v "http://localhost:8080/team1/path/example"` - 401 + default error message;
- `curl -v -X POST "http://localhost:8080/stub/error?key=/team1/path/example:GET&status=401&message=My%20error%20message"` -
устанавливаем статус 401 с сообщением об ошибке "My error message";
- `curl -v "http://localhost:8080/team1/path/example"` - 401 + "My error message";
- `curl -v "http://localhost:8080/stub/data?key=/team1/path/example:GET"` - смотрим какой корректный ответ
сейчас сопоставлен ключу _/team1/path/example:GET_. Видим: _{"body":{"id":1},"status":203}_
- `curl -v "http://localhost:8080/stub/error?key=/team1/path/example:GET"` - смотрим какой ответ с ошибкой
сейчас сопоставлен ключу _/team1/path/example:GET_. Видим: _{"message":"My error message","status":401}_
- `curl -v -X DELETE "http://localhost:8080/stub/data?key=/team1/path/example:GET"` - удаляем корректный ответ
для ключа _key=/team1/path/example:GET_
- `curl -v -X DELETE "http://localhost:8080/stub/error?key=/team1/path/example:GET"` - удаляем ответ
с ошибкой для ключа _key=/team1/path/example:GET_