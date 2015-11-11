package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation;

/**
 * Created by hongjuhae on 2015. 10. 25..
 */
public class UserList {

    String id = null;
    String name = null;
    boolean selected = false;

    public UserList(String id, String name, boolean selected) {
        super();
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}