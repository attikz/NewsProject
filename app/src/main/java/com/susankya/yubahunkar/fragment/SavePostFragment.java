package com.susankya.yubahunkar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.adapter.SavePostAdapter;
import com.susankya.yubahunkar.model.SavePostModel;
import com.susankya.yubahunkar.model.SavePostModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavePostFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context context;
    private List<SavePostModel> savePostModel = new ArrayList<>();

    public SavePostFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_save_post, container, false);

        ButterKnife.bind(this, mView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sp = context.getSharedPreferences("PostSave", 0);

        Map<String, ?> allEntries = sp.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            String postId = entry.getKey();

            Gson gson = new Gson();
            String json = sp.getString(postId, "");
            SavePostModelList postSaveModelLists = gson.fromJson(json, SavePostModelList.class);

            savePostModel.add(new SavePostModel(
                postSaveModelLists.postImagePath,
                postSaveModelLists.postTitle,
                postSaveModelLists.postDate,
                postSaveModelLists.postContent,
                postSaveModelLists.postId));
        }

        recyclerView.setAdapter(new SavePostAdapter(getContext(), savePostModel));

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
