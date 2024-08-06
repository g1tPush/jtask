## Обзор

API для перевода

## Базовый URL

`http://localhost:8080/api/v1/`

## Конечные точки

### 1. Запрос на перевод

**Endpoint:** `/translation/request`

**Метод:** `POST`

**Описание:** Переводит текст с исходного языка на целевой.

**Тело запроса:**
```json
{
    "text": "string", 
    "targetLanguageCode": "string",
    "sourceLanguageCode": "string"
}
```

**Успешный ответ:**
```json
{
    "translatedString": "string"
}
```

**Неуспешный ответ:**
```json
{
    "message": "string"
}
```

## Запуск проекта

### Переменные окружения

| Переменная           | Значение                |
| -------------------- | ----------------------- |
| `API_FOLDER_ID`      | `yandex-api-folder-id`  |
| `API_KEY`            | `yandex-api-key`        |
| `API_URL`            | `yandex-api-url`        |
| `POSTGRES_DB`        | `translation`           |
| `POSTGRES_PASSWORD`  | `admin`                 |
| `POSTGRES_USER`      | `admin`                 |

Эти переменные окружения нужно передавать в `docker-compose`, `JUnit` и `jTaskApplication Configuration` при запуске.

### Шаги для запуска проекта

1. **Сборка проекта с использованием Maven**:

   Прежде чем запускать Docker-контейнеры, необходимо собрать проект. Перейдите в корневую директорию проекта и выполните следующую команду Maven:

   ```sh
   mvn clean compile
   ```

2. **Запуск Docker-контейнера**:

   Для запуска контейнеров используйте команду:

   ```sh
   docker-compose up
   ```

### Используемые порты

Контейнеры используют следующие порты:

```yaml
ports:
  - "5432:5432"  # PostgreSQL
  - "8080:8080"  # Приложение
```

## Как получить ключи

1. Пройдите аутентификацию через [Yandex ID](https://cloud.yandex.ru/).
2. После входа создайте облако и дефолтный каталог. Скопируйте `API_FOLDER_ID`.
   ![1.png](..%2F1.png)
3. Перейдите в раздел “Сервисные аккаунты” -> нажмите на троеточие справа вверху -> выберите “Создать сервисный аккаунт”.
   ![2.png](..%2F2.png)
   Заполните данные, выберите роль `ai.translate.user` и нажмите "Создать".
4. Заполните всю необходимую информацию для создания платежного аккаунта.
5. После создания сервисного аккаунта (возможно, потребуется перезагрузить страницу), перейдите на его страницу.
6. Генерируйте API ключ.
   ![3.png](..%2F3.png)
8. Сохраните "секретный ключ" в `API_KEY`.
8. `API_URL`: `https://translate.api.cloud.yandex.net/translate/v2/translate`

## Используемые технологии

- **Java** - язык программирования.
- **Spring Boot** - для создания REST API.
- **PostgreSQL** - реляционная база данных.
- **Liquibase** - инструмент для управления изменениями базы данных.
- **JUnit** - для написания юнит тестов.
- **Docker** - для контейнеризации приложения.
- **Docker Compose** - для оркестрации многоконтейнерных Docker приложений.
- **Maven** - инструмент для управления зависимостями и сборки проекта.
- **Yandex.Cloud** - облачный провайдер для хранения и обработки данных.