package kudos.web.beans.response;

/**
 * Created by chc on 15.7.30.
 */
public class IndexResponse extends Response {

    private boolean isLogged;

    public IndexResponse() {}

    public IndexResponse(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
}
