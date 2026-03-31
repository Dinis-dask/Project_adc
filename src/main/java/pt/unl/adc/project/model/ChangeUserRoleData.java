package pt.unl.adc.project.model;

public class ChangeUserRoleData {

    private String username;
    private String newRole;

    /**
     * Classe que contém o username do cliente e o novo role a atribuir.
     */
    public ChangeUserRoleData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}
