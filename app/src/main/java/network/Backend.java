package network;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Jacob on 10/5/14.
 */
public class Backend {
    private UserInfoService userInfoService;

    public Backend() {
        userInfoService = new UserInfoService();
    }

    private String getCanonicalUserId(User u) {
        return null;
    }

    public static ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }

    public void deleteFiles(ParseUser user) {
        if (!userInfoService.doesUserExist(user)) {
            return;
        }
        UserInfo i = userInfoService.getUserInfo(user);
        i.deleteInBackground();
        userInfoService.deleteUserInfo(user);
    }

    // TODO: look for collisions
    private static String generateRandomFileName() {
        Random r = new Random();
        // TODO: format this string to fix the width of integers
        String s = "guid_" + r.nextInt();
        return s;
    }

    private static String generateUserFileName(ParseUser u) {
        return generateRandomFileName();
    }

    public void saveFiles(ParseUser user, File picture, File voice) {
        UserInfo info = userInfoService.doesUserExist(user)
                ? userInfoService.getUserInfo(user) : new UserInfo();

        String pictureFileName = generateRandomFileName();
        String voiceFileName   = generateRandomFileName();
        info.setPictureFileName(pictureFileName);
        info.setPictureFileName(voiceFileName);
        // fragile since we're assuming we can overwrite shit but w/e
        userInfoService.saveUserInfo(user, info);

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

    private File getPicture(ParseUser user) {
        String pathname = generateRandomFileName();
        final File f = new File(pathname);

        if (!userInfoService.doesUserExist(user)) {
            return null;
            // TODO: should throw exception
        }
        UserInfo i = userInfoService.getUserInfo(user);
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

    private File getVoice(ParseUser user) {
        String pathname = generateRandomFileName();
        final File f = new File(pathname);

        if (!userInfoService.doesUserExist(user)) {
            return null;
            // TODO: should throw exception
        }
        UserInfo i = userInfoService.getUserInfo(user);
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

    public FilePair getUserFiles(ParseUser user) {
        FilePair pair = new FilePair();
        pair.pictureFile = getPicture(user);
        pair.voiceFile   = getVoice(user);
        if (pair.pictureFile == null || pair.voiceFile == null) {
            return null;
            // TODO: catch below exception and throw another one
        }
        return pair;
    }

    public List<ParseUser> getRandomUsers(int numUsers) {
        return userInfoService.getRandomUsers(numUsers);
    }
}

