package pt.unl.adc.project.model;

public class LogoutRequest {

    private LogoutData account;
    private SessionToken token;

    /**
     * Classe que representa o corpo completo do pedido para terminar sessões autenticadas.
     */
    public LogoutRequest() {
    }

    public LogoutData getInput() {
        return account;
    }

    public void setInput(LogoutData input) {
        this.account = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
