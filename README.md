# VideoPlayerApp

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-brightgreen)](https://developer.android.com/jetpack/compose)
[![ExoPlayer](https://img.shields.io/badge/ExoPlayer-2.18.0-orange)](https://developer.android.com/guide/topics/media/exoplayer)
[![Dagger Hilt](https://img.shields.io/badge/Dagger%20Hilt-2.48.1-red)](https://dagger.dev/hilt/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

VideoPlayerApp — это учебное приложение, представляющее клиентскую часть видеоплатформы, аналогичную YouTube. Проект демонстрирует современные подходы к разработке Android-приложений с использованием чистой архитектуры (Clean Architecture).

> **Примечание**: Для полноценной работы приложения требуется подключение VPN, так как используется API Pexels, которое может быть недоступно в некоторых регионах.

## Основные функции
- 📺 **Список видео**: Отображение списка популярных видео с бесконечной прокруткой и поддержкой pull-to-refresh.
- 🔄 **Адаптивный интерфейс**: Поддержка портретной и ландшафтной ориентации с автоматическим переключением на полноэкранный режим.
- ▶️ **Воспроизведение видео**: Проигрывание видео через ExoPlayer с локальным кэшированием.
- 🎥 **Экран плеера**: Переход на экран воспроизведения с анимацией и управлением полноэкранным режимом.
- ⏳ **Индикатор загрузки**: Shimmer-эффект для отображения загрузки контента.
- 🛠 **Обработка ошибок**: Уведомления об ошибках сети или сервера с возможностью повторной попытки.
- 💾 **Кэширование**: Сохранение видео в локальной базе данных Room для работы оффлайн.

## Архитектура
- **Подход**: Чистая архитектура (Clean Architecture) с разделением на слои:
  - **Domain**: Бизнес-логика (UseCase, сущности).
  - **Data**: Источники данных (API, Room, кэширование).
  - **Presentation**: UI и ViewModel (MVVM).
- **MVVM**: Используется для привязки данных к UI через ViewModel.

## Технологии
- **Язык**: Kotlin 2.1.0
- **UI**: Jetpack Compose 1.5.0
- **Воспроизведение видео**: ExoPlayer 2.18.0
- **Сетевые запросы**: Retrofit 2.9.0
- **Локальная база данных**: Room 2.6.1
- **Внедрение зависимостей**: Dagger Hilt 2.48.1
- **Загрузка изображений**: Coil 2.1.0
- **Навигация**: Navigation Compose 2.8.7

