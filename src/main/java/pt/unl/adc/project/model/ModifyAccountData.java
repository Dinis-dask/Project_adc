package pt.unl.adc.project.model;

public class ModifyAccountData {

    private String username;
    private ModifyAccountAttributes attributes;

    /**
     * Classe que contém os dados necessários para modificar uma conta.
     */
    public ModifyAccountData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ModifyAccountAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(ModifyAccountAttributes attributes) {
        this.attributes = attributes;
    }
}
