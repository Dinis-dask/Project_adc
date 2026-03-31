package pt.unl.adc.project.model;

public class ShowUsersRequest {

    private Object input;
    private SessionToken token;
    /**
     * Classe que representa o pedido para a operação op4-showUsers.
     */
    public ShowUsersRequest() {
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
