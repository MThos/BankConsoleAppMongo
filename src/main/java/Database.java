import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class Database {
    static String connectionString = "mongodb://127.0.0.1:27017/bankapp?directConnection=true`";
    static String dbName = "bankapp";
    static String collectionName = "user";

    public static void findUser(String name, User _USER) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = collection.find(eq("name", name)).first();

            if (doc != null) {
                _USER.setName(doc.getString("name"));
                System.out.println("Welcome back, " + _USER.getName());
                System.out.println();
            } else {
                System.out.println("We could have find the user " + name);
                _USER.setName(createUser(name));
                System.out.println("We have created a new user: " + _USER.getName());
                System.out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String createUser(String name) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("name", name)
                    .append("chequing", 0)
                    .append("savings", 0));
            return name;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static double getChequingBalance(String name) {

        return 0;
    }
}
