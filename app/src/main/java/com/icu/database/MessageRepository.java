package com.icu.database;

import android.content.Context;

import com.icu.ApplicationLoader;

import java.util.List;

/**
 * Created by sitaram on 10/6/16.
 */

public class MessageRepository {
    public static void insertOrUpdate(Context context, Message button) {
        getMessageDao(context).insertOrReplace(button);
    }

    public static List<Message> loadAllMessages(Context context) {
        return getMessageDao(context).loadAll();
    }

    private static MessageDao getMessageDao(Context c) {
        return ((ApplicationLoader) c.getApplicationContext()).getDaoSession().getMessageDao();
    }
}
