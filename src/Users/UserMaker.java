package Users;

/**
 * Created by mahmoud on 5/10/2016.
 */
public class UserMaker {

    private boolean canCreate = true;

    public User makeUser (boolean type){
        if (canCreate){
            if (type){
                canCreate = false;
                return new MaleUser();
            }else{
                canCreate = false;
                return new FemaleUser();
            }
        }
        return null;
    }
}
