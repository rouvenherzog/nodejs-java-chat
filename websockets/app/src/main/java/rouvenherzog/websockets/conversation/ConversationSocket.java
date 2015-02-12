package rouvenherzog.websockets.conversation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import rouvenherzog.websockets.conversation.models.MessageCollection;
import rouvenherzog.websockets.conversation.models.MessageModel;

/**
 * Created by rouvenherzog on 30.01.15.
 */
public class ConversationSocket implements Runnable {
    private MessageCollection adapter;
    private Activity context;
    private Socket socket;

    public ConversationSocket( MessageCollection adapter, Activity context ) {
        this.adapter = adapter;
        this.context = context;
    }

    public void sendMessage(String m) {
        if( m.trim().length() == 0 )
            return;

        try {
            Log.d("SOCKET", "SENDING STRING " + m );
            new PrintWriter(this.socket.getOutputStream(), true).println(m);
        } catch( Exception e ) {
            Log.e("SOCKET", e.toString());
        }
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket("192.168.0.7", 8027);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while( (line = input.readLine()) != null ) {
                Log.d("SOCKET", "RECEIVED " + line);
                this.context.runOnUiThread(new AddToList(line));
            }

            Log.d("GOT", "EOF");
        } catch( Exception e ) {
            Log.e(
                    "SOCKET", "READING INPUT: " + e.toString());
        }
    }

    private class AddToList implements Runnable {
        private MessageModel[] messages;

        public AddToList( String line ) {
            try {
                JSONArray data = new JSONArray(line);
                this.messages = new MessageModel[data.length()];
                for( int i = 0; i < data.length(); i++ )
                    this.messages[i] = new MessageModel((JSONObject) data.get(i));
            } catch( JSONException e ) {
                Log.e("SOCKET", e.toString());
            }
        }

        @Override
        public void run() {
            Log.d("SOCKET", "ADDING " + this.messages.length + " MESSAGES.");
            for( MessageModel message : this.messages )
                adapter.add(message);

            adapter.notifyDataSetChanged();
        }
    }
}
