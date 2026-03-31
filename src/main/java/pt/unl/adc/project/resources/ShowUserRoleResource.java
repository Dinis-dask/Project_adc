package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.ShowUserRoleRequest;
import pt.unl.adc.project.model.User;
import pt.unl.adc.project.repositories.SessionTokenRepository;
import pt.unl.adc.project.repositories.UserRepository;

import java.util.Map;

/**
 * Esta classe é responsável por mostrar o role de um utilizador registado.
 */
@Path("/showuserrole")
public class ShowUserRoleResource {

    private static final String USER_NOT_FOUND = "9902";
    private static final String INVALID_TOKEN = "9903";
    private static final String TOKEN_EXPIRED = "9904";
    private static final String UNAUTHORIZED = "9905";
    private static final String INVALID_INPUT = "9906";

    private final SessionTokenRepository sessionTokenRepository = new SessionTokenRepository();
    private final UserRepository userRepository = new UserRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response showUserRole(ShowUserRoleRequest request) {
        if (request == null || request.getInput() == null || request.getToken() == null) {
            return invalidInputResponse("Missing input or token");
        }

        if (isBlank(request.getInput().getUsername())) {
            return invalidInputResponse("Missing username");
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

        if (!"ADMIN".equals(storedToken.getRole()) && !"BOFFICER".equals(storedToken.getRole())) {
            return Response.ok(
                    Map.of(
                            "status", UNAUTHORIZED,
                            "data", "The operation is not allowed for the user role"
                    )
            ).build();
        }

        User user = userRepository.getUser(request.getInput().getUsername());

        if (user == null) {
            return Response.ok(
                    Map.of(
                            "status", USER_NOT_FOUND,
                            "data", "The username referred in the operation doesn’t exist in registered accounts"
                    )
            ).build();
        }

        return Response.ok(
                Map.of(
                        "status", "success",
                        "data", Map.of(
                                "username", user.getUsername(),
                                "role", user.getRole()
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
