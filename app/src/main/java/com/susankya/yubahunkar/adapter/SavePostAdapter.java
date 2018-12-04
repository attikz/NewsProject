package com.susankya.yubahunkar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.susankya.yubahunkar.R;
import com.susankya.yubahunkar.activity.TestActivity;
import com.susankya.yubahunkar.model.SavePostModel;

import java.io.File;
import java.util.List;

public class SavePostAdapter extends RecyclerView.Adapter<SavePostAdapter.ViewHolder> {

    private Context context;
    private List<SavePostModel> savePostModelList;

    public SavePostAdapter(Context context, List<SavePostModel> savePostModelList) {
        this.context = context;
        this.savePostModelList = savePostModelList;
    }

    @NonNull
    @Override
    public SavePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posts_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavePostAdapter.ViewHolder viewHolder, int i) {

        final SavePostModel model = savePostModelList.get(i);

        final File file = new File(context.getFilesDir(),model.postId + ".jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        viewHolder.postImageView.setImageBitmap(bitmap);

        viewHolder.postTitleTV.setText(model.postTitle);
        viewHolder.postDateTV.setText(model.postDate);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("key", "saved_post_detail");
                intent.putExtra("image", file.getPath());
                intent.putExtra("title", model.postTitle);
                intent.putExtra("publish_date", model.postDate);
                intent.putExtra("content", model.postContent);
                context.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.delete_save_post_dialog_layout, true)
                        .canceledOnTouchOutside(false)
                        .show();

                if (dialog.getCustomView() != null) {
                    final TextView deletePostTV = dialog.getCustomView().findViewById(R.id.delete_post_tv);

                    deletePostTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences sp = context.getSharedPreferences("PostSave", 0);
                            SharedPreferences.Editor editor = sp.edit();
                        }
                    });
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return savePostModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImageView;
        TextView postTitleTV, postDateTV;

        private ViewHolder(View itemView) {
            super(itemView);

            postImageView = itemView.findViewById(R.id.image_view);
            postTitleTV = itemView.findViewById(R.id.title_tv);
            postDateTV = itemView.findViewById(R.id.publish_date_tv);
        }
    }
}