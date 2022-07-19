## Server
- При получение GET-запроса отправляет POST-запрос на сервис TEKO и пересылает его
- При получение POST-запроса производит работу с базой данных в Mongodb и формирует ответ на основе шаблона API
- Примеры запросов:
```
GET http://localhost:80/initPayment
GET http://localhost:80/getPaymentsByTag
```
```
POST http://localhost:80/isPaymentPossible
Body:
{"client":{"id":"test_client","showcase":"showcases"},"product":"test_client_product","payment":{"amount":10000,"currency":623,"exponent":2},"src_payment":{"amount":5000,"currency":623,"exponent":2},"rate":110,"src":{"phone_number":"79771234567","operator":"tele2_ru","cls":"mc"},"dst":{"id":"login","extra":{"premium":true,"game_server":"Europe"}},"order":{"transaction":{"id":"qwerty","start_t":1508336966892,"finish_t":1508336969354},"cls":"transaction","extra":{"from":"mobile_app","some_key":"some_value"}},"tx":{"id":"59e765510cf26db591dbfb43","start_t":1508336977892},"partner_tx":{"id":"11223344556677","start_t":1537134068907},"extra":{"key":"important info"}}
```
## Некоторые пояснения
### isPaymentPossible
- Проверяет наличие ресурсов по id в базе и делает запись с временным id транзакции
### resumePayment
- Если находит id, созданный isPaymentPossible, то проводит работу с данными в базе и постоянную запись о платеже

### TO DO

# Задание
> 1. Разобраться с тем, что за формат такой JSON
> 2. Выбрать библиотеку работы с JSON на языке программирования, на котором планируете делать (идеальный вариант Java/Scala, библиотека json4s)
> 3. Изучить протокол работы с платформой ТЕКО:
> - https://docs.teko.io/api-reference/initiator-protocol
> - https://docs.teko.io/api-reference/intro/security
> - https://docs.teko.io/api-reference/merchant-protocol
> 4. Сделать себе репозиторий в гите для проекта
> 5. Разработать приложение для отправки запросов по инициаторскому протоколу (библиотека akka-http)
> 6. Разработать приложение для приёма запросов по мерчантскому протоколу (библиотека akka-http)
> 7. Записывать происходящие события в БД MongoDB
