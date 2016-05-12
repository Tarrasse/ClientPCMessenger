package Users;

/**
 * Created by mahmoud on 5/10/2016.
 */
public abstract class User {
    private String name ;
    private int gender;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
