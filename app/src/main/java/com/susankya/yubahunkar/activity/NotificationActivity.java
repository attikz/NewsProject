package com.susankya.yubahunkar.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.fragment.PostDetailsFragment;
import com.susankya.yubahunkar.model.all_posts_model.PostsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private int id = 0;
    String imageUrl, title, publishDate, content, postLink;
    int featuredMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        id = getIntent().getIntExtra("id", 0);
        callAPI();
    }

    public void callAPI() {

        ClientAPI api = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        api.getPost(id).enqueue(new Callback<PostsModel>() {
            @Override
            public void onResponse(@NonNull Call<PostsModel> call, @NonNull Response<PostsModel> response) {

                if (response.isSuccessful() && response.body() != null) {

                    title = response.body().title.rendered;
                    publishDate = response.body().date;
                    content = response.body().content.rendered;
                    postLink = response.body().link;
                    featuredMedia = response.body().featured_media;

                    callFrag();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostsModel> call, @NonNull Throwable t) {

            }
        }
        );
    }

    public void callFrag() {

        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("image", imageUrl);
        bundle.putString("title", title);
        bundle.putString("publish_date", publishDate);
        bundle.putString("content", content);
        bundle.putString("featured_media", String.valueOf(featuredMedia));
        bundle.putString("link", postLink);

        postDetailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, postDetailsFragment).commit();
    }
}
