package pt.unl.adc.project.model;

public class SessionToken {

    private String tokenId;
    private String username;
    private String role;
    private Long issuedAt;
    private Long expiresAt;
    /**
     * Classe que representa um token de sessão autenticada.
     * O token é gerado após um login bem sucedido e é usado
     */
    public SessionToken() {
    }

    public SessionToken(String tokenId, String username, String role, Long issuedAt, Long expiresAt) {
        this.tokenId = tokenId;
        this.username = username;
        this.role = role;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
