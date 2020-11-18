## Что это?

Telegram-бот для генерации готового для печати Word-файла, содержащего задания для обучения устному счёту

Параметры заданий и файла (минимальное и максимальное используемые числа, количество страниц) определяет пользователь

Бот принесёт пользу родителям, которые хотят подтянуть навыки устного счёта у своих детей

## Лицензия

Этот проект лицензируется в соответствии с лицензией Apache 2.0

Подробности в файле LICENSE.md

## Попробовать

[@MentalCalculationBot](https://t.me/MentalCalculationBot) доступен в Telegram

## Автор

Сергей Козырев

## Контакты для связи

Telegram [@taksebe](https://t.me/taksebe)

## Создано с помощью

Java™ SE Development Kit 11.0.5

Git - управление версиями

GitHub - репозиторий

[Heroku](https://www.heroku.com/) - деплой, хостинг

[Telegram Bots](https://core.telegram.org/bots) - взаимодействие с Telegram

[Apache Maven](https://maven.apache.org/) - сборка, управление зависимостями

[Apache POI](https://poi.apache.org/) - создание документа Word

[Lombok](https://projectlombok.org/) - упрощение кода, замена стандартных java-методов аннотациями

[Apache Log4j](https://logging.apache.org/log4j/) - логирование

[JUnit 5](https://junit.org/junit5/) - тестирование

Полный список зависимостей и используемые версии компонентов можно найти в pom.xml

## Порядок развёртывания

### Создание бота

Необходимо создать бота с помощью [BotFather](https://t.me/botfather) и сохранить его имя и токен

### Развертывание на heroku

Проект писался для релиза на [heroku](https://www.heroku.com/) и содержит файл Procfile, специфический именно для этой площадки

Порядок:
+ `mvn clean install`
+ зарегистрироваться на heroku
+ перейти в консоль
+ `heroku login` и следовать инструкции
+ `heroku create <имя приложения>`
+ `heroku ps:scale worker=1` - установить количество контейнеров (dynos) для типа процесса worker (устанавливается в Procfile)
+ `git push heroku master`
+ `heroku config:set BOT_NAME=<имя бота>` - добавить имя бота (получен от BotFather) в переменные окружения
+ `heroku config:set BOT_TOKEN=<токен бота>` - добавить токен бота (получен от BotFather) в переменные окружения
+ при необходимости - в интерфейсе управления приложением в личном кабинете на [heroku](https://www.heroku.com/) переключить деплой на GitHub-репозиторий (по запросу или автоматически)

## Отдельное спасибо

[Владу](https://github.com/itotx), который возится со мной, неразумным