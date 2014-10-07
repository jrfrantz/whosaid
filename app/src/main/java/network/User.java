package network;

/**
 * Created by robertsami on 10/5/14.
 */
public class User {
    private String id;
    private String fileID;

    public User() {
        fileID = "user_id_" + id;
    }

    public String getID() {
        return id;
    }

    public String getParseKey() {
        return id;
    }

    public String getFileID() {
        return fileID;
    }
}
