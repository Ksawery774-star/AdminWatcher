# AdminWatcher v2.2

Czysty, czytelny plugin do monitorowania administracji.
Napisany tak, jak pisałby go normalny deweloper Minecrafta – bez zbędnego nadęcia, ale z głową.

## Główne usprawnienia w tej wersji
- Prawdziwe wykrywanie podejrzanej aktywności na podstawie czasu (creative + give w oknie czasowym)
- Lepsze logowanie komendy /give (wykrywa czy dał komuś innemu)
- Czysta, naturalna struktura kodu
- Łatwo rozbudować dalej

## Jak działa
- Loguje wejście w creative
- Loguje użycie monitorowanych komend
- Jeśli gracz wszedł w creative i w ciągu X sekund użył /give → od razu alert "SUSPICIOUS"
- Komenda /adminlogs pokazuje ostatnie zdarzenia

Stworzone z myślą o realnych serwerach.