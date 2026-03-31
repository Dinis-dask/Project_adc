package pt.unl.adc.project.model;

public class CreateAccountRequest {

    private CreateAccountData account;
    /**
     * Esta classe representa o pedido para a operação op1-createaccount
     * Corresponde a estrutura JSON 
     */
    public CreateAccountRequest() {
    }

    public CreateAccountRequest(CreateAccountData account) {
        this.account = account;
    }

    public CreateAccountData getInput() {
        return account;
    }

    public void setInput(CreateAccountData account) {
        this.account = account;
    }
}
