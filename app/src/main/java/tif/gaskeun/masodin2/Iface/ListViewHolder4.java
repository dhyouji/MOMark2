package tif.gaskeun.masodin2.Iface;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import tif.gaskeun.masodin2.R;

public class ListViewHolder4 extends RecyclerView.ViewHolder {
    public TextView itemName,itemPrice,itemQty,itemTotal;

    public ListViewHolder4(View itemView){
        super(itemView);
        itemName = itemView.findViewById(R.id.list_title4);
        itemPrice = itemView.findViewById(R.id.list_price4);
        itemQty = itemView.findViewById(R.id.list_qty);
        itemTotal = itemView.findViewById(R.id.list_total);
    }
}

