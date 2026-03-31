package pt.unl.adc.project.repositories;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import pt.unl.adc.project.model.SessionToken;
import pt.unl.adc.project.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
/**
 * Esta classe é responsável por gerir os SessionTokens na base de dados.
 */
public class SessionTokenRepository {

    private static final String KIND = "SessionToken";

    private final Datastore datastore;
     /**
     * Construtor que inicializa a ligação ao Datastore.
     */
    public SessionTokenRepository() {
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }
    /**
     * Guarda um novo token de sessão na base de dados.
    */
    public void addSessionToken(SessionToken sessionToken) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(sessionToken.getTokenId());

        Entity entity = Entity.newBuilder(key)
                .set("tokenId", sessionToken.getTokenId())
                .set("username", sessionToken.getUsername())
                .set("role", sessionToken.getRole())
                .set("issuedAt", sessionToken.getIssuedAt())
                .set("expiresAt", sessionToken.getExpiresAt())
                .build();

        datastore.put(entity);
    }
    /**
     * Procura um token de sessão na base de dados usando o tokenId.
    */
    public SessionToken getSessionToken(String tokenId) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(tokenId);
        Entity entity = datastore.get(key);

        if (entity == null) {
            return null;
        }

        return new SessionToken(
                entity.getString("tokenId"),
                extractUsername(entity),
                entity.getString("role"),
                extractTimestamp(entity, "issuedAt"),
                extractTimestamp(entity, "expiresAt")
        );
    }
    /**
     * Remove todos os tokens associados a um utilizador.
     */
    public void deleteSessionTokensByUsername(String username) {
        EntityQuery query = Query.newEntityQueryBuilder().setKind(KIND).build();
        QueryResults<Entity> results = datastore.run(query);

        while (results.hasNext()) {
            Entity entity = results.next();

            if (username.equals(extractUsername(entity))) {
                datastore.delete(entity.getKey());
            }
        }
    }
    /**
     * Obtém todos os tokens de sessão existentes na base de dados.
     */
    public List<SessionToken> getAllSessionTokens() {
        EntityQuery query = Query.newEntityQueryBuilder().setKind(KIND).build();
        QueryResults<Entity> results = datastore.run(query);
        List<SessionToken> sessionTokens = new ArrayList<>();

        while (results.hasNext()) {
            Entity entity = results.next();
            String username = extractUsername(entity);

            if (username != null) {
                sessionTokens.add(new SessionToken(
                        entity.getString("tokenId"),
                        username,
                        entity.getString("role"),
                        extractTimestamp(entity, "issuedAt"),
                        extractTimestamp(entity, "expiresAt")
                ));
            }
        }

        return sessionTokens;
    }

    private String extractUsername(Entity entity) {
        if (entity.contains("username")) {
            return entity.getString("username");
        }

        if (entity.contains("userId")) {
            UserRepository userRepository = new UserRepository();
            User user = userRepository.getUserById(entity.getString("userId"));

            if (user != null) {
                return user.getUsername();
            }
        }

        return null;
    }

    private Long extractTimestamp(Entity entity, String fieldName) {
        try {
            return entity.getLong(fieldName);
        } catch (RuntimeException ignored) {
            if (entity.contains(fieldName)) {
                try {
                    return Long.parseLong(entity.getString(fieldName));
                } catch (NumberFormatException ignoredToo) {
                    try {
                        return Instant.parse(entity.getString(fieldName)).getEpochSecond();
                    } catch (RuntimeException ignoredThree) {
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
