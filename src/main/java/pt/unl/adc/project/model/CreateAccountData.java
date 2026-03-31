package pt.unl.adc.project.model;

public class CreateAccountData {

    private String username;
    private String password;
    private String confirmation;
    private String email;
    private String phone;
    private String address;
    private String role;
    /**
     * Esta Classe consiste em receber e guardar os dados enviados no pedido Rest
     * para a operação op1-createaccount
     */
    public CreateAccountData() {
    }

    public CreateAccountData(String username, String password, String confirmation, String email, String phone, String address, String role) {
        this.username = username;
        this.password = password;
        this.confirmation = confirmation;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
