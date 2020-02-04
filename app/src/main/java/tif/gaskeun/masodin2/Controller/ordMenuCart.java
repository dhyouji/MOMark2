package tif.gaskeun.masodin2.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import tif.gaskeun.masodin2.Iface.ListViewHolder3;
import tif.gaskeun.masodin2.Model.Order;
import tif.gaskeun.masodin2.R;

public class ordMenuCart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbref;
    public EditText etNM,etKt,etMj;
    public Button btn_chkout;
    public RecyclerView rlist;
    RecyclerView.LayoutManager layoutManager;
    String uid,storeKey;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormenu_cart);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        uid = mAuth.getCurrentUser().getUid();
        storeKey = getIntent().getStringExtra("storeKey");


        dbref = FirebaseDatabase.getInstance().getReference(storeKey);

        etNM = findViewById(R.id.et_nama);
        etKt = findViewById(R.id.et_kontak);
        etMj = findViewById(R.id.et_nmeja);
        btn_chkout = findViewById(R.id.btn_chkout);
        btn_chkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String nm,kt,mj,time;
               nm = etNM.getText().toString();
               kt = etKt.getText().toString();
               mj = etMj.getText().toString();
               time = new String(String.valueOf(new Timestamp(System.currentTimeMillis())));

                dbref.child("Cart").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       DatabaseReference push = dbref.child("Transaksi").child(uid).push();
                       String path = (dbref.child("Transaksi").child(uid).push().getKey());
                       dbref.child("Transaksi").child(uid).child(path).child("Menu").setValue(dataSnapshot.getValue());
//                       FirebaseDatabase.getInstance().getReference(path).child("Menu").setValue(dataSnapshot.getValue());

                       Map<String, Object> info = new HashMap<>();
                       info.put("/nama",nm);
                       info.put("/kontak",kt);
                       info.put("/nmeja",mj);
                       info.put("/waktu",time);
                       dbref.child("Transaksi").child(uid).child(path).child("Info").updateChildren(info);

                       dataSnapshot.getRef().removeValue();
                       etMj.setText("");

                       Bundle transaksi = new Bundle();
                       transaksi.putString("trans",path);
                       transaksi.putString("key",storeKey);


                       startActivity(new Intent(getApplicationContext(),ordMenuChkout.class).putExtras(transaksi));
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

                Snackbar.make(findViewById(R.id.btn_chkout),"Pesanan Di Pesan", Snackbar.LENGTH_LONG).show();
            }
        });

        rlist = findViewById(R.id.ormnlist_rc);
        rlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rlist.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String uid = mAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>().setQuery((dbref.child("Cart").child(uid)), Order.class).build();
        final FirebaseRecyclerAdapter<Order, ListViewHolder3> adapter = new FirebaseRecyclerAdapter<Order, ListViewHolder3>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListViewHolder3 holder3,final int position, @NonNull Order order) {
                final String id = order.getMnID();
                holder3.itemName.setText(order.getMnNama());
                holder3.itemPrice.setText(order.getMnHarga());
                holder3.itemQty.setText(order.getMnQty());

                final Query query = dbref.child("Cart").child(uid).orderByChild("mnID").equalTo(id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            holder3.itemKey.setText(snapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder3.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String KeyX = id;
                        startActivity(new Intent(getApplicationContext(),ordMenuInfo.class).putExtra("key",KeyX));
                    }
                });
                holder3.itemDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String itemKey = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_Key3)).getText().toString();
                        dbref.child("Cart").child(uid).child(itemKey).removeValue();
                        rlist.getAdapter().notifyDataSetChanged();
                    }
                });
            }

            @NonNull
            @Override
            public ListViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item3, parent, false);
                ListViewHolder3 holder = new ListViewHolder3(view);
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
