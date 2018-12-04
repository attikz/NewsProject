package com.susankya.yubahunkar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<PostsModel> allPostsModels;
    private HashMap<Integer, String> hashMap = new HashMap<>();

    private String imageUrl, correctDateFormat;

    public PostsAdapter(Context context, List<PostsModel> allPostsModels) {
        this.context = context;
        this.allPostsModels = allPostsModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final PostsModel model = allPostsModels.get(position);

        viewHolder.titleTV.setText(Html.fromHtml(model.title.rendered));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        try {
            Date publishDate = simpleDateFormat.parse(model.date);
            correctDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(publishDate);
            viewHolder.publishDateTV.setText(correctDateFormat);
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

                    Picasso.get().load(imageUrl).into(viewHolder.imageView);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainMediaDetails> call, @NonNull Throwable t) {

            }
        }
        );

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image = hashMap.get(position);

                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("key", "fragment");
                intent.putExtra("image", image);
                intent.putExtra("title", model.title.rendered);
                intent.putExtra("publish_date", correctDateFormat);
                intent.putExtra("content", model.content.rendered);
                intent.putExtra("featured_media", String.valueOf(model.featured_media));
                intent.putExtra("link", model.link);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allPostsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTV, publishDateTV;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);

            imageView = itemView.findViewById(R.id.image_view);
            titleTV = itemView.findViewById(R.id.title_tv);
            publishDateTV = itemView.findViewById(R.id.publish_date_tv);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
