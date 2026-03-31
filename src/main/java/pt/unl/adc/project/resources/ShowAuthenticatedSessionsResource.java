package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.ShowUsersRequest;
import pt.unl.adc.project.repositories.SessionTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta classe é responsável por mostrar as sessões autenticadas de um utilizador.
 */
@Path("/showauthsessions")
public class ShowAuthenticatedSessionsResource {

    private static final String INVALID_TOKEN = "9903";
    private static final String TOKEN_EXPIRED = "9904";
    private static final String UNAUTHORIZED = "9905";
    private static final String INVALID_INPUT = "9906";

    private final SessionTokenRepository sessionTokenRepository = new SessionTokenRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAuthenticatedSessions(ShowUsersRequest request) {
        if (request == null || request.getToken() == null) {
            return invalidInputResponse("Missing token");
        }

        SessionToken requestToken = request.getToken();

        if (isInvalidToken(requestToken)) {
            return invalidTokenResponse("The operation is called with an invalid token");
        }

        SessionToken storedToken = sessionTokenRepository.getSessionToken(requestToken.getTokenId());

        if (storedToken == null) {
            return invalidTokenResponse("The operation is called with an invalid token");
        }

        if (!storedToken.getUsername().equals(requestToken.getUsername())
                || !storedToken.getRole().equals(requestToken.getRole())
                || !storedToken.getIssuedAt().equals(requestToken.getIssuedAt())
                || !storedToken.getExpiresAt().equals(requestToken.getExpiresAt())) {
            return invalidTokenResponse("The operation is called with an invalid token");
        }

        if (storedToken.getExpiresAt() < currentTimestamp()) {
            return Response.ok(
                    Map.of(
                            "status", TOKEN_EXPIRED,
                            "data", "The operation is called with a token that is expired"
                    )
            ).build();
        }

        if (!"ADMIN".equals(storedToken.getRole())) {
            return Response.ok(
                    Map.of(
                            "status", UNAUTHORIZED,
                            "data", "The operation is not allowed for the user role"
                    )
            ).build();
        }

        List<Map<String, Object>> sessionsResponse = new ArrayList<>();

        for (SessionToken sessionToken : sessionTokenRepository.getAllSessionTokens()) {
            sessionsResponse.add(Map.of(
                    "tokenId", sessionToken.getTokenId(),
                    "username", sessionToken.getUsername(),
                    "role", sessionToken.getRole(),
                    "expiresAt", sessionToken.getExpiresAt()
            ));
        }

        return Response.ok(
                Map.of(
                        "status", "success",
                        "data", Map.of(
                                "sessions", sessionsResponse
                        )
                )
        ).build();
    }

    private Response invalidInputResponse(String message) {
        return Response.ok(
                Map.of(
                        "status", INVALID_INPUT,
                        "data", message
                )
        ).build();
    }

    private Response invalidTokenResponse(String message) {
        return Response.ok(
                Map.of(
                        "status", INVALID_TOKEN,
                        "data", message
                )
        ).build();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isInvalidToken(SessionToken token) {
        return isBlank(token.getTokenId()) || isBlank(token.getUsername())
                || isBlank(token.getRole()) || token.getIssuedAt() == null
                || token.getExpiresAt() == null;
    }

    private long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
