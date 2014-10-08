package network;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by robertsami on 10/6/14.
 */
public class UserInfoService {
    // TODO(robertsami): read this in statically
    private static final String userMapObjectId = "fRGHMuofaW";
    private ParseObject userMap;

    public UserInfoService() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserMapObject");
        try {
            userMap = query.get(userMapObjectId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserInfo(ParseUser u) {
        return (UserInfo) userMap.get(u.getUsername());
    }

    public boolean saveUserInfo(ParseUser u, UserInfo i) {
        userMap.add(u.getUsername(), i);
        return true;
    }

    public boolean doesUserExist(ParseUser u) {
        boolean b = userMap.containsKey(u.getUsername());
        return b;
    }

    public boolean deleteUserInfo(ParseUser u) {
        if (doesUserExist(u)) {
            userMap.remove(u.getUsername());
        }
        return true;
    }

    // TODO: make this more efficient, i.e. don't use contains
    public List<UserInfo> getRandomFiles(int numUsers) {
        String[] keysAsArray = (String[]) userMap.keySet().toArray();
        Random r = new Random();
        Set<Integer> seenUsers = new HashSet<Integer>();
        List<UserInfo> newUsers = new ArrayList<UserInfo>();
        while (newUsers.size() < numUsers) {
            int index = r.nextInt(keysAsArray.length);
            if (!seenUsers.contains(index)) {
                seenUsers.add(index);
                newUsers.add((UserInfo)userMap.get(keysAsArray[index]));
            }
        }
        return newUsers;
    }
}
