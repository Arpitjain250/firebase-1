package com.icu;

import com.icu.database.Message;

import java.util.List;

/**
 * Created by sitaram on 10/6/16.
 */

public interface DataListener {
    void onMessageReceived(Message messages);
}
