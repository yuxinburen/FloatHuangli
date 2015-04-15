package com.davie.huangli;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;
import com.davie.message.ChetMessage;

import java.util.LinkedList;

/**
 * User: davie
 * Date: 15-4-15
 */
public class ChatActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListView chatListView = (ListView) findViewById(R.id.chat_list);
        LinkedList<ChetMessage> messages = new LinkedList<ChetMessage>();

        for (int i = 0; i <100 ; i++) {
            ChetMessage message  = new ChetMessage();
            message.setContent("你好"+i);
            message.setSendTime(System.currentTimeMillis());
            if(i%3==0){
                message.setFrom("GaGa");
                message.setTo("Lily");
            }else {
                message.setFrom("Lily");
                message.setTo("GaGa");
            }
            messages.add(message);
        }

        ChatAdapter adapter = new ChatAdapter(this, messages);
        chatListView.setAdapter(adapter);
    }
}