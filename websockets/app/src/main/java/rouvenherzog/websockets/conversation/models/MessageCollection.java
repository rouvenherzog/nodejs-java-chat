package rouvenherzog.websockets.conversation.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import rouvenherzog.websockets.R;

/**
 * Created by rouvenherzog on 30.01.15.
 */
public class MessageCollection extends ArrayAdapter {
    public MessageCollection(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        View result;
        if( convertView != null ) {
            result = convertView;
        } else {
            result = LayoutInflater
                    .from(getContext()
                    ).inflate(R.layout.fragment_message, parent, false);
        }

        MessageModel message = (MessageModel) getItem(position);
        TextView author = (TextView) result.findViewById(R.id.message_author);
        author.setText(message.getAuthor());
        TextView text = (TextView) result.findViewById(R.id.message_text);
        text.setText(message.getText());

        return result;
    }
}
