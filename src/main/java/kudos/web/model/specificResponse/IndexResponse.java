package kudos.web.model.specificResponse;

import kudos.web.model.mainResponse.Response;

/**
 * Created by chc on 15.7.30.
 */
public class IndexResponse extends Response {

    private boolean isLogged;

    public boolean isLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
}
