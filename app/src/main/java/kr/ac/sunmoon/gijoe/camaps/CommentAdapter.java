package kr.ac.sunmoon.gijoe.camaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CommentData> commentData;

    public CommentAdapter(Context mContext, ArrayList<CommentData> commentData) {
        this.mContext = mContext;
        this.commentData = commentData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return commentData.size();
    }

    @Override
    public CommentData getItem(int position) {
        return commentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.comment_list, null);

        TextView commenter = (TextView)view.findViewById(R.id.commenter);
        TextView comment = (TextView)view.findViewById(R.id.comment);



        commenter.setText(commentData.get(position).getNick());
        comment.setText(commentData.get(position).getComment());

        return view;
    }
}