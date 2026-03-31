package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.ChangeUserRoleRequest;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.User;
import pt.unl.adc.project.repositories.SessionTokenRepository;
import pt.unl.adc.project.repositories.UserRepository;

import java.util.Map;

/**
 * Esta classe é responsável por alterar o role de um utilizador registado.
 */
@Path("/changeuserrole")
public class ChangeUserRoleResource {

    private static final String USER_NOT_FOUND = "9902";
    private static final String INVALID_TOKEN = "9903";
    private static final String TOKEN_EXPIRED = "9904";
    private static final String UNAUTHORIZED = "9905";
    private static final String INVALID_INPUT = "9906";
    private static final String FORBIDDEN = "9907";

    private final SessionTokenRepository sessionTokenRepository = new SessionTokenRepository();
    private final UserRepository userRepository = new UserRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserRole(ChangeUserRoleRequest request) {
        if (request == null || request.getInput() == null || request.getToken() == null) {
            return invalidInputResponse("Missing input or token");
        }

        if (isBlank(request.getInput().getUsername()) || isBlank(request.getInput().getNewRole())) {
            return invalidInputResponse("Missing username or newRole");
        }

        if (!isValidRole(request.getInput().getNewRole())) {
            return invalidInputResponse("Invalid role");
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

        User targetUser = userRepository.getUser(request.getInput().getUsername());

        if (targetUser == null) {
            return Response.ok(
                    Map.of(
                            "status", USER_NOT_FOUND,
                            "data", "The username referred in the operation doesn’t exist in registered accounts"
                    )
            ).build();
        }

        if (targetUser.getRole().equals(request.getInput().getNewRole())) {
            return Response.ok(
                    Map.of(
                            "status", FORBIDDEN,
                            "data", "The operation generated a forbidden error by other reason"
                    )
            ).build();
        }

        targetUser.setRole(request.getInput().getNewRole());
        userRepository.updateUser(targetUser);

        // Existing sessions are removed so the user must authenticate again with the new role.
        sessionTokenRepository.deleteSessionTokensByUsername(targetUser.getUsername());

        return Response.ok(
                Map.of(
                        "status", "success",
                        "data", Map.of(
                                "message", "Role updated successfully"
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

    private boolean isValidRole(String role) {
        return "USER".equals(role) || "BOFFICER".equals(role) || "ADMIN".equals(role);
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
