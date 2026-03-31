package pt.unl.adc.project.model;

public class DeleteAccountRequest {

    private DeleteAccountData account;
    private SessionToken token;
    /**
     * Classe que representa o pedido para a operação op2-deleteAccount.
     * 
     */
    public DeleteAccountRequest() {
    }

    public DeleteAccountData getInput() {
        return account;
    }

    public void setInput(DeleteAccountData account) {
        this.account = account;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
