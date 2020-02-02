package tif.gaskeun.masodin2.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tif.gaskeun.masodin2.Iface.ListViewHolder2;
import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.Model.Order;
import tif.gaskeun.masodin2.R;

public class ordMenuList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbref;
    public Button btn_chkout;
    public Spinner spn_katg;
    public RecyclerView rlist;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Menu> DataDaftarMenus;
    String storeKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormenu_list);
        mAuth = FirebaseAuth.getInstance();
        storeKey = getIntent().getStringExtra("Key");
        dbref = FirebaseDatabase.getInstance().getReference(storeKey);

//        dbref.child("DaftarMenu").addValueEventListener(new ValueEventListener() {
//            @OverridestoreKey
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final List<String> areas = new ArrayList<>();
//
//                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    String Kategori = snapshot.child("kategori").getValue(Menu.class).getKategori();
//                    areas.add(Kategori);
//                }
//
//                spn_katg = findViewById(R.id.spn_katg);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ordMenuList.this,android.R.layout.simple_spinner_item,areas);
//                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//                spn_katg.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        btn_chkout = findViewById(R.id.btn_chkout);
        btn_chkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ordMenuCart.class).putExtra("storeKey",storeKey));
            }
        });

        rlist = findViewById(R.id.ormnlist_rc);
        rlist.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rlist.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        FirebaseRecyclerOptions<Menu> options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(dbref.child("DaftarMenu"),Menu.class).build();
        FirebaseRecyclerAdapter<Menu, ListViewHolder2> adapter = new FirebaseRecyclerAdapter<Menu, ListViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListViewHolder2 holder2, final int position, @NonNull Menu menu) {
                String eqq = menu.getNama();

                holder2.itemName.setText(eqq);
                holder2.itemPrice.setText(menu.getHarga());

                Query query = dbref.child("DaftarMenu").orderByChild("nama").equalTo(eqq);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            holder2.itemKey.setText(snapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String KeyX = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_Key2)).getText().toString();

                        Bundle extras = new Bundle();
                        extras.putString("storeKey",storeKey);
                        extras.putString("itemKey",KeyX);

                        startActivity(new Intent(getApplicationContext(),ordMenuInfo.class).putExtras(extras));
                    }
                });
                holder2.itemAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String uid = mAuth.getCurrentUser().getUid();
                        final String itemKey = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_Key2)).getText().toString();
                        final String itemName = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_title2)).getText().toString();
                        final String itemPrice = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_price2)).getText().toString();

                        Query query1 = dbref.child("Cart").child(uid);
                        query1.orderByChild("mnID").equalTo(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                        query1.orderByChild("mnID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        String qtyval = snapshot.getValue(Order.class).getMnQty();
                                        int qtycalc = Integer.parseInt(qtyval) + 1;
                                        qtyval = String.valueOf(qtycalc);
                                        dbref.child("Cart").child(uid).child(snapshot.getKey()).child("mnQty").setValue(qtyval);
                                        Snackbar.make(getWindow().getDecorView().getRootView(),snapshot.getKey(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    String qty = "1";
                                    Map<String, Object> add = new HashMap<>();
                                    add.put("/mnID",itemKey);
                                    add.put("/mnNama",itemName);
                                    add.put("/mnHarga",itemPrice);
                                    add.put("/mnQty",qty);
                                    dbref.child("Cart").child(uid).push().updateChildren(add);
                                    Snackbar.make(getWindow().getDecorView().getRootView(),itemName + "Di Tambahkan", Snackbar.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public ListViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item2, parent, false);
                ListViewHolder2 holder = new ListViewHolder2(view);
                return holder;
            }
        };
        rlist.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateUI(final FirebaseUser user) {
        if(user==null){
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }else {
                        updateUI(null);
                    }
                }
            });
        }
    }
}
