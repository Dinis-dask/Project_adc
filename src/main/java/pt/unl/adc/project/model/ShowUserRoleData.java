package pt.unl.adc.project.model;

public class ShowUserRoleData {

    private String username;

    /**
     * Classe que contém o username usado para consultar o role de um utilizador.
     */
    public ShowUserRoleData() {
    }

    public ShowUserRoleData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
