package com.susankya.yubahunkar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.fragment.AboutFragment;
import com.susankya.yubahunkar.fragment.HeaderPostFragment;
import com.susankya.yubahunkar.fragment.OthersFragment;
import com.susankya.yubahunkar.fragment.PostDetailsFragment;
import com.susankya.yubahunkar.fragment.SavePostDetailsFragment;
import com.susankya.yubahunkar.fragment.SavePostFragment;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String key = getIntent().getStringExtra("key");
        String imageUrl = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String publishDate = getIntent().getStringExtra("publish_date");
        String content = getIntent().getStringExtra("content");
        String featuredMedia = getIntent().getStringExtra("featured_media");
        String postLink = getIntent().getStringExtra("link");
        String postId = getIntent().getStringExtra("post_id");

        switch (key) {
            case "about": {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new AboutFragment()).commit();
                break;
            }
            case "header_post": {
                HeaderPostFragment headerPostFragment = new HeaderPostFragment();

                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("publish_date", publishDate);
                bundle.putString("content", content);
                bundle.putString("featured_media", featuredMedia);
                bundle.putString("post_id", postId);
                headerPostFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, headerPostFragment).commit();
                break;
            }
            case "fragment": {

                PostDetailsFragment postDetailsFragment = new PostDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("image", imageUrl);
                bundle.putString("title", title);
                bundle.putString("publish_date", publishDate);
                bundle.putString("content", content);
                bundle.putString("featured_media", featuredMedia);
                bundle.putString("link", postLink);
                bundle.putString("post_id", postId);

                postDetailsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, postDetailsFragment).commit();
                break;
            }
            case "others": {

                OthersFragment othersFragment = new OthersFragment();

                Bundle bundle = new Bundle();
                bundle.putString("id", imageUrl);
                bundle.putString("title", title);

                othersFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, othersFragment).commit();
                break;
            }
            case "saved_post": {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new SavePostFragment()).commit();
                break;
            }
            case "saved_post_detail": {

                SavePostDetailsFragment savePostDetailsFragment = new SavePostDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("post_image_url", imageUrl);
                bundle.putString("post_title", title);
                bundle.putString("post_date", publishDate);
                bundle.putString("post_content", content);

                savePostDetailsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, savePostDetailsFragment).commit();
                break;
            }
        }
    }
}
