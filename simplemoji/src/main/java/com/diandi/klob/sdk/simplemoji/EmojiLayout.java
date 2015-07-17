package com.diandi.klob.sdk.simplemoji;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * *******************************************************************************
 * *********    Author : klob(kloblic@gmail.com) .
 * *********    Date : 2015-07-17  .
 * *********    Time : 17:17 .
 * *********    Version : 1.0
 * *********    Copyright © 2015, klob, All Rights Reserved
 * *******************************************************************************
 */

public class EmojiLayout {
    private FragmentActivity mActivity;
    private EmojiconEditText mEmojiconEditText;
    private View emojiKeyboardLayout;
    private View rootView;
    private int rootViewHigh = 0;


    private TextView sendText;
    private ImageButton send;
    private CheckBox checkBoxEmoji;
    private LinearLayout emojiKeyboardIndicator;


    public EmojiLayout(FragmentActivity activity, View.OnClickListener onClickSend) {
        mActivity = activity;
        init(onClickSend);
    }

    public String getContent() {
        return mEmojiconEditText.getText().toString();
    }

    public void init(View.OnClickListener onClickSend) {
        initViews(onClickSend);
        initViewPager();
        bindEvent();
        updateSendButtonStyle();
    }

    private void initViews(View.OnClickListener onClickSend) {
        rootView = mActivity.findViewById(android.R.id.content);
        emojiKeyboardLayout = mActivity.findViewById(R.id.emojiKeyboardLayout);
        emojiKeyboardIndicator = (LinearLayout) mActivity.findViewById(R.id.emojiKeyboardIndicator);
        mEmojiconEditText = (EmojiconEditText) mActivity.findViewById(R.id.comment);
        checkBoxEmoji = (CheckBox) mActivity.findViewById(R.id.popEmoji);
        sendText = (TextView) mActivity.findViewById(R.id.sendText);
        send = (ImageButton) mActivity.findViewById(R.id.send);
        sendText.setVisibility(View.VISIBLE);
        sendText.setOnClickListener(onClickSend);
        send.setVisibility(View.GONE);
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) mActivity.findViewById(R.id.viewPager);
        viewPager.setAdapter(new EmojiPagerAdapter(mActivity.getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new PageChangeListener());


        setIndicatorCount(People.emojis.length);

    }

    private void bindEvent() {
        mEmojiconEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateSendButtonStyle();
            }
        });
        checkBoxEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiconEditText.requestFocus();
                if (checkBoxEmoji.isChecked()) {
                    rootViewHigh = rootView.getHeight();

                    final int bottomHigh = dpToPx(100);
                    int rootParentHigh = rootView.getRootView().getHeight();
                    if (rootParentHigh - rootViewHigh > bottomHigh) {

                        popSoftkeyboard(mActivity, mEmojiconEditText, false);


                        rootView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rootView.setLayoutParams(rootView.getLayoutParams());
                            }
                        }, 50);

                    } else {
                        emojiKeyboardLayout.setVisibility(View.VISIBLE);
                        rootViewHigh = 0;
                    }
                } else {
                    popSoftkeyboard(mActivity, mEmojiconEditText, true);
                    emojiKeyboardLayout.setVisibility(View.GONE);
                }
            }
        });


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (rootViewHigh == 0) {
                            return;
                        }

                        int high = rootView.getHeight();
                        if (high >= rootViewHigh) {
                            emojiKeyboardLayout.setVisibility(View.VISIBLE);
                            rootViewHigh = 0;
                        }
                    }
                }
        );
    }

    private void setIndicatorCount(int count) {
        emojiKeyboardIndicator.removeAllViews();
        int pointWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.point_width);
        int pointMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.point_margin);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pointWidth, pointWidth);
        lp.leftMargin = pointWidth;
        lp.rightMargin = pointMargin;
        for (int i = 0; i < count; ++i) {
            View pointView = mActivity.getLayoutInflater().inflate(R.layout.common_point, null);
            emojiKeyboardIndicator.addView(pointView, lp);
        }
        emojiKeyboardIndicator.getChildAt(0).setBackgroundResource(R.drawable.ic_point_select);


    }

    protected void updateSendButtonStyle() {
        if (sendButtonEnable()) {
            sendText.setBackgroundResource(R.drawable.edit_send_green);
            sendText.setTextColor(0xffffffff);
        } else {
            sendText.setBackgroundResource(R.drawable.edit_send);
            sendText.setTextColor(0xff999999);
        }
    }

    protected boolean sendButtonEnable() {
        return mEmojiconEditText.getText().length() > 0;
    }

    public void onEmojiClick(Emojicon emojicon) {
        if (mEmojiconEditText == null || emojicon == null) {
            return;
        }
        int start = mEmojiconEditText.getSelectionStart();
        int end = mEmojiconEditText.getSelectionEnd();
        if (start < 0) {
            mEmojiconEditText.append(emojicon.getEmoji());
        } else {
            mEmojiconEditText.getText().replace(Math.min(start, end),
                    Math.max(start, end), emojicon.getEmoji(), 0,
                    emojicon.getEmoji().length());
        }
    }

    public void onBackspace() {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        mEmojiconEditText.dispatchKeyEvent(event);
    }

    private static void popSoftkeyboard(Context ctx, View view, boolean wantPop) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (wantPop) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int dpToPx(int dpValue) {
        return (int) (dpValue * mActivity.getResources().getDisplayMetrics().density + 0.5f);
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }

    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View v);
    }

    public class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    protected class EmojiPagerAdapter extends FragmentStatePagerAdapter {

        EmojiPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            EmojiFragment fragment = new EmojiFragment();
            fragment.init(EmojiLayout.this, People.emojis[i]);
            return fragment;
        }

        @Override
        public int getCount() {
            return People.emojis.length;
        }
    }

    protected class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        int oldPos = 0;

        public void resetPos() {
            oldPos = 0;
        }

        @Override
        public void onPageSelected(int position) {
            View oldPoint = emojiKeyboardIndicator.getChildAt(oldPos);
            View newPoint = emojiKeyboardIndicator.getChildAt(position);
            oldPoint.setBackgroundResource(R.drawable.ic_point_normal);
            newPoint.setBackgroundResource(R.drawable.ic_point_select);

            oldPos = position;
        }
    }

}
