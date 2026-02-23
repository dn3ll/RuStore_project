package com.example.rustore


val Repo = listOf(
    App("sbol", "СберБанк", "Сбербанк", AppCategory.FINANCE,
        "Управляйте финансами", "СберБанк — ваш личный финансовый помощник для управления счетами и картами.",
        4.8f, "100M+", AgeRating.A0,
        listOf(Shot("s1", "Главный"), Shot("s2", "Карты")),
        iconRes = R.drawable.ic_sberbank),

    App("tinkoff", "Т-Банк", "Тбанк", AppCategory.FINANCE,
        "Банк без отделений", "Т-Банк — первый российский банк без отделений, все операции в приложении.",
        4.9f, "50M+", AgeRating.A0,
        listOf(Shot("t1", "Главная")),
        iconRes = R.drawable.ic_tinkoff),

    App("vtb", "ВТБ Онлайн", "ВТБ", AppCategory.FINANCE,
        "Мобильный банк", "ВТБ Онлайн — удобное приложение для клиентов банка ВТБ.",
        4.5f, "30M+", AgeRating.A0,
        listOf(Shot("v1", "Продукты")),
        iconRes = R.drawable.ic_vtb),

    App("yabro", "Яндекс Браузер", "Яндекс", AppCategory.TOOLS,
        "Быстрый браузер", "Яндекс Браузер — быстрый и безопасный браузер с защитой от мошенников.",
        4.6f, "100M+", AgeRating.A6,
        listOf(Shot("yb1", "Стартовая"), Shot("yb2", "Защита")),
        iconRes = R.drawable.ic_yandex_browser),

    App("tg", "Telegram", "Telegram FZ-LLC", AppCategory.TOOLS,
        "Быстрый мессенджер", "Telegram — мессенджер с фокусом на скорость и безопасность.",
        4.7f, "1B+", AgeRating.A12,
        listOf(Shot("tg1", "Чаты"), Shot("tg2", "Группы")),
        iconRes = R.drawable.ic_telegram),

    App("files", "Файловый менеджер", "Simple Mobile", AppCategory.TOOLS,
        "Управление файлами", "Простой и функциональный файловый менеджер без рекламы.",
        4.4f, "10M+", AgeRating.A0,
        listOf(Shot("f1", "Файлы")),
        iconRes = R.drawable.ic_files),

    App("brawl", "Brawl Stars", "Supercell", AppCategory.GAMES,
        "Командные бои 3 на 3", "Brawl Stars — динамичная многопользовательская игра с командными боями.",
        4.5f, "500M+", AgeRating.A12,
        listOf(Shot("b1", "Бой"), Shot("b2", "Бойцы"), Shot("b3", "Карты")),
        iconRes = R.drawable.ic_brawl_stars),

    App("clash", "Clash Royale", "Supercell", AppCategory.GAMES,
        "Стратегия в реальном времени", "Clash Royale — собирайте колоду карт и сражайтесь с игроками.",
        4.4f, "500M+", AgeRating.A12,
        listOf(Shot("c1", "Битва")),
        iconRes = R.drawable.ic_clash_royale),

    App("pubg", "PUBG MOBILE", "Level Infinite", AppCategory.GAMES,
        "Королевская битва", "PUBG MOBILE — культовая королевская битва на 100 игроков.",
        4.2f, "1B+", AgeRating.A16,
        listOf(Shot("p1", "Высадка"), Shot("p2", "Бой")),
        iconRes = R.drawable.ic_pubg),

    // ===== GOVERNMENT =====
    App("gos", "Госуслуги", "Ростелеком", AppCategory.GOVERNMENT,
        "Госуслуги онлайн", "Единый портал государственных услуг России для получения услуг онлайн.",
        4.0f, "100M+", AgeRating.A0,
        listOf(Shot("g1", "Главная"), Shot("g2", "Услуги")),
        iconRes = R.drawable.ic_gosuslugi),

    App("mos", "Госуслуги Москвы", "Правительство Москвы", AppCategory.GOVERNMENT,
        "Услуги Москвы", "Официальное приложение правительства Москвы для городских услуг.",
        4.3f, "20M+", AgeRating.A0,
        listOf(Shot("m1", "Услуги")),
        iconRes = R.drawable.ic_mos_ru),

    App("mfc", "Мои документы", "МФЦ России", AppCategory.GOVERNMENT,
        "МФЦ в кармане", "Приложение МФЦ для записи на приём и отслеживания заявлений.",
        3.8f, "5M+", AgeRating.A0,
        listOf(Shot("mf1", "Запись")),
        iconRes = R.drawable.ic_mfc),


    App("ygo", "Яндекс Go", "Яндекс", AppCategory.TRANSPORT,
        "Такси и доставка", "Яндекс Go — сервис для заказа такси, еды и доставки из магазинов.",
        4.6f, "100M+", AgeRating.A6,
        listOf(Shot("yg1", "Заказ"), Shot("yg2", "Еда")),
        iconRes = R.drawable.ic_yandex_go),


    App("metro", "Метро Москвы", "Московский метрополитен", AppCategory.TRANSPORT,
        "Схема метро", "Официальное приложение с интерактивной схемой московского метро.",
        4.2f, "10M+", AgeRating.A0,
        listOf(Shot("mt1", "Схема")),
        iconRes = R.drawable.ic_metro_moscow)
)