package com.susankya.yubahunkar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageZoomFragment extends Fragment {

    @BindView(R.id.post_image_view)
    PhotoView postImageView;

    private String featuredMedia;
    private int mediaId;

    public ImageZoomFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_image_zoom, container, false);

        ButterKnife.bind(this, mView);

        if (getArguments() != null) {
            featuredMedia = getArguments().getString("featured_media");
        }

        if (featuredMedia != null) {
            mediaId = Integer.valueOf(featuredMedia);
        }

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getPostDetails(mediaId).enqueue(new Callback<MainMediaDetails>() {
            @Override
            public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                if (response.isSuccessful() && response.body() != null) {

                    String postImageUrl = response.body().getMediaDetails().getSizes().getFull().getSourceUrl();
                    Picasso.get().load(postImageUrl).into(postImageView);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainMediaDetails> call, @NonNull Throwable t) {

            }
        }
        );

        return mView;
    }
}
