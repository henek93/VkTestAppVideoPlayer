# VideoPlayerApp (Для полноценной работы нужно подклють VPN, из-за используемого API)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-brightgreen)](https://developer.android.com/jetpack/compose)
[![ExoPlayer](https://img.shields.io/badge/ExoPlayer-2.18.0-orange)](https://developer.android.com/guide/topics/media/exoplayer)
[![Dagger Hilt](https://img.shields.io/badge/Dagger%20Hilt-2.44-red)](https://dagger.dev/hilt/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

VideoPlayerApp — это учебное приложение, представляющее собой клиентскую часть видеоплатформы, аналогичную YouTube. Проект разработан с использованием современных технологий Android-разработки.

## Основные функции
- 📺 **Список видео**: Отображение списка видео с поддержкой пагинации и pull-to-refresh.
- 🔄 **Адаптивный интерфейс**: Поддержка портретной и ландшафтной ориентации.
- ▶️ **Воспроизведение видео**: Использование ExoPlayer для воспроизведения видео с кэшированием данных.
- 🎥 **Экран видеоплеера**: Переход на экран видеоплеера с анимацией и поддержкой полноэкранного режима.
- ⏳ **Индикатор загрузки**: Shimmer-эффект для плавного отображения контента.
- 🛠 **Обработка ошибок**: Уведомления об ошибках сети и возможность повторной загрузки данных.

## Технологии
- **Язык программирования**: Kotlin
- **Архитектура**: MVVM
- **UI**: Jetpack Compose
- **Воспроизведение видео**: ExoPlayer
- **Сетевые запросы**: Retrofit
- **Локальное кэширование**: Room
- **DI**: Dagger Hilt
- **Загрузка изображений**: Coil

## Зависимости
- **Dagger Hilt**: Используется для управления зависимостями в приложении. Корневой компонент `ApplicationComponent` предоставляет зависимости для ViewModel и других слоев приложения.
- **ViewModelFactory**: Позволяет создавать ViewModel с использованием зависимостей, предоставляемых Dagger Hilt.


