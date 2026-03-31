package pt.unl.adc.project.model;

public class ShowUserRoleRequest {

    private ShowUserRoleData user;
    private SessionToken token;

    /**
     * Classe que representa o corpo completo do pedido para mostrar o role de um utilizador.
     */
    public ShowUserRoleRequest() {
    }

    public ShowUserRoleData getInput() {
        return user;
    }

    public void setInput(ShowUserRoleData input) {
        this.user = input;
    }

    public SessionToken getToken() {
        return token;
    }

    public void setToken(SessionToken token) {
        this.token = token;
    }
}
