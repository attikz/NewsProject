package com.susankya.yubahunkar.api;

import com.susankya.yubahunkar.model.all_posts_model.PostsModel;
import com.susankya.yubahunkar.model.post_detail_model.MainMediaDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClientAPI {

    @GET("v2/posts")
    Call<List<PostsModel>> getAllPosts();

    @GET("v2/posts/{id}")
    Call<PostsModel> getPost(@Path("id")int id);

    @GET("v2/posts")
    Call<List<PostsModel>> getAllPosts(@Query("page")Integer page);

    @GET("v2/media/{id}")
    Call<MainMediaDetails> getPostDetails(@Path("id") int id);

    @GET("v2/posts")
    Call<List<PostsModel>> getPostsCategories(@Query("categories") int id);
}
