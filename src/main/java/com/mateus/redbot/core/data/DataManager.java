package com.mateus.redbot.core.data;

import com.mateus.redbot.core.config.ConfigManager;
import com.mateus.redbot.core.permissions.UserPermission;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bson.conversions.Bson;

public class DataManager {
    private static DataManager instance;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) instance = new DataManager();
            }
        }
        return instance;
    }

    public void setup() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://<username>:<password>@cluster0-zsnfd.gcp.mongodb.net/test?retryWrites=true&w=majority"
                        .replace("<username>", (CharSequence) ConfigManager.getInstance().getConfig().get("mongodb-username"))
                        .replace("<password>", (CharSequence) ConfigManager.getInstance().getConfig().get("mongodb-password"))
        );
        MongoClient mongoClient = new MongoClient(uri);
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoClient.getDatabase("redbotdb");
    }
    public void addUserMoney(User user, long money) {
        if (containsUserMoney(user)) return;
        MongoCollection<Document> collection = mongoDatabase.getCollection("userMoney");
        Document document = new Document("userID", user.getId());
        document.append("money", money);
        collection.insertOne(document);
    }
    public boolean containsUserMoney(User user) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("userMoney");
        FindIterable<Document> iterable = collection.find(new Document("userID", user.getId()));
        return iterable.first() != null;
    }
    public void addUserPermission(UserPermission userPermission, User user, Guild guild) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("userGuildPerms");
        Document document = new Document("guildID", guild.getId());
        document.append("userID", user.getId());
        document.append("perm", userPermission.toString());
        collection.insertOne(document);
    }
    public UserPermission getUserGuildPermission(User user, Guild guild) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("userGuildPerms");
        FindIterable<Document> iterable = collection.find(new Document("guildID", guild.getId()));
        iterable = iterable.filter(new Document("userID", user.getId()));
        return UserPermission.valueOf((String) iterable.first().get("perm"));
    }
    public boolean containsUserGuildPermission(User user, Guild guild) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("userGuildPerms");
        FindIterable<Document> iterable = collection.find(new Document("guildID", guild.getId()));
        iterable = iterable.filter(new Document("userID", user.getId()));
        return iterable.first() != null;
    }
    public void updateUserPermission(UserPermission userPermission,User user, Guild guild){
        if (!containsUserGuildPermission(user, guild)) return;
        MongoCollection<Document> collection = mongoDatabase.getCollection("userGuildPerms");
        Document document = collection.find(new Document("guildID", guild.getId())).filter(new Document("userID", user.getId())).first();
        Bson updateValue = new Document("perm",userPermission.toString());
        Bson updateOperation = new Document("$set", updateValue);
        collection.updateOne(document, updateOperation);
    }
    public void updateUserMoney(User user, long money) {
        if (!containsUserMoney(user)) return;
        MongoCollection<Document> collection = mongoDatabase.getCollection("userMoney");
        Document document = collection.find(new Document("userID", user.getId())).first();
        Bson updateMoney = new Document("money", money);
        Bson updateOperation = new Document("$set", updateMoney);
        collection.updateOne(document, updateOperation);
    }
    public long getUserMoney(User user) {
        if (!containsUserMoney(user)) return 0;
        MongoCollection<Document> collection = mongoDatabase.getCollection("userMoney");
        Document document = collection.find(new Document("userID", user.getId())).first();
        return (long) document.get("money");
    }
}

