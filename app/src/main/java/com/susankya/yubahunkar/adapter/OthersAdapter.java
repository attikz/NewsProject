package com.susankya.yubahunkar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.model.OthersModel;

import java.util.List;

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.ViewHolder> {

    private Context context;
    private List<OthersModel> modelList;

    public OthersAdapter(Context context, List<OthersModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.others_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.categoriesTV.setText(modelList.get(i).title);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("key", "others");
                intent.putExtra("image", String.valueOf(modelList.get(i).id));
                intent.putExtra("title", modelList.get(i).title);
                intent.putExtra("publish_date", "none");
                intent.putExtra("content", "none");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView categoriesTV;

        public ViewHolder(View view) {
            super(view);

            cardView = itemView.findViewById(R.id.cardView);
            categoriesTV = itemView.findViewById(R.id.categories_tv);
        }
    }
}
