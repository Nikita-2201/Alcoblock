# Alcoblock
Пьянству бой!

Завернуть в докер(скорее всего не доделано)
```bash
cd server
mvn package
```
```bash
cd client
./mvn package
```
```bash
docker-compose up
```

Запуск приложения:
client/src/main/java/ru/gknsv/ClientApplication.java

API:

Auth

POST /auth/singup – Регистрация:
Тело:
{"login": "string",
"password": "string",
"sex": "string",
"height": double,
"weight": double,
”experience”: double
}
POST /auth/singin – Авторизация:
Тело:
{
"login": "string",
"password": "string",
}

User

POST /user/update – Редактирование пользователя
Тело:
{"login": "string",
"password": "string",
"sex": "string",
"height": double,
"weight": double,
”experience”: double
}
‌

GET /user/{userId} - Чтение пользователя
userId - идентификатор пользователя
Результат - пользователь
‌

GET /user/delete/{userId} - Удаление пользователя
userId - идентификатор пользователя
Результат - пользователь удален, ничего не возвращает

Alco

GET /alco/{userId} - Получить список алкоголя:
userId - идентификатор пользователя
Результат - список алкоголя
‌

POST /alco/add - Добавление персонального алкоголя:
{
"title": "string",
"strength": double,
"userId": "string"
}
Результат - алкоголь добавлен, возвращает созданную запись
‌

GET /alco/delete/{alcoId} - Удаление персонального алкоголя:
alcoId - идентификатор алкоголя
Результат - алкоголь удален, ничего не возвращает


Images

GET images/{ImageId}/ - Получение картинок
ImageId - идентификатор Картинки
Результат - возвращает “Картинку” по текущему id


History

GET history/{userId} – взятие даты конкретного пользователя
userId - история у конкретного пользователя
Результат - список уникальных дат без времени у конкретного пользователя
‌

POST history/add – занести дату и вывести разницу
{
"alcoId" : "String",
"userId": "String",
"volume" : "String"
}
Результат - выводит разницу между текущей датой и последней добавленной датой в БД, измерение: миллисекунды(тип данных Long)