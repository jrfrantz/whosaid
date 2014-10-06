package network;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {
    public String getPictureFileName() {
        return getString("pictureFileName");
    }
    public void setPictureFileName(String value) {
        put("pictureFileName", value);
    }
    public String getVoiceFileName() {
        return getString("voiceFileName");
    }
    public void setVoiceFileName(String value) {
        put("voiceFile", value);
    }
    public ParseFile getPictureFile() {
        return getParseFile("voiceFile");
    }
    public void setPictureFile(ParseFile value) {
        put("voiceFile", value);
    }
    public ParseFile getVoiceFile() {
        return getParseFile("pictureFile");
    }
    public void setVoiceFile(ParseFile value) {
        put("pictureFile", value);
    }
}
