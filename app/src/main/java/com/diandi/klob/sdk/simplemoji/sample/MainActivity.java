package com.diandi.klob.sdk.simplemoji.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.diandi.klob.sdk.simplemoji.EmojiLayout;
import com.diandi.klob.sdk.simplemoji.sample.R;

/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2015-07-17  .
 * *********    Time : 17:27 .
 * *********    Version : 1.0
 * *********    Copyright © 2015, klob, All Rights Reserved
 * *******************************************************************************
 */

public class MainActivity extends FragmentActivity {
     EmojiLayout emojiLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.lv);
        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.listview_row_layout);
        lv.setAdapter(mAdapter);
        emojiLayout=new EmojiLayout(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = emojiLayout.getContent();
                Log.e("main",input);
                mAdapter.add(input);
                mAdapter.notifyDataSetChanged();
            }
        });

    }


}
