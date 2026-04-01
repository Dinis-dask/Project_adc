package pt.unl.adc.project.model;

public class LogoutData {

    private String username;

    /**
     * Classe que contém o username cujas sessões devem ser terminadas.
     */
    public LogoutData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
