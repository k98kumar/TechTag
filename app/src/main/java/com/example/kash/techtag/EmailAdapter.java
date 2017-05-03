package com.example.kash.techtag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kash on 4/15/17.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private ArrayList<Email> emailList;

    EmailAdapter (ArrayList<Email> emailList) {
        this.emailList = emailList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.emailView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        final View emailItemList = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.rec_view_email, parent, false);
        return new ViewHolder(emailItemList);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Email email = emailList.get(position);
        holder.textView.setText(email.getEmail());
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }
}
