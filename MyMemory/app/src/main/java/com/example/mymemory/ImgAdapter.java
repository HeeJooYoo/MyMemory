package com.example.mymemory;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.Viewholder> {

    private Activity activity;
    private ArrayList<PhotoForm> dataList;
    public String photo_id;

    public ImgAdapter(Activity activity, ArrayList<PhotoForm> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_img, parent, false);
        Viewholder viewholder = new Viewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Log.e("type", String.valueOf(holder.getItemViewType()));
        Log.d("typeeeeeee", String.valueOf(holder.getItemViewType()));
        PhotoForm data = dataList.get(position);
        holder.image.setImageBitmap(data.getImage());
        Log.d("TEST", String.valueOf(holder.getItemViewType())+"!!!!~~!!!!"+data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public ImageView image;

        public Viewholder(final View itemview) {
            super(itemview);
            image = itemView.findViewById(R.id.album_img);

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoForm data = dataList.get(getAdapterPosition());
                    photo_id = data.getID();
                    Intent albumIntent = new Intent(v.getContext(), PhotoActivity.class);
                    albumIntent.putExtra("photo_id", photo_id);
                    v.getContext().startActivity(albumIntent);
                }
            });
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), PhotoAddActivity.class);
                        itemView.getContext().startActivity(addIntent);
                        break;
                    case 1002: ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        dataList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), dataList.size());
                        break;
                }
                return true;
            }
        };
    }
}
