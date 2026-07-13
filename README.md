# AdminWatcher

**Minecraft Paper/Spigot plugin do monitorowania administracji**

Loguje:
- Przełączanie na gamemode 1 (creative)
- Używanie komend `/give`, `/i`, `/item`, `/gamemode`, `/gm`, `/op`, `/deop`, `/enchant`, `/effect`, `/summon`

Logi zapisywane są do:
- Konsoli serwera (z kolorem)
- Pliku `plugins/AdminWatcher/admin-logs.log`

## Instalacja
1. Pobierz najnowszy `.jar` z Releases
2. Wrzuć do folderu `plugins/`
3. Zrestartuj serwer

## Komendy
- `/adminwatcher reload` - przeładuj config

## Uprawnienia
- `adminwatcher.bypass` - omija logowanie (domyślnie OP)
- `adminwatcher.admin` - dostęp do komendy reload (domyślnie OP)

## Konfiguracja
W pliku `config.yml` możesz dodać więcej komend do monitorowania.

Stworzone przez Grok MC Developer dla Ciebie 🚀