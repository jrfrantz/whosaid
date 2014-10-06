package network;
import com.parse.*;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 10/5/14.
 */
public class Backend {
    private HashMap<User, UserInfo> fileMap;

    private static final String info_map = "USER_TO_FILE_MAP";

    public Backend() {
        // TODO: a lot of this
        ParseQuery<ParseObject> query = ParseQuery.getQuery(info_map);
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });
    }

    public void deleteFiles(User user) {
        if (!fileMap.containsKey(user)) {
            return;
        }
        UserInfo i = fileMap.get(user);
        i.deleteInBackground();
        fileMap.remove(user);
    }

    // TODO: look for collisions
    private String generateRandomFileName() {
        Random r = new Random();
        // TODO: format this string to fix the width of integers
        String s = "guid_" + r.nextInt();
        return s;
    }

    public void saveFiles(User user, File picture, File voice) {
        UserInfo info = fileMap.containsKey(user) ? fileMap.get(user) : new UserInfo();

        String pictureFileName = generateRandomFileName();
        String voiceFileName   = generateRandomFileName();
        info.setPictureFileName(pictureFileName);
        info.setPictureFileName(voiceFileName);

        byte[] pictureFileBytes = null;
        try {
            pictureFileBytes = FileUtils.readFileToByteArray(picture);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        byte[] voiceFileBytes = null;
        try {
            voiceFileBytes = FileUtils.readFileToByteArray(voice);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        ParseFile pictureFile = new ParseFile(pictureFileName, pictureFileBytes);
        ParseFile voiceFile   = new ParseFile(voiceFileName, voiceFileBytes);

        pictureFile.saveInBackground();
        voiceFile.saveInBackground();
    }

    private void writeBytesToFile(File f, byte[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getPicture(User user) {
        String pathname = generateRandomFileName();
        final File f = new File(pathname);

        if (!fileMap.containsKey(user)) {
            return null;
            // TODO: should throw exception
        }
        UserInfo i = fileMap.get(user);
        ParseFile pictureFile = (ParseFile)i.getPictureFile();
        pictureFile.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // data has the bytes for the file
                    writeBytesToFile(f, data);
                } else {
                    // something went wrong
                    // TODO: frown
                }
            }
        });
        return f;
    }

    private File getVoice(User user) {
        String pathname = generateRandomFileName();
        final File f = new File(pathname);

        if (!fileMap.containsKey(user)) {
            return null;
            // TODO: should throw exception
        }
        UserInfo i = fileMap.get(user);
        ParseFile pictureFile = (ParseFile)i.getVoiceFile();
        pictureFile.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // data has the bytes for the file
                    writeBytesToFile(f, data);
                } else {
                    // something went wrong
                    // TODO: frown
                }
            }
        });
        return f;
    }

    public FilePair getUserFiles(User user) {
        FilePair pair = new FilePair();
        pair.pictureFile = getPicture(user);
        pair.voiceFile   = getVoice(user);
        if (pair.pictureFile == null || pair.voiceFile == null) {
            return null;
            // TODO: catch below exception and throw another one
        }
        return pair;
    }

    // TODO: make this more efficient, i.e. don't use contains
    public List<User> getRandomUsers(int numUsers) {
        List<User> keysAsArray = new ArrayList<User>(fileMap.keySet());
        Random r = new Random();

        List<User> newUsers = new ArrayList<User>();
        while (newUsers.size() < numUsers) {
            User u = keysAsArray.get(r.nextInt(keysAsArray.size()));
            if (!keysAsArray.contains(u)) {
                newUsers.add(u);
            }
        }
        return newUsers;
    }
}
