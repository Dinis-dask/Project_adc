package pt.unl.adc.project.model;

public class DeleteAccountData {

    private String username;
    /** 
     * Classe que contém os dados necessários para apagar uma conta.
     */
    public DeleteAccountData() {
    }

    public DeleteAccountData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
