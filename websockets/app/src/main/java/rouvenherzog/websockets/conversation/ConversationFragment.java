package rouvenherzog.websockets.conversation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import rouvenherzog.websockets.R;
import rouvenherzog.websockets.conversation.models.MessageCollection;

/**
 * Created by rouvenherzog on 30.01.15.
 */
public class ConversationFragment extends Fragment {
    MessageCollection messages;
    ConversationSocket socket;
    Thread t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.messages = new MessageCollection(getActivity().getApplicationContext(), R.layout.fragment_message);
        this.socket = new ConversationSocket(messages, getActivity());
        this.t = new Thread(this.socket);
        this.t.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        ListView messages_list = (ListView) rootView.findViewById(R.id.message_list);
        messages_list.setAdapter(messages);

        final EditText message_text = (EditText) rootView.findViewById(R.id.message_content);
        Button message_send = (Button) rootView.findViewById(R.id.message_send);
        message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(message_text.getText().toString());
                message_text.setText("");
            }
        });


        return rootView;
    }
}
