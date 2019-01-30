package com.safedoctor.safedoctor.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.safedoctor.safedoctor.Model.ChatMessage;
import com.safedoctor.safedoctor.Model.SignalMessage;
import com.safedoctor.safedoctor.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.formateDate;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getMessageTime;
import static com.safedoctor.safedoctor.Utils.AuxUtils.Date.getReadabledate;

/**
 * Created by Alvin on 16/08/2017.
 */

public class SignalMessageAdapter  extends ArrayAdapter<SignalMessage> {

    private String TAG="SignalMessageAdapter";
    private Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    public static final int VIEW_TYPE_LOCAL = 0;
    public static final int VIEW_TYPE_REMOTE = 1;
    private static final Map<Integer, Integer> viewTypes;
    static {
        Map<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(VIEW_TYPE_LOCAL, R.layout.right_chat_item);
        aMap.put(VIEW_TYPE_REMOTE, R.layout.left_chat_item);
        viewTypes = Collections.unmodifiableMap(aMap);
    }

    public SignalMessageAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = LayoutInflater.from(getContext()).inflate(viewTypes.get(type), null);
        }
        TextView messageTextView = (TextView)convertView.findViewById(R.id.message_text);
        TextView sendertxt=(TextView)convertView.findViewById(R.id.sender);
        TextView timetxt=(TextView)convertView.findViewById(R.id.time) ;
        SignalMessage message = getItem(position);
        ImageView profile_picture=(ImageView)convertView.findViewById(R.id.profile_picture);

        try{
            parser.parse(message.getMessageText());
            Log.e(TAG,"Chat message "+gson.fromJson(message.getMessageText(),ChatMessage.class).toString());
            ChatMessage chatMessage=gson.fromJson(message.getMessageText(),ChatMessage.class);
            if (messageTextView != null) {
                messageTextView.setText(chatMessage.getMsg());
            }

            sendertxt.setText(chatMessage.getSender());

            Log.e(TAG," is remote? "+message.isRemote());

            if(message.isRemote()){

                if(chatMessage.getMobile()==null ){
                    timetxt.setText(formateDate(chatMessage.getTime(),"MMM dd yyyy h:mm a"));
                }else{
                    timetxt.setText(getMessageTime(chatMessage.getTime()));
                }

            }else{//local
                if(chatMessage.isFromserver()){
                    timetxt.setText(formateDate(chatMessage.getTime(),"MMM dd yyyy h:mm a"));
                }else{
                    timetxt.setText(getMessageTime(chatMessage.getTime()));
                }

            }
            timetxt.setVisibility(View.VISIBLE);
            sendertxt.setVisibility(View.VISIBLE);


            if(chatMessage.getPic() != null && !chatMessage.getPic().isEmpty())
            {
                byte[] imageBytes = Base64.decode(chatMessage.getPic(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                profile_picture.setImageBitmap(decodedImage);
            }
            profile_picture.setVisibility(View.GONE);
        }
        catch (Exception e){

            Log.e(TAG,""+e.getMessage());

            //Toast.makeText(getContext(),"Json exception thrown",Toast.LENGTH_LONG).show();
            if (messageTextView != null) {
                messageTextView.setText(message.getMessageText());
            }
            profile_picture.setVisibility(View.GONE);
            timetxt.setText(getMessageTime(getReadabledate()));
            timetxt.setVisibility(View.VISIBLE);
            sendertxt.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        SignalMessage message = getItem(position);
        Log.e(TAG,""+message.isRemote());
        return message.isRemote() ? VIEW_TYPE_REMOTE : VIEW_TYPE_LOCAL;
    }

    @Override
    public int getViewTypeCount() {
        return viewTypes.size();
    }

}
