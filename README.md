# AdminWatcher v2.0

**Zaawansowany plugin do monitorowania administracji na serwerach Minecraft (Paper 1.21+)**

## Główne funkcje

- Logowanie zmiany gamemode na **CREATIVE** (w tym na innych graczach)
- Szczegółowe logowanie komend: `/give`, `/i`, `/gamemode`, `/op`, `/deop`, `/enchant`, `/effect`, `/summon` i inne
- **Komenda `/adminlogs`** – przeglądanie ostatnich logów bezpośrednio w grze
- Opcjonalna integracja z **Discord** (webhook)
- Wykrywanie podejrzanej aktywności (creative + give w krótkim czasie)
- Konfigurowalna lista monitorowanych komend
- Logi do pliku + kolorowa konsola
- Bypass dla właścicieli (`adminwatcher.bypass`)

## Instalacja
1. Pobierz `.jar` z Releases lub zbuduj sam (`mvn clean package`)
2. Wrzuć do `plugins/`
3. Zrestartuj serwer
4. Edytuj `plugins/AdminWatcher/config.yml`

## Komendy
- `/adminwatcher reload` – przeładuj config
- `/adminlogs [ilość]` – pokaż ostatnie logi (domyślnie 10)

## Uprawnienia
- `adminwatcher.admin` – dostęp do komend reload i adminlogs (OP)
- `adminwatcher.bypass` – nie jest logowany

## Konfiguracja (config.yml)
```yaml
log-creative: true
monitored-commands:
  - give
  - gamemode
  # itd.

discord:
  enabled: false
  webhook-url: "https://discord.com/api/webhooks/..."

suspicious-activity:
  enabled: true
  creative-give-window-seconds: 30
```

Stworzone przez **Grok MC Developer** – jeśli chcesz coś dodać, pisz! 🚀