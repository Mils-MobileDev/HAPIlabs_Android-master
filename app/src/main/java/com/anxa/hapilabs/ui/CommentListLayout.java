package com.anxa.hapilabs.ui;

import java.util.List;

import com.hapilabs.R;
import com.anxa.hapilabs.common.connection.listener.MainActivityCallBack;
import com.anxa.hapilabs.common.connection.listener.MealAddCommentListener;
import com.anxa.hapilabs.ui.adapters.CommentViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

public class CommentListLayout extends LinearLayout implements AbsListView.OnScrollListener{

    Context context;

    OnClickListener listener;

    LayoutInflater layoutInflater;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    List<Object> items;

    View row = null;
    MainActivityCallBack mainListener;
    CommentViewAdapter adapter;
    CustomListView listview;
    MealAddCommentListener commentlistener;

    boolean userScrolled = false;

    int currentFirstVisibleItem;
    int currentVisibleItemCount;
    int totalItem;

    @SuppressLint("NewApi")
    public CommentListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        onCreateView(null);

    }

    public CommentListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        onCreateView(null);
    }

    public CommentListLayout(Context context, List<Object> items) {
        super(context);
        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        onCreateView(null);
    }

    public boolean initData(List<Object> items, Context context, OnClickListener listener, MealAddCommentListener commentlistener, MainActivityCallBack mainListener) {

        layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        this.context = context;
        this.listener = listener;
        this.items = items;
        this.commentlistener = commentlistener;
        this.mainListener = mainListener;

        onCreateView(null);
        return true;
    }

    public boolean updateDataFromRefresh(List<Object> items) {
        this.items = items;

        if (adapter == null) {
            if (row == null) {
                row = LayoutInflater.from(context).inflate(R.layout.commentlist, null, false);
            }

            adapter = new CommentViewAdapter(context, items, mainListener, listener, commentlistener);
            listview = (CustomListView) row.findViewById(R.id.listcomments);
            listview.setAdapter(adapter);
            addView(row);

        } else {
            adapter.update(items);
        }

        listview.smoothScrollToPositionFromTop(items.size()-1, 0);

        adapter.notifyDataSetChanged();

        return true;
    }

    public boolean updateData(List<Object> items) {

        final int startingItem = items.size() - totalItem + currentFirstVisibleItem;

        this.items = items;

        if (adapter == null) {
            if (row == null) {
                row = LayoutInflater.from(context).inflate(R.layout.commentlist, null, false);
            }

            adapter = new CommentViewAdapter(context, items, mainListener, listener, commentlistener);
            adapter.setNotifyOnChange(false);
            listview = (CustomListView) row.findViewById(R.id.listcomments);
            listview.setStackFromBottom(false);
            listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
            listview.setAdapter(adapter);
            addView(row);

        } else {
            adapter.setNotifyOnChange(false);
            adapter.update(items);
            listview.setStackFromBottom(false);
            listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
            listview.setAdapter(adapter);
        }

        // save index and top position
        View v = listview.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();


        // restore index and top position
        listview.setSelectionFromTop(startingItem, top);
        listview.setScrollbarFadingEnabled(false);

        return true;
    }


    public void onCreateView(ViewGroup container) {
        if (items == null) {
            return;
        }

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.commentlist, null, false);
            listview = (CustomListView) row.findViewById(R.id.listcomments);
            listview.setOnScrollListener(this);
            adapter = new CommentViewAdapter(context, items, mainListener, listener, commentlistener);
            listview.setAdapter(adapter);
            try {
                removeView(row);
            } catch (Exception e) {
            }

            addView(row);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItem = totalItemCount;

        if (userScrolled){
            if (totalItem - currentFirstVisibleItem == currentVisibleItemCount) {
            }
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            userScrolled = true;
            //set load more button to visible
//            System.out.println("userScrolled SCROLL_STATE_TOUCH_SCROLL");
            Intent broadint = new Intent();
            broadint.setAction("SCROLL_STARTED");
            getContext().sendBroadcast(broadint);
            listview.setScrollbarFadingEnabled(true);

        }
        if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                && scrollState == SCROLL_STATE_IDLE && userScrolled) {
            Intent broadint = new Intent();
            broadint.setAction("END_OF_LINE");
            getContext().sendBroadcast(broadint);
            //get latest
        }else if (currentFirstVisibleItem == 0
                && scrollState == SCROLL_STATE_IDLE && userScrolled){

            listview.setStackFromBottom(false);
            listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);

            //reached the top most list
            Intent broadint = new Intent();
            broadint.setAction("TOP_OF_LINE");
            getContext().sendBroadcast(broadint);
        }
    }
}