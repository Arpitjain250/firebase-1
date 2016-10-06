package com.icu.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.icu.utils.MessageListAdapter;
import com.icu.R;
import com.icu.database.Message;
import com.icu.database.MessageRepository;

import java.util.List;

/**
 * Created by sitaram on 10/7/16.
 */

public class MessageListActivity extends AppCompatActivity {

    private MessageListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        RecyclerView messageList = (RecyclerView) findViewById(R.id.messageList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageList.setLayoutManager(manager);
        adapter = new MessageListAdapter(this);
        messageList.setAdapter(adapter);
        updateMessages();

    }

    private void updateMessages() {
        List<Message> messages = MessageRepository.loadAllMessages(this);
        if (messages != null && messages.size() > 0) {
            adapter.updateMessages(messages);
        }
    }
}
