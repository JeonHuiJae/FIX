/**
 * Created by hhj73 on 2018-04-17.
 */

public class User {

    boolean type; // true: 노인, false: 학생
    String id;
    String pw;
    String name;
    boolean smoking;
    boolean curfew;
    boolean pet;
    int cost;
    boolean help;
    String address;
    String uniqueness;

    public User(boolean type, String id, String pw, String name, boolean smoking, boolean curfew,
                boolean pet, int cost, boolean help, String address, String uniqueness) {
        this.type = type;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.smoking = smoking;
        this.curfew = curfew;
        this.pet = pet;
        this.help = help;
        this.uniqueness = uniqueness;

        if(type) {
            // 노인 회원
            this.cost = cost;
            this.address = address;
        } else {
            this.cost = -1;
            this.address = null;
        }
    }
}
