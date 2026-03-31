package pt.unl.adc.project.model;

public class ChangeUserRoleRequest {

    private ChangeUserRoleData input;
    private SessionToken token;

    /**
     * Classe que representa o corpo completo do pedido para alterar o role de um utilizador.
     */
    public ChangeUserRoleRequest() {
    }

    public ChangeUserRoleData getInput() {
        return input;
    }

    public void setInput(ChangeUserRoleData input) {
        this.input = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
