package tif.gaskeun.masodin2.Controller;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import tif.gaskeun.masodin2.Iface.ListViewHolder4;
import tif.gaskeun.masodin2.Model.Informasi;
import tif.gaskeun.masodin2.Model.Order;
import tif.gaskeun.masodin2.Model.Order2;
import tif.gaskeun.masodin2.R;

public class ordMenuChkout extends AppCompatActivity {

    DatabaseReference dbref;
    FirebaseAuth mAuth;
    ImageView barcode;
    TextView tvcode,tvrest,tvadrs,tvinfo,tvtotal;
    String uid,storekey,key;
    int calctotal;
    RecyclerView rlist;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcheckout);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        barcode = findViewById(R.id.iv_barcode);
        tvcode = findViewById(R.id.tv_barcode);
        tvrest = findViewById(R.id.tv_restname);
        tvadrs = findViewById(R.id.tv_restaddrs);
        tvinfo = findViewById(R.id.tv_info);
        tvtotal = findViewById(R.id.tv_totalharga);
        rlist = findViewById(R.id.rlistStruct);

        storekey = getIntent().getStringExtra("key");
        key = getIntent().getStringExtra("trans");

        dbref = FirebaseDatabase.getInstance().getReference(storekey);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Informasi info = dataSnapshot.getValue(Informasi.class);
                    String rest = info.getRestoran();
                    String addrs = info.getAlamat();
                    tvrest.setText(rest);
                    tvadrs.setText(addrs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tvcode.setText(key);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(key, BarcodeFormat.CODE_128,900,150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            barcode.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

        dbref.child("Transaksi").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Order2 order = dataSnapshot.getValue(Order2.class);
                    String nama = order.getNama();
                    String kontak = order.getKontak();
                    String meja = order.getNmeja();
                    tvinfo.setText("Atas Nama : " + nama + " (" + kontak + ") No Meja : " + meja);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        calctotal = 0;
        rlist.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rlist.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Order2> options = new FirebaseRecyclerOptions.Builder<Order2>().setQuery(dbref.child("Transaksi").child(key).child("Menu"),Order2.class).build();
        FirebaseRecyclerAdapter<Order2, ListViewHolder4> adapter = new FirebaseRecyclerAdapter<Order2, ListViewHolder4>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListViewHolder4 holder4, int position, @NonNull Order2 order2) {
                String Menu = order2.getMnNama();
                String Harga = order2.getMnHarga();
                String Qty = order2.getMnQty();

                holder4.itemName.setText(Menu);
                holder4.itemPrice.setText("Rp. "+Harga);
                holder4.itemQty.setText("("+Qty+")");

                int Total = Integer.parseInt(Harga) * Integer.parseInt(Qty);
                calctotal = calctotal + Total;
                String Totalfx = Integer.toString(Total);

                holder4.itemTotal.setText("Rp. "+Totalfx);
                tvtotal.setText("Rp. "+Integer.toString(calctotal));
            }

            @NonNull
            @Override
            public ListViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item4, parent, false);
                ListViewHolder4 holder4 = new ListViewHolder4(view);
                return holder4;
            }
        };

        rlist.setAdapter(adapter);
        adapter.startListening();
    }
}
