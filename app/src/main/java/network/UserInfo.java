package network;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {
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
