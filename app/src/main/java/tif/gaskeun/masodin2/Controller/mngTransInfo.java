package tif.gaskeun.masodin2.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import tif.gaskeun.masodin2.Iface.ListViewHolder4;
import tif.gaskeun.masodin2.Model.Informasi;
import tif.gaskeun.masodin2.Model.Order2;
import tif.gaskeun.masodin2.R;

public class mngTransInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView nma, nmja, wktu, cnct, rest, addrs, total;
    public DatabaseReference dbref, dbref2;
    RecyclerView rlist;
    int calctotal;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_info);

        mAuth = FirebaseAuth.getInstance();
        nma = findViewById(R.id.tv_nma);
        nmja = findViewById(R.id.tv_nomej);
        wktu = findViewById(R.id.tv_wktu);
        cnct = findViewById(R.id.tv_cnct);
        rest = findViewById(R.id.tv_restname);
        addrs = findViewById(R.id.tv_restaddrs);
        rlist = findViewById(R.id.rlistTrans);
        total = findViewById(R.id.tv_totalharga);

        String key = mAuth.getUid();
        final String key1 = getIntent().getStringExtra("id1");

        dbref = FirebaseDatabase.getInstance().getReference(key);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Informasi info = dataSnapshot.getValue(Informasi.class);
                    String nrest = info.getRestoran();
                    String addrsr = info.getAlamat();
                    rest.setText(nrest);
                    addrs.setText(addrsr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbref2 = FirebaseDatabase.getInstance().getReference(key).child("Transaksi").child(key1);
        dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order2 ord = dataSnapshot.getValue(Order2.class);
                    nma.setText("Nama: "+ord.getNama());
                    nmja.setText("No. Meja: "+ord.getNmeja());
                    wktu.setText("Waktu: "+ord.getWaktu());
                    cnct.setText("Kontak: "+ord.getKontak());
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
        dbref2 = FirebaseDatabase.getInstance().getReference(key);
        FirebaseRecyclerOptions<Order2> options = new FirebaseRecyclerOptions.Builder<Order2>().setQuery(dbref2.child("Transaksi").child(key1).child("Menu"), Order2.class).build();
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
                total.setText("Rp. "+Integer.toString(calctotal));
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
