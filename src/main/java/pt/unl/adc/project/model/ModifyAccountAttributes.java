package pt.unl.adc.project.model;

public class ModifyAccountAttributes {

    private String username;
    private String email;
    private String phone;
    private String address;
    /**
     * Classe que contém os atributos que podem ser modificados de uma conta.
     */
    public ModifyAccountAttributes() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
