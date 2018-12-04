package com.susankya.yubahunkar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.adapter.PostsAdapter;
import com.susankya.yubahunkar.api.ClientAPI;
import com.susankya.yubahunkar.application.PostsApplication;
import com.susankya.yubahunkar.model.all_posts_model.PostsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.no_post_message_tv)
    TextView noPostMessageTV;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<PostsModel> allPostsModels = new ArrayList<>();
    private String id, title;
    private int categoriesId;

    public OthersFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_others, container, false);

        ButterKnife.bind(this, mView);

        if (getArguments() != null) {
            id = getArguments().getString("id");
            title = getArguments().getString("title");
        }

        if (id != null) {
            categoriesId = Integer.valueOf(id);
        }

        if (getActivity() != null) {
            ((TestActivity) getActivity()).setSupportActionBar(toolbar);
            ((TestActivity) getActivity()).getSupportActionBar().setTitle(title);
            ((TestActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        callAPI();

        return mView;
    }

    private void callAPI() {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getPostsCategories(categoriesId).enqueue(new Callback<List<PostsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostsModel>> call, @NonNull Response<List<PostsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        noPostMessageTV.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        noPostMessageTV.setVisibility(View.INVISIBLE);
                        allPostsModels = response.body();
                        recyclerView.setAdapter(new PostsAdapter(getContext(), allPostsModels));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostsModel>> call, @NonNull Throwable t) {

            }
        });
    }
}
