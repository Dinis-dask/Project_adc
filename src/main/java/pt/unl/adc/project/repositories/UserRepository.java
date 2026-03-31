package pt.unl.adc.project.repositories;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import pt.unl.adc.project.model.User;

import java.util.ArrayList;
import java.util.List;
/** 
 * Esta classe é responsável por gerir os Users na base de dados.
 */
public class UserRepository {

    private static final String KIND = "User";

    private final Datastore datastore;
    /**
     * Construtor que inicializa a ligação ao Datastore.
     */
    public UserRepository() {
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }
     /**
     * Verifica se já existe um utilizador com o username indicado.
     */
    public boolean exists(String username) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(username);
        return datastore.get(key) != null;
    }
    /**
     * Obtém um utilizador a partir do username.
     */
    public User getUser(String username) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(username);
        Entity entity = datastore.get(key);

        if (entity == null) {
            return null;
        }

        return new User(
                entity.getString("userId"),
                entity.getString("username"),
                entity.getString("password"),
                entity.getString("email"),
                entity.getString("phone"),
                entity.getString("address"),
                entity.getString("role")
        );
    }
    /**
     * Adiciona um novo utilizador à base de dados.
     */
    public void addUser(User user) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(user.getUsername());

        Entity entity = Entity.newBuilder(key)
                .set("userId", user.getUserId())
                .set("username", user.getUsername())
                .set("password", user.getPassword())
                .set("email", user.getEmail())
                .set("phone", user.getPhone())
                .set("address", user.getAddress())
                .set("role", user.getRole())
                .build();

        datastore.put(entity);
    }
    /**
     * Obtém todos os utilizadores registados na base de dados.
     */
    public List<User> getAllUsers() {
        EntityQuery query = Query.newEntityQueryBuilder().setKind(KIND).build();
        QueryResults<Entity> results = datastore.run(query);
        List<User> users = new ArrayList<>();

        while (results.hasNext()) {
            Entity entity = results.next();
            users.add(new User(
                    entity.getString("userId"),
                    entity.getString("username"),
                    entity.getString("password"),
                    entity.getString("email"),
                    entity.getString("phone"),
                    entity.getString("address"),
                    entity.getString("role")
            ));
        }

        return users;
    }
    /**
     * Procura um utilizador através do userId.
     */
    public User getUserById(String userId) {
        EntityQuery query = Query.newEntityQueryBuilder()
                .setKind(KIND)
                .setFilter(PropertyFilter.eq("userId", userId))
                .build();
        QueryResults<Entity> results = datastore.run(query);

        if (!results.hasNext()) {
            return null;
        }

        Entity entity = results.next();
        return new User(
                entity.getString("userId"),
                entity.getString("username"),
                entity.getString("password"),
                entity.getString("email"),
                entity.getString("phone"),
                entity.getString("address"),
                entity.getString("role")
        );
    }
    /**
     * Remove um utilizador da base de dados através do userId.
     */
    public void deleteUserById(String userId) {
        User user = getUserById(userId);

        if (user != null) {
            Key key = datastore.newKeyFactory().setKind(KIND).newKey(user.getUsername());
            datastore.delete(key);
        }
    }
    /**
     * Atualiza os dados de um utilizador na base de dados.
     */
    public void updateUser(User user) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(user.getUsername());

        Entity entity = Entity.newBuilder(key)
                .set("userId", user.getUserId())
                .set("username", user.getUsername())
                .set("password", user.getPassword())
                .set("email", user.getEmail())
                .set("phone", user.getPhone())
                .set("address", user.getAddress())
                .set("role", user.getRole())
                .build();

        datastore.put(entity);
    }
}
