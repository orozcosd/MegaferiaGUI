package core.models.storage;

import core.models.Stand;
import java.util.ArrayList;

public class StandStorage {

    private static StandStorage instance;

    private ArrayList<Stand> stands;

    private StandStorage() {
        this.stands = new ArrayList<>();
    }

    public static StandStorage getInstance() {
        if (instance == null) {
            instance = new StandStorage();
        }
        return instance;
    }

    public boolean addStand(Stand stand) {
        for (Stand s : this.stands) {
            if (s.getId() == stand.getId()) {
                return false;
            }
        }
        this.stands.add(stand);
        return true;
    }

    public Stand getStand(long id) {
        for (Stand stand : this.stands) {
            if (stand.getId() == id) {
                return stand;
            }
        }
        return null;
    }

    public ArrayList<Stand> getAllStands() {
        return new ArrayList<>(this.stands);
    }

    public boolean deleteStand(long id) {
        for (Stand stand : this.stands) {
            if (stand.getId() == id) {
                this.stands.remove(stand);
                return true;
            }
        }
        return false;
    }

}
