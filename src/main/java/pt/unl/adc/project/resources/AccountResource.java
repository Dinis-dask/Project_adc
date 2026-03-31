package pt.unl.adc.project.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.adc.project.model.CreateAccountData;
import pt.unl.adc.project.model.CreateAccountRequest;
import pt.unl.adc.project.model.User;
import pt.unl.adc.project.repositories.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * Esta classe é responsável por criar uma nova conta de utilizador.
 */
@Path("/createaccount")
public class AccountResource {

    private static final String USER_ALREADY_EXISTS = "9901";
    private static final String INVALID_INPUT = "9906";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository = new UserRepository();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest request) {
        if (request == null || request.getInput() == null) {
            return invalidInputResponse("Missing input data");
        }

        CreateAccountData data = request.getInput();

        if (isBlank(data.getUsername()) || isBlank(data.getPassword()) || isBlank(data.getConfirmation())
                || isBlank(data.getPhone()) || isBlank(data.getAddress())
                || isBlank(data.getRole())) {
            return invalidInputResponse("Missing required fields");
        }

        if (!isValidUsername(data.getUsername())) {
            return invalidInputResponse("Username must use an email address format");
        }

        if (!data.getPassword().equals(data.getConfirmation())) {
            return invalidInputResponse("Password and confirmation do not match");
        }

        if (!isValidRole(data.getRole())) {
            return invalidInputResponse("Invalid role");
        }

        if (userRepository.exists(data.getUsername())) {
            return Response.ok(
                    Map.of(
                            "status", USER_ALREADY_EXISTS,
                            "data", "Error in creating an account because the username already exists"
                    )
            ).build();
        }

        User user = new User(
                UUID.randomUUID().toString(),
                data.getUsername(),
                data.getPassword(),
                isBlank(data.getEmail()) ? data.getUsername() : data.getEmail(),
                data.getPhone(),
                data.getAddress(),
                data.getRole()
        );

        userRepository.addUser(user);

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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidRole(String role) {
        return "USER".equals(role) || "BOFFICER".equals(role) || "ADMIN".equals(role);
    }

    private boolean isValidUsername(String username) {
        return EMAIL_PATTERN.matcher(username).matches();
    }
}
