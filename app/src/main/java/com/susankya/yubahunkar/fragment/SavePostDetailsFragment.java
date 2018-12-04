package com.susankya.yubahunkar.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavePostDetailsFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.post_image)
    ImageView postImageView;

    @BindView(R.id.post_title)
    TextView postTitleTV;

    @BindView(R.id.post_publish_date)
    TextView postPublishDateTV;

//    @BindView(R.id.post_share)
//    ImageView postShare;

    @BindView(R.id.post_content)
    TextView postContentTV;

    private String postImageUrl, postTitle, postDate, postContent;

    public SavePostDetailsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_save_post_details, container, false);

        ButterKnife.bind(this, mView);

        if (getArguments() != null) {
            postImageUrl = getArguments().getString("post_image_url");
            postTitle = getArguments().getString("post_title");
            postDate = getArguments().getString("post_date");
            postContent = getArguments().getString("post_content");
        }

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

        Bitmap bitmap = BitmapFactory.decodeFile(postImageUrl);
        postImageView.setImageBitmap(bitmap);

        postTitleTV.setText(Html.fromHtml(postTitle));
        postPublishDateTV.setText(postDate);
        postContentTV.setText(Html.fromHtml(postContent));

//        postShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, postLink);
//                intent.setType("text/plain");
//                startActivity(intent);
//            }
//        });

        return mView;
    }
}
