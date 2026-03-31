package pt.unl.adc.project.model;

public class ModifyAccountRequest {

    private ModifyAccountData input;
    private SessionToken token;
    /**
     * Classe que representa o pedido para a operação op5-modifyAccount.
     */
    public ModifyAccountRequest() {
    }

    public ModifyAccountData getInput() {
        return input;
    }

    public void setInput(ModifyAccountData input) {
        this.input = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
