package com.susankya.yubahunkar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.model.all_posts_model.PostsModel;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<PostsModel> postsModelList;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> hashMap = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> dateHashMap = new HashMap<>();
    private String imageUrl, correctDateFormat, headerPostTitle;

    public HomeAdapter(Context context, List<PostsModel> postsModelList, String headerPostTitle) {
        this.context = context;
        this.postsModelList = postsModelList;
        this.headerPostTitle = headerPostTitle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final PostsModel model = postsModelList.get(position);

        if (headerPostTitle.equals(model.title.rendered)) {
            viewHolder.cardView.setVisibility(View.GONE);
            viewHolder.postImageView.setVisibility(View.GONE);
            viewHolder.postTitleTV.setVisibility(View.GONE);
            viewHolder.postDateTV.setVisibility(View.GONE);
        } else {
            viewHolder.postTitleTV.setText(Html.fromHtml(model.title.rendered));

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                Date publishDate = simpleDateFormat.parse(model.date);
                correctDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(publishDate);
                viewHolder.postDateTV.setText(correctDateFormat);
                dateHashMap.put(position, correctDateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

            clientAPI.getPostDetails(model.featured_media).enqueue(new Callback<MainMediaDetails>() {
                @Override
                public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        imageUrl = response.body().getMediaDetails().getSizes().getThumbnail().getSourceUrl();

                        hashMap.put(position, imageUrl);

                        Picasso.get().load(imageUrl).into(viewHolder.postImageView);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MainMediaDetails> call, @NonNull Throwable t) {

                }
            }
            );
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image = hashMap.get(position);
                String correctedDateFormat = dateHashMap.get(position);

                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("key", "fragment");
                intent.putExtra("image", image);
                intent.putExtra("title", model.title.rendered);
                intent.putExtra("publish_date", correctedDateFormat);
                intent.putExtra("content", model.content.rendered);
                intent.putExtra("featured_media", String.valueOf(model.featured_media));
                intent.putExtra("link", model.link);
                intent.putExtra("post_id", String.valueOf(model.id));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView postImageView;
        private TextView postTitleTV, postDateTV;
        private CardView cardView;

        private ViewHolder(View view) {
            super(view);

            postImageView = itemView.findViewById(R.id.image_view);
            postTitleTV = itemView.findViewById(R.id.title_tv);
            postDateTV = itemView.findViewById(R.id.publish_date_tv);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
