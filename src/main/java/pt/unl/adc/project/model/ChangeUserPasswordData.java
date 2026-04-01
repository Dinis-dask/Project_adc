package pt.unl.adc.project.model;

public class ChangeUserPasswordData {

    private String username;
    private String oldPassword;
    private String newPassword;

    /**
     * Classe que contém os dados necessários para alterar a password de um utilizador.
     */
    public ChangeUserPasswordData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
