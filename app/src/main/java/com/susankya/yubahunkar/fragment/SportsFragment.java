package com.susankya.yubahunkar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.susankya.yubahunkar.R;
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

public class SportsFragment extends Fragment {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<PostsModel> allPostsModels = new ArrayList<>();

    public SportsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, mView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        callAPI();

        return mView;
    }

    private void callAPI() {

        ClientAPI clientAPI = PostsApplication.ClientRetrofit().create(ClientAPI.class);

        clientAPI.getPostsCategories(20).enqueue(new Callback<List<PostsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostsModel>> call, @NonNull Response<List<PostsModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    allPostsModels = response.body();

                    progressBar.setVisibility(View.GONE);

                    recyclerView.setAdapter(new PostsAdapter(getContext(), allPostsModels));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostsModel>> call, @NonNull Throwable t) {

            }
        });
    }
}
