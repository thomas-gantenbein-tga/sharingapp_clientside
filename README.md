# TeilHabe -- Clientseitige Anwendung

## Über dieses Projekt
Diese Android-App ist eine Art Schwarzes Brett für ein Quartier / eine Siedlung,
über das Menschen Dinge verleihen und ausleihen können, die manchmal ganz
praktisch wären, aber meist nur unbenutzt rumliegen: Grössere Werkzeuge oder Küchen-
utensilien zum Beispiel.

Für die Speicherung der Daten und den Austausch der Daten wird ein Rest-Service
benutzt, der auf Google-Appengine deployt ist. Der Quellcode dieser []serverseitigen
Anwendung](https://github.com/thomas-gantenbein-tga/sharingapp_serverside) ist ebenfalls frei 
verfügbar auf Github.

## Features
* Gegenstände lassen sich
  * nur mit Text erstellen,
  * zusätzlich mit Foto erstellen,
  * anzeigen (alle in einer Liste),
  * durchsuchen und
  * löschen.
* Die App läuft auch auf relativ alten Android-Versionen (bis Android 4.4 Kit-Kat).
* Die Bilddaten werden erst geladen, wenn die Detailansicht zu einem Gegenstand
  aufgerufen wird.
* Die offensichtlichsten Fehler werden abgefangen und behandelt (zum Beispiel:
  Server nicht erreichbar, keine Gegenstände gefunden, Fotoaufnahme abgebrochen).
* Der Mechanismus, wie und wo Daten gespeichert werden, lässt sich relativ einfach
  ändern, indem eine neue Klasse geschrieben wird, die das Interface `DataService` implementiert.
* Über das Feld ENDPOINT in der Klasse `ServerDataService` lässt sich einfach steuern, ob
  der lokale Entwicklungsserver oder der Google-Cloud-Server benutzt werden soll. 


## Limitierungen
* Keine Input-Validierung der Textfelder.
* Keine Authentifizierung und Autorisierung implementiert.
* Progress-Dialog ist nicht eben state-of-the-art, nur in `DeleteItemDetailActivity` ist die
  aus UX-Sicht bessere ProgressBar eingesetzt.
* Kein Menü eingefügt für schnelleres Navigieren zwischen den Activities.