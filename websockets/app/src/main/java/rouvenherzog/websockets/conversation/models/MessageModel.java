package rouvenherzog.websockets.conversation.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rouvenherzog on 30.01.15.
 */
public class MessageModel {
    private String text;
    private String author;

    public MessageModel(JSONObject data) throws JSONException {
        this.text = data.getString("text");
        this.author = data.getString("author");
    }

    @Override
    public String toString() {
        return this.getText();
    }

    public String getText() {
        return this.text;
    }

    public String getAuthor() { return this.author; }
}
