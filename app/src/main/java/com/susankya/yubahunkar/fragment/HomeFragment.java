package com.susankya.yubahunkar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.adapter.HomeAdapter;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.generic.EndlessRecyclerViewScrollListener;
import com.susankya.yubahunkar.model.all_posts_model.PostsModel;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.post_image_view)
    ImageView postImageView;

    @BindView(R.id.post_title_tv)
    TextView postTitleTV;

    @BindView(R.id.app_name_tv)
    TextView appNameTV;

    @BindView(R.id.dash_tv)
    TextView dashTV;

    @BindView(R.id.post_date_tv)
    TextView postDateTV;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.overlay_layout)
    RelativeLayout overlayLayout;

    private List<PostsModel> postsModelList = new ArrayList<>();
    private String headerPostTitle, headerPostDate;
    private int featuredMedia, nextPage = 2, postId;
    private boolean isThisLastPage = false;

    private HomeAdapter homeAdapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, mView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (!isThisLastPage && postsModelList.size() > 0) {

                    ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

                    clientAPI.getAllPosts(nextPage).enqueue(new Callback<List<PostsModel>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<PostsModel>> call, @NonNull Response<List<PostsModel>> response) {

                            if (response.isSuccessful() && HomeFragment.this.isAdded() && response.body() != null) {
                                ++nextPage;
                                postsModelList.addAll(response.body());
                                homeAdapter.notifyItemRangeInserted(postsModelList.size() - response.body().size(), response.body().size());

                            } else {
                                isThisLastPage = true;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<PostsModel>> call, @NonNull Throwable t) {

                        }
                    }
                    );
                } else
                    Toast.makeText(getContext(), "No more posts left to show right now!", Toast.LENGTH_SHORT).show();
            }
        }
        );

        postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), TestActivity.class);
                intent.putExtra("key", "header_post");
                startActivity(intent);
            }
        });

        callAPI();

        return mView;
    }

    private void callAPI() {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getAllPosts().enqueue(new Callback<List<PostsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostsModel>> call, @NonNull Response<List<PostsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    postsModelList = response.body();

                    for (int i = 0; i < 1; i++) {
                        featuredMedia = response.body().get(i).featured_media;
                        headerPostTitle = response.body().get(i).title.rendered;
                        headerPostDate = response.body().get(i).date;
                        postId = response.body().get(i).id;
                    }

                    overlayLayout.setVisibility(View.VISIBLE);

                    postTitleTV.setText(Html.fromHtml(headerPostTitle));

                    appNameTV.setText(getString(R.string.app_name));

                    dashTV.setText(getString(R.string.dash));

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                        Date date = simpleDateFormat.parse(headerPostDate);
                        String correctFormatDate = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH).format(date);
                        postDateTV.setText(correctFormatDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    callAPIHeaderImage(featuredMedia);

                    progressBar.setVisibility(View.GONE);

                    homeAdapter = new HomeAdapter(getContext(), postsModelList, headerPostTitle);
                    recyclerView.setAdapter(homeAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostsModel>> call, @NonNull Throwable t) {

            }
        });
    }

    private void callAPIHeaderImage(int featuredMedia) {

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
}
