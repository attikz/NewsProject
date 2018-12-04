package com.susankya.yubahunkar.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fb_image_view)
    ImageView fbImageView;

    @BindView(R.id.website_image_view)
    ImageView websiteImageView;

    @BindView(R.id.mazagine_image_view)
    ImageView mazagineImageView;

    private Context context;

    public AboutFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.bind(this, mView);

        if (getActivity() != null) {
            ((TestActivity) getActivity()).setSupportActionBar(toolbar);
            ((TestActivity) getActivity()).getSupportActionBar().setTitle("");
            ((TestActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/yubahunkar")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/yubahunkar")));
                }
            }
        });

        websiteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yubahunkar.com")));
            }
        });

        mazagineImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yuwahunkar.com")));
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}
