package tif.gaskeun.masodin2.Iface;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import tif.gaskeun.masodin2.R;

public class ListViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemName,itemPrice,itemKey;
    public ImageView itemPic;
    public EditText itemNote,itemQty;
    public Button itemDel;
    public MaterialCardView card;

    public ListViewHolder3(View itemView){
        super(itemView);
        card = itemView.findViewById(R.id.cvRoot3);
        itemPic = itemView.findViewById(R.id.list_img3);
        itemName = itemView.findViewById(R.id.list_title3);
        itemPrice = itemView.findViewById(R.id.list_price3);
        itemNote = itemView.findViewById(R.id.et_ornote);
        itemQty = itemView.findViewById(R.id.et_orqty);
        itemKey = itemView.findViewById(R.id.list_Key3);
        itemDel = itemView.findViewById(R.id.btn_del_item);
        itemDel.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemView.setOnClickListener(this);

    }
}
