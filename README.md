## initiatorsRequest
- InitPayment/
- POST-запрос протокола инициатора
## merchantRequest 
- isPaymentPossible/
- POST-запрос протокола мерчанта
## Server
- Http-обработчик запроса мерчанта и передачи ответа

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
> 7* - записывать происходящие события в БД MongoDB
