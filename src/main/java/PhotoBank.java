import java.util.Random;
import java.util.ArrayList;

public class PhotoBank {
    ArrayList<String> missList;
    ArrayList<String> loveList;
    Random rng = new Random();

    public PhotoBank() {
        missList = Firebase.getList("miss");
        loveList = Firebase.getList("love");
    }

    public String getRandom() {
        int random = rng.nextInt(2);
        if (random == 1) {
            return getRandomMiss();
        } else {
            return getRandomLove();
        }
    }

    public String getRandomMiss() {
        return missList.get(rng.nextInt(missList.size()));
    }

    public String getRandomLove() {
        return loveList.get(rng.nextInt(loveList.size()));
    }

    public boolean addImage(String s, String f_id) {
        if (!s.equals("miss") && !s.equals("love")) {
            return false;
        }
        Firebase.addImage(s, f_id);

        if (s == "miss") {
            missList.add(f_id);
        } else {
            loveList.add(f_id);
        }
        return true;
    }
}
