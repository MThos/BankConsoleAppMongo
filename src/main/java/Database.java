import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Objects;

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
                System.out.println("We could not find the user: " + name);
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

        return "";
    }

    public static void withdraw(String name, String accountType, double amount) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = new Document().append("name", name);
            Bson update = Updates.set(Main.getAccountType(accountType), getBalance(name, accountType) - amount);

            if (doc != null) {
                collection.updateOne(doc, update);
            } else {
                System.out.println("There was an issue withdrawing your money.");
            }
        }
    }

    public static void deposit(String name, String accountType, double amount) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = new Document().append("name", name);
            Bson update = Updates.set(Main.getAccountType(accountType), getBalance(name, accountType) + amount);

            if (doc != null) {
                collection.updateOne(doc, update);
            } else {
                System.out.println("There was an issue depositing your money.");
            }
        }
    }

    public static double getBalance(String name, String accountType) {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document doc = collection.find(eq("name", name)).first();

            if (doc != null) {
                return (Objects.equals(accountType, "C")) ?
                        doc.getDouble("chequing") : doc.getDouble("savings");
            }
        }

        return 0;
    }
}
