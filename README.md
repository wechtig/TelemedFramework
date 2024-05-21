# TelemedFramework
Um das Framework zu starten gibt es zwei Möglichkeiten:

**1. Mit dem Initializer**
Man verwendet den Initializer und generiert damit die SQL Skripte, Jar Files, das Logo Bild und das Property File. Dannach muss man die Datenbank erstellen und alle Files in einen Ordner geben. 
Das Ganze kann gestartet werden mit "java -jar KOMPONENTENNAME". Die gewünschten Komponenten müssen einzeln gestartet werden.

1.1 Startn des Initializers ohne Entwicklungsumgebung
Der Initalizer braucht folgende Ordner: 
- Config: Mit den application.properties, beinhalten aber nur das Port
- Files: SQL Skripte (in der Initializerkomponente oder einzeln in den anderen)
- Modules: Die Jar Files, welche dann der Initializer anbieten kann. Diese können einfach aus den Target Ordner aus den Komponenten kopiert werden (mvn clean install zum Erstellen)
Ist die Struktur erstellt, kann man Sie einfach mit: java -jar INITIALIZERNAME.jar starten

![image](https://github.com/wechtig/TelemedFramework/assets/39216564/dc27cbb0-4121-4724-aef2-59729e99b5d9)

1.1 Starten des Initializer mit Entwicklungsumgebunug
Der Initializer bietet die anderen Module an. Um diesen verwenden zu können muss der Initializer aber auf die anderen Module (.jar)-Dateien zugreifen können. Dazu muss ein Ordner /modules mit den Jar Files erstellt und befüllt werden (aus dem generierten /target Folders der anderen Module kopieren).


**2. Mit der Entwicklungsumgebung**
Man verwendet eine Entwicklungsumgebung. Für die Entwicklung und Testzwecke gibt es in den Komponentenordnern ein initiales Logobild und ein initiales Propertyfile, welches dann auch angepasst werden kann. (Getestet und entwickelt mit Intellij) 
In den Ordnern der einzelnen Komponenten gibt es auch immer ein create.sql für die Datenbanktabellen der einzelnen Komponenten.

**Zusatzinfos**
- Die verwendete Datenbank ist eine Mysql(MariaDB).
- Beim ersten Anmelden gibt es noch keinen Benutzer. Aus Sicherheitsgründen kann man sich aber nur als Patient registrieren. Um einen Doktor zu erstellen, muss man einfach in der Datenbank das Feld Role in der Usertabelle von PATIENT auf DOCTOR umstellen
- Die Ports sind:
    Initializer: 9000,
    Root: 8080 (muss für das Anmelden immer gestartet sein),
    Communication: 8081,
    Symptom: 8082,
    Course: 8083,
    Export: 8084,
    Appointment: 8085
