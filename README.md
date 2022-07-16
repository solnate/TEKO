## RequestFromFile
- Post-запрос с чтением из файла

## initiatorsRequest
- InitPayment/
- POST-запрос протокола инициатора
- Можно получить callback на Server
```
null: [HTTP/1.1 200 OK]
X-Frame-Options: [DENY]
Strict-Transport-Security: [max-age=31536000; includeSubDomains; preload]
Server: [nginx/1.18.0 (Ubuntu)]
X-Content-Type-Options: [nosniff]
Connection: [keep-alive]
Content-Length: [90]
Date: [Fri, 15 Jul 2022 17:42:24 GMT]
Content-Type: [application/json; charset=UTF-8]
Receive:
{
	"success": true,
	"result": {
		"tx": {
			"id": "62d1a70060b216e44a57b9c2",
			"start_t": 1657906944561
		}
	}
}
```
## merchantRequest 
- isPaymentPossible/
- POST-запрос протокола мерчанта
```
null: [HTTP/1.1 200 OK]
Content-Length: [163]
Content-Type: [application/json]
Receive:
{
	"success": "true",
	"result": {
		"tx": {
			"id": "11223344556677",
			"start_t": "1537134068907"
		},
		"src_payment": {
			"amount": 10100,
			"currency": 643,
			"exponent": 3
		},
		"rate": 110,
		"code": 0
	}
}
```
## Server
- Http-обработчик запроса мерчанта и передачи ответа
- Тестировал на открытом порту + merchantRequest + https://reqbin.com/

``` Socket[addr=/89.169.52.44,port=56916,localport=80]
    Receive:
    POST / HTTP/1.1
    Content-Type: application/json
    Signature: qCj9kuQq9/7Yd4wTcwu26ZlD8ko=
    User-Agent: Java/18.0.1.1
    Host: 89.169.52.44
    Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2
    Connection: keep-alive
    Content-Length: 401
    {
	"client": {
		"id": "praktika_2022",
		"showcase": "app"
	},
	"product": "spotify",
	"payment": {
		"amount": 10100,
		"currency": 643,
		"exponent": 3
	},
	"src_payment": {
		"amount": 5000,
		"currency": 343,
		"exponent": 3
	},
	"src": {
		"cls": "mc",
		"phone_number": "78005553535",
		"operator": "mts"
	},
	"order": {
		"transaction": {
			"id": "1122334455",
			"start_t": 142843063
		},
		"cls": "transaction",
		"extra": {
			"from": "mobile_app",
			"some_key": "some_value"
		}
	},
	"tag": "Europe"
  }

    Sent:
    HTTP/1.1 200 OK
    Content-Length: 163
    Content-Type: application/json
    {
	"success": "true",
	"result": {
		"tx": {
			"id": "11223344556677",
			"start_t": "1537134068907"
		},
		"src_payment": {
			"amount": 10100,
			"currency": 643,
			"exponent": 3
		},
		"rate": 110,
		"code": 0
	}
   }
```
## Пример записей в mongodb
![alt text](https://github.com/solnate/TEKO/blob/master/Снимок%20экрана%202022-07-15%20210132.png)
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
