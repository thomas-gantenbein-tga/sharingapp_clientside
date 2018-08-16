# Rudimentary client app
## Showing that we can get data from "our" server
* Run with localhost: Start server locally. Use URL http://10.0.2.2:8080/items in servercall(). 
  10.0.2.2 points to "localhost" of the machine on which the emulator is running.
  See [Documentation](https://developer.android.com/studio/run/emulator-networking).
* Run with Google App Engine: Use URL https://fabled-coder-210208.appspot.com/items in servercall().
* network_security_config.xml and android:networkSecurityConfig="@xml/network_security_config"
  in Manifest necessary to allow cleartext connections to localhost.
* Check console output to see that name of one item owner and the HTTP response code are
  printed (in the method onResponse(String response)).
* meine Zeile ...
