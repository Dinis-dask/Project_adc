package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.ModifyAccountAttributes;
import pt.unl.adc.project.model.ModifyAccountRequest;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.User;
import pt.unl.adc.project.repositories.SessionTokenRepository;
import pt.unl.adc.project.repositories.UserRepository;

import java.util.Map;

/**
 * Esta classe é responsável por modificar os atributos de uma conta de utilizador.
 */
@Path("/modaccount")
public class ModifyAccountResource {

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
    public Response modifyAccount(ModifyAccountRequest request) {
        if (request == null || request.getInput() == null || request.getToken() == null) {
            return invalidInputResponse("Missing input or token");
        }

        if (isBlank(request.getInput().getUsername()) || request.getInput().getAttributes() == null) {
            return invalidInputResponse("Missing username or attributes");
        }

        ModifyAccountAttributes attributes = request.getInput().getAttributes();

        if (!isBlank(attributes.getUsername())) {
            return invalidInputResponse("Username attribute cannot be changed");
        }

        if (isBlank(attributes.getEmail()) && isBlank(attributes.getPhone()) && isBlank(attributes.getAddress())) {
            return invalidInputResponse("No updatable attributes were provided");
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

        User targetUser = userRepository.getUser(request.getInput().getUsername());

        if (targetUser == null) {
            return Response.ok(
                    Map.of(
                            "status", USER_NOT_FOUND,
                            "data", "The username referred in the operation doesn’t exist in registered accounts"
                    )
            ).build();
        }

        if (!canModify(storedToken, targetUser)) {
            return Response.ok(
                    Map.of(
                            "status", UNAUTHORIZED,
                            "data", "The operation is not allowed for the user role"
                    )
            ).build();
        }

        if (!isBlank(attributes.getEmail())) {
            targetUser.setEmail(attributes.getEmail());
        }

        if (!isBlank(attributes.getPhone())) {
            targetUser.setPhone(attributes.getPhone());
        }

        if (!isBlank(attributes.getAddress())) {
            targetUser.setAddress(attributes.getAddress());
        }

        userRepository.updateUser(targetUser);

        return Response.ok(
                Map.of(
                        "status", "success",
                        "data", Map.of(
                                "message", "Updated successfully"
                        )
                )
        ).build();
    }

    private boolean canModify(SessionToken storedToken, User targetUser) {
        if ("ADMIN".equals(storedToken.getRole())) {
            return true;
        }

        if ("USER".equals(storedToken.getRole())) {
            return storedToken.getUsername().equals(targetUser.getUsername());
        }

        if ("BOFFICER".equals(storedToken.getRole())) {
            return storedToken.getUsername().equals(targetUser.getUsername()) || "USER".equals(targetUser.getRole());
        }

        return false;
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
