package json_toolkit;

import org.bson.types.ObjectId;

public class User {
    private ObjectId _id;
    private String username;
    private String password;
    private double credits;

    public User (ObjectId _id, String username, String password, double credits) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.credits = credits;
    }

    public User(){}

    public ObjectId get_id() {
        return _id;
    }

    //public void set_id(ObjectId _id) {this._id = _id;}

    public double getCredits() {
        return credits;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
