package network;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by robertsami on 10/6/14.
 */
public class UserInfoService {

    private static HashMap<ParseUser, UserInfo> fileMap;
    private static final String info_map = "USER_TO_FILE_MAP";
    private static final String info_map_key = "USER_TO_FILE_MAP";

    public UserInfoService() {
        // TODO: a lot of this
        ParseQuery<ParseObject> query = ParseQuery.getQuery(info_map);
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (!object.containsKey(info_map_key)) {
                        fileMap = new HashMap<ParseUser, UserInfo>();
                    } else {
                        fileMap = (HashMap<ParseUser, UserInfo>) object.get(info_map_key);
                    }
                } else {
                    // something went wrong
                }
            }
        });
    }

    public UserInfo getUserInfo(ParseUser u) {
        return fileMap.get(u);
    }

    public boolean saveUserInfo(ParseUser u, UserInfo i) {
        fileMap.put(u, i);
        return true;
    }

    public boolean doesUserExist(ParseUser u) {
        return fileMap.containsKey(u);
    }

    public boolean deleteUserInfo(ParseUser u) {
        if (doesUserExist(u)) {
            fileMap.remove(u);
        }
        return true;
    }

    // TODO: make this more efficient, i.e. don't use contains
    public List<ParseUser> getRandomUsers(int numUsers) {
        List<ParseUser> keysAsArray = new ArrayList<ParseUser>(fileMap.keySet());
        Random r = new Random();

        List<ParseUser> newUsers = new ArrayList<ParseUser>();
        while (newUsers.size() < numUsers) {
            ParseUser u = keysAsArray.get(r.nextInt(keysAsArray.size()));
            if (!keysAsArray.contains(u)) {
                newUsers.add(u);
            }
        }
        return newUsers;
    }
}
