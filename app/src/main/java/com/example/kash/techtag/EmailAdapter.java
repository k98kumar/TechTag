package com.example.kash.techtag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Copyright 2017  Koushhik Kumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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
