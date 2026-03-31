package pt.unl.adc.project.model;

public class LoginRequest {

    private LoginData account;
    /**
     * Classe que representa o pedido para a operação op3-login.
     */
    public LoginRequest() {
    }

    public LoginRequest(LoginData login) {
        this.account = login;
    }

    public LoginData getInput() {
        return account;
    }

    public void setInput(LoginData login) {
        this.account = login;
    }
}
