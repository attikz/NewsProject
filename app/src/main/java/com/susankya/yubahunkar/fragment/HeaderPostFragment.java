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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Target;
import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.model.SavePostModelList;
import com.susankya.yubahunkar.model.all_posts_model.PostsModel;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeaderPostFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.post_image_view)
    ImageView postImageView;

    @BindView(R.id.post_title_tv)
    TextView postTitleTV;

    @BindView(R.id.app_name_tv)
    TextView appNameTV;

    @BindView(R.id.post_publish_date_tv)
    TextView postDateTV;

    @BindView(R.id.post_content_tv)
    TextView postContentTV;

    @BindView(R.id.one)
    TextView pipeTV;

    @BindView(R.id.post_save)
    ImageView postSave;

    @BindView(R.id.post_share)
    ImageView postShare;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Context context;
    private String headerPostTitle, headerPostDate, headerPostContent, headerPostLink, postId;
    private int featuredMedia, mediaId;

    public HeaderPostFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_header_post, container, false);

        ButterKnife.bind(this, mView);

        if (getArguments() != null) {
            String mediaId = getArguments().getString("featured_media");
            postId = getArguments().getString("post_id");

            if (mediaId != null) {
                this.mediaId = Integer.valueOf(mediaId);
            }
        }

        callAPI();

        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");

        if (getActivity() != null) {
            ((TestActivity) getActivity()).setSupportActionBar(toolbar);
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

        postSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

                clientAPI.getPostDetails(mediaId).enqueue(new Callback<MainMediaDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            String imageUrl = response.body().getMediaDetails().getSizes().getFull().getSourceUrl();

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

                            SavePostModelList postSaveModelList = new SavePostModelList(postId, headerPostTitle, headerPostDate, headerPostContent, postId);

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

        postShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, headerPostLink);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        return mView;
    }

    private void callAPI() {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getAllPosts().enqueue(new Callback<List<PostsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostsModel>> call, @NonNull Response<List<PostsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    for (int i = 0; i < 1; i++) {
                        headerPostTitle = response.body().get(i).title.rendered;
                        headerPostDate = response.body().get(i).date;
                        headerPostContent = response.body().get(i).content.rendered;
                        featuredMedia = response.body().get(i).featured_media;
                        headerPostLink = response.body().get(i).link;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    postTitleTV.setText(Html.fromHtml(headerPostTitle));

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                        Date date = simpleDateFormat.parse(headerPostDate);
                        String correctDate = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(date);
                        postDateTV.setText(correctDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.GONE);

                    cardView.setVisibility(View.VISIBLE);

                    postSave.setVisibility(View.VISIBLE);

                    postShare.setVisibility(View.VISIBLE);

                    postContentTV.setText(Html.fromHtml(headerPostContent));

                    appNameTV.setText(getString(R.string.app_name_nepali));

                    pipeTV.setText(getString(R.string.pipe));

                    callHeaderPostAPI(featuredMedia);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostsModel>> call, @NonNull Throwable t) {

            }
        });
    }

    private void callHeaderPostAPI(int featuredMedia) {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getPostDetails(featuredMedia).enqueue(new Callback<MainMediaDetails>() {
            @Override
            public void onResponse(@NonNull Call<MainMediaDetails> call, @NonNull Response<MainMediaDetails> response) {

                if (response.isSuccessful() && response.body() != null) {

                    String imageUrl = response.body().getMediaDetails().getSizes().getFull().getSourceUrl();
                    Picasso.get().load(imageUrl).into(postImageView);
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