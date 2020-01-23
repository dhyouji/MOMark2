package tif.gaskeun.masodin2.Iface;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import tif.gaskeun.masodin2.R;

public class ListViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName,itemPrice,itemKey;
    public ImageView itemPic;
    public Button itemAdd;
    public MaterialCardView card;

    public ListViewHolder2(View itemView){
        super(itemView);
        card = itemView.findViewById(R.id.cvRoot2);
        itemPic = itemView.findViewById(R.id.list_img2);
        itemName = itemView.findViewById(R.id.list_title2);
        itemPrice = itemView.findViewById(R.id.list_price2);
        itemKey = itemView.findViewById(R.id.list_Key2);
        itemAdd = itemView.findViewById(R.id.btn_add_item);
        itemAdd.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemView.setOnClickListener(this);

    }
}
