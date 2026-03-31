package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.LoginData;
import pt.unl.adc.project.model.LoginRequest;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.User;
import pt.unl.adc.project.repositories.SessionTokenRepository;
import pt.unl.adc.project.repositories.UserRepository;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Esta classe é responsável por fazer login de um utilizador.
 */
@Path("/login")
public class LoginResource {

    private static final String INVALID_CREDENTIALS = "9900";
    private static final String USER_NOT_FOUND = "9902";
    private static final String INVALID_INPUT = "9906";

    private final UserRepository userRepository = new UserRepository();
    private final SessionTokenRepository sessionTokenRepository = new SessionTokenRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        if (request == null || request.getInput() == null) {
            return invalidInputResponse("Missing input data");
        }

        LoginData data = request.getInput();

        if (isBlank(data.getUsername()) || isBlank(data.getPassword())) {
            return invalidInputResponse("Missing required fields");
        }

        User user = userRepository.getUser(data.getUsername());

        if (user == null) {
            return Response.ok(
                    Map.of(
                            "status", USER_NOT_FOUND,
                            "data", "The username referred in the operation doesn’t exist in registered accounts"
                    )
            ).build();
        }

        if (!user.getPassword().equals(data.getPassword())) {
            return Response.ok(
                    Map.of(
                            "status", INVALID_CREDENTIALS,
                            "data", "The username-password pair is not valid"
                    )
            ).build();
        }

        long issuedAt = Instant.now().getEpochSecond();
        long expiresAt = issuedAt + 900;

        SessionToken sessionToken = new SessionToken(
                UUID.randomUUID().toString(),
                user.getUsername(),
                user.getRole(),
                issuedAt,
                expiresAt
        );

        sessionTokenRepository.addSessionToken(sessionToken);

        return Response.ok(
                Map.of(
                        "status", "success",
                        "data", Map.of(
                                "token", sessionToken
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
