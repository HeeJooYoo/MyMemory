package com.example.mymemory;

import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewholder> {
    private Activity activity;
    private ArrayList<ItemForm> datalist;
    public String album_id;

    public RVAdapter(Activity activity, ArrayList<ItemForm> datalist){
        this.activity = activity;
        this.datalist = datalist;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, parent, false);
        MyViewholder viewholder = new MyViewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
        ItemForm data = datalist.get(position);
        holder.image.setImageBitmap(data.getImage());
        holder.title.setText(data.getTitle());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public ImageView image;
        public TextView title;

        public MyViewholder(final View itemview){
            super(itemview);
            image = itemView.findViewById(R.id.img_cd);
            title = itemView.findViewById(R.id.txt_cd);

            itemview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ItemForm data = datalist.get(getAdapterPosition());
                    album_id = data.getID();
                    Intent albumIntent = new Intent(v.getContext(), AlbumItemActivity.class);
                    albumIntent.putExtra("album_id", album_id);
                    v.getContext().startActivity(albumIntent);
                }
            });
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001,1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002,2,"삭제");
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
                    case 1002:
                        datalist.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), datalist.size());
                        break;
                }
                return true;
            }
        };
    }
}