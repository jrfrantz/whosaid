package network;
import com.parse.*;

//import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Jacob on 10/5/14.
 */
public class Backend {
    private HashMap<User, UserInfo> fileMap;

    private static final String info_map = "USER_TO_FILE_MAP";


    public Backend() {
        // read
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

    // TODO(rob): this
    public void deleteFiles(User user) {

    }

    // TODO(rob): this
    private String generateRandomFileName() {
        return null;
    }

    public void saveFiles(User user, File picture, File voice) {
        if (fileMap.containsKey(user)) {
            deleteFiles(user);
        }

        String pictureFileName = generateRandomFileName();
        String voiceFileName   = generateRandomFileName();
        UserInfo info = new UserInfo();
        info.setPictureFileName(pictureFileName);
        info.setPictureFileName(voiceFileName);

        // TODO: import commons io and use byte function
        byte[] pictureFileBytes = null;
        byte[] voiceFileBytes = null;

        ParseFile pictureFile = new ParseFile(pictureFileName, pictureFileBytes);
        ParseFile voiceFile   = new ParseFile(voiceFileName, voiceFileBytes);

        pictureFile.saveInBackground();
        voiceFile.saveInBackground();


    }

    private void writeBytesToFile(File f, byte[] data) {

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

    public User[] getRandomUsers(int numUsers) {
        return null;
    }
}

