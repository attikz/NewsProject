package com.susankya.yubahunkar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Target;
import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.NotificationActivity;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.model.SavePostModelList;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailsFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.post_image)
    ImageView postImage;

    @BindView(R.id.post_title)
    TextView postTitleTV;

    @BindView(R.id.post_publish_date)
    TextView postPublishDateTV;

    @BindView(R.id.post_save)
    ImageView postSave;

    @BindView(R.id.post_share)
    ImageView postShare;

    @BindView(R.id.post_content)
    TextView postContentTV;

    private Context context;
    private String postImageUrl, postTitle, postContent, postDate, featuredMedia, imageUrl, postLink, postId;
    private int mediaId;

    public PostDetailsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post_details, container, false);

        ButterKnife.bind(this, mView);

        if (getArguments() != null) {
            postImageUrl = getArguments().getString("image");
            postTitle = getArguments().getString("title");
            postDate = getArguments().getString("publish_date");
            postContent = getArguments().getString("content");
            featuredMedia = getArguments().getString("featured_media");
            postLink = getArguments().getString("link");
            postId = getArguments().getString("post_id");
        }

        if (featuredMedia != null) {
            mediaId = Integer.valueOf(featuredMedia);
        }

        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");

        if (getActivity() instanceof TestActivity) {
            if (getActivity() != null) {
                ((TestActivity) getActivity()).setSupportActionBar(toolbar);
            }
        } else if (getActivity() instanceof NotificationActivity) {
            if (getActivity() != null) {
                ((NotificationActivity) getActivity()).setSupportActionBar(toolbar);
            }
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        postTitleTV.setText(Html.fromHtml(postTitle));

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(postDate);
            simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
            postDate = simpleDateFormat.format(date);
            postPublishDateTV.setText(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        postContentTV.setText(Html.fromHtml(postContent));

        callAPI(mediaId);

        postSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

                clientAPI.getPostDetails(mediaId).enqueue(new Callback<MainMediaDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            imageUrl = response.body().getMediaDetails().getSizes().getFull().getSourceUrl();

                            Picasso.get().load(imageUrl).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    String fileName = postId + ".jpg";
                                    File file = new File(context.getFilesDir(), fileName);

                                    try {
                                        FileOutputStream out = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                        out.flush();
                                        out.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });

                            SharedPreferences sp = context.getSharedPreferences("PostSave", 0);
                            SharedPreferences.Editor editor = sp.edit();

                            SavePostModelList postSaveModelList = new SavePostModelList(postId, postTitle, postDate, postContent, postId);

                            Gson gson = new Gson();
                            String json = gson.toJson(postSaveModelList);
                            editor.putString(postId, json);
                            editor.apply();

                            Toast.makeText(getContext(), "Post saved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MainMediaDetails> call, @NonNull Throwable t) {

                    }
                }
                );
            }
        });

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageZoomFragment imageZoomFragment = new ImageZoomFragment();

                Bundle bundle = new Bundle();
                bundle.putString("featured_media", featuredMedia);
                imageZoomFragment.setArguments(bundle);

                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, imageZoomFragment).addToBackStack(null).commit();
                }
            }
        }
        );

        postShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, postLink);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        return mView;
    }

    private void callAPI(int featuredMedia) {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getPostDetails(featuredMedia).enqueue(new Callback<MainMediaDetails>() {
            @Override
            public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                if (response.isSuccessful() && response.body() != null) {

                    imageUrl = response.body().getMediaDetails().getSizes().getFull().getSourceUrl();
                    Picasso.get().load(imageUrl).into(postImage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainMediaDetails> call, @NonNull Throwable t) {

            }
        }
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
