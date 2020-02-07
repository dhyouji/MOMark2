package tif.gaskeun.masodin2.Iface;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import tif.gaskeun.masodin2.R;

public class ListViewHolder5 extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName,itemKey;
    public ImageView itemPic;
    public MaterialCardView card;

    public ListViewHolder5(View itemView){
        super(itemView);
        itemPic = itemView.findViewById(R.id.list_img1);
        itemName = itemView.findViewById(R.id.list_title1);
        itemKey = itemView.findViewById(R.id.list_Key1);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemView.setOnClickListener(this);

    }
}
