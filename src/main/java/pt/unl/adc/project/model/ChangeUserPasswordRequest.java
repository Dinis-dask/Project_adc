package pt.unl.adc.project.model;

public class ChangeUserPasswordRequest {

    private ChangeUserPasswordData input;
    private SessionToken token;

    /**
     * Classe que representa o corpo completo do pedido para alterar a password de um utilizador.
     */
    public ChangeUserPasswordRequest() {
    }

    public ChangeUserPasswordData getInput() {
        return input;
    }

    public void setInput(ChangeUserPasswordData input) {
        this.input = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
