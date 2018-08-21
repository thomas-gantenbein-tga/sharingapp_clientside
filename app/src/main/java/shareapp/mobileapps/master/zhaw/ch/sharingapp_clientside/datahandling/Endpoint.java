package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

public enum Endpoint {
    LOCALHOST("http://10.0.2.2:8080"),
    GOOGLE_APP("https://fabled-coder-210208.appspot.com");
    private final String url;

    Endpoint(String url) {
        this.url = url;
    }

    public String getUrlBasePath() {
        return url;
    }
}
