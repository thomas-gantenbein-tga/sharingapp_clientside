# Rudimentary client app
## Showing that we can get data from "our" server
* Methode receiveData in Klasse MainActivity wird aufgerufen, sobald alle Daten vom Server --
  oder eben einer anderen Datenquelle hier sind. Hier kommt der Code hin, der die Daten
  in der App sichtbar macht. 
* Run with localhost: Start server locally. Use URL http://10.0.2.2:8080/items in ServerDataService. 
  10.0.2.2 points to "localhost" of the machine on which the emulator is running.
  See [Documentation](https://developer.android.com/studio/run/emulator-networking).
* Run with Google App Engine: Use URL https://fabled-coder-210208.appspot.com/items in ServerDataService.
* network_security_config.xml and android:networkSecurityConfig="@xml/network_security_config"
  in Manifest necessary to allow cleartext connections to localhost.
