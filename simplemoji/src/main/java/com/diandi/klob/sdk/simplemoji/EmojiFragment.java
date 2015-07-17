package com.diandi.klob.sdk.simplemoji;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;


/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2015-07-17  .
 * *********    Time : 17:08 .
 * *********    Version : 1.0
 * *********    Copyright © 2015, klob, All Rights Reserved
 * *******************************************************************************
 */

public class EmojiFragment extends Fragment {

    private View mRootView;
    private Emojicon[] mData;
    private EmojiLayout mEmojiLayout;
    private LayoutInflater mInflater;

    public void init(EmojiLayout emojiLayout, Emojicon[] data) {
        mEmojiLayout = emojiLayout;
        mData = data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mRootView = inflater.inflate(R.layout.emojicon_grid, container, false);
        GridView gridView = (GridView) mRootView.findViewById(R.id.Emoji_GridView);
        EmojiAdapter mAdapter = new EmojiAdapter(mRootView.getContext(), mData);
        gridView.setAdapter(mAdapter);
        mAdapter.setEmojiClickListener(new EmojiLayout.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                mEmojiLayout.onEmojiClick(emojicon);
            }
        });
        mAdapter.setBackspaceClickedListener(new EmojiLayout.OnEmojiconBackspaceClickedListener() {
            @Override
            public void onEmojiconBackspaceClicked(View v) {
                mEmojiLayout.onBackspace();
            }
        });
        return mRootView;

    }

    class EmojiAdapter extends ArrayAdapter<Emojicon> {
        EmojiLayout.OnEmojiconClickedListener emojiClickListener;
        EmojiLayout.OnEmojiconBackspaceClickedListener mBackspaceClickedListener;

        public void setBackspaceClickedListener(EmojiLayout.OnEmojiconBackspaceClickedListener backspaceClickedListener) {
            mBackspaceClickedListener = backspaceClickedListener;
        }

        public EmojiAdapter(Context context, List<Emojicon> data) {
            super(context, R.layout.emojicon_item, data);
        }

        public EmojiAdapter(Context context, Emojicon[] data) {
            super(context, R.layout.emojicon_item, data);
        }

        public void setEmojiClickListener(EmojiLayout.OnEmojiconClickedListener listener) {
            this.emojiClickListener = listener;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = View.inflate(getContext(), R.layout.emojicon_item, null);
                ViewHolder holder = new ViewHolder();
                holder.icon = (TextView) v.findViewById(R.id.emojicon_icon);
                v.setTag(holder);
            }
            Emojicon emoji = getItem(position);
            ViewHolder holder = (ViewHolder) v.getTag();

            if (position == 20 || getCount() < 10 && position == 3 ) {
                holder.icon.setBackgroundResource(R.drawable.ic_keyboard_delete);
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackspaceClickedListener.onEmojiconBackspaceClicked(v);
                    }
                });
            } else {
                holder.icon.setText(emoji.getEmoji());
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emojiClickListener.onEmojiconClicked(getItem(position));
                    }
                });
            }

            return v;
        }

        class ViewHolder {
            TextView icon;
        }
    }

}
