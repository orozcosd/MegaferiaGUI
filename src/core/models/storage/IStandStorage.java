package core.models.storage;

import core.models.Stand;
import java.util.ArrayList;

public interface IStandStorage {

    boolean addStand(Stand stand);

    Stand getStand(long id);

    ArrayList<Stand> getAllStands();

    boolean deleteStand(long id);

}
