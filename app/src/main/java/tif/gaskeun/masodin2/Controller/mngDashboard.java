package tif.gaskeun.masodin2.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Queue;

import tif.gaskeun.masodin2.Iface.ListViewHolder1;
import tif.gaskeun.masodin2.Iface.ListViewHolder5;
import tif.gaskeun.masodin2.MainActivity;
import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.Model.Order2;
import tif.gaskeun.masodin2.R;

public class mngDashboard extends AppCompatActivity{

    private DatabaseReference dbref,dbref2;
    public Button btn_add,btn_out;
    public RecyclerView rlist, rlist2;
    public String idtrans,iduser;
    RecyclerView.LayoutManager layoutManager1,layoutManager2;
    FirebaseAuth mAuth;
    String key;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();

        btn_add = findViewById(R.id.btn_addMenu);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mngMenuAdd.class).putExtra("Key",key));
            }
        });
        btn_out = findViewById(R.id.btn_logout);
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        rlist = findViewById(R.id.rcy_list_menu2);
        rlist.setHasFixedSize(false);
        rlist2 = findViewById(R.id.rcy_list_trans);
        rlist2.setHasFixedSize(false);
        layoutManager1 = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        rlist.setLayoutManager(layoutManager1);
        rlist2.setLayoutManager(layoutManager2);

        key = mAuth.getUid();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        dbref = FirebaseDatabase.getInstance().getReference(key).child("DaftarMenu");

        FirebaseRecyclerOptions<Menu> options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(dbref,Menu.class).build();
        FirebaseRecyclerAdapter<Menu, ListViewHolder1> adapter = new FirebaseRecyclerAdapter<Menu, ListViewHolder1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListViewHolder1 holder1, final int position, @NonNull Menu menu) {
                String keyhint = menu.getNama();
                holder1.itemName.setText(keyhint);

                Query query = dbref.orderByChild("nama").equalTo(keyhint);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            holder1.itemKey.setText(snapshot.getKey());
                        }
                    }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }

               });
               holder1.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String keyX = ((TextView)rlist.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.list_Key1)).getText().toString();
                       startActivity(new Intent(getApplicationContext(), mngMenuInfo.class).putExtra("key",keyX));
                   }
               });
            }

            @NonNull
            @Override
            public ListViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item1, parent, false);
                ListViewHolder1 holder1 = new ListViewHolder1(view);
                return holder1;
            }
        };

        dbref2 = FirebaseDatabase.getInstance().getReference(key).child("Transaksi");
        FirebaseRecyclerOptions<Order2> options1 = new FirebaseRecyclerOptions.Builder<Order2>().setQuery(dbref2,Order2.class).build();
        FirebaseRecyclerAdapter<Order2, ListViewHolder5> adapter1 = new FirebaseRecyclerAdapter<Order2, ListViewHolder5>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final ListViewHolder5 holder2,final int position, @NonNull final Order2 order2) {
                dbref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(final DataSnapshot snapshot:dataSnapshot.getChildren()){

                            idtrans = (snapshot.getKey());
                            holder2.itemName.setText(snapshot.getKey());
                            dbref2.child(idtrans).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                    holder2.itemName.setText(snapshot1.getKey());
                                    iduser = snapshot1.getKey();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

//                            iduser = holder2.itemName.getText().toString();

                            Query query1 = dbref2.child(idtrans).orderByChild("uid").equalTo(iduser);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot1:dataSnapshot.getChildren()){
//                                        Order2 ord2 = snapshot1.getValue(Order2.class);
//                                        holder2.itemName.setText(ord2.getWaktu());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ListViewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item1, parent, false);
                ListViewHolder5 holder5 = new ListViewHolder5(view);
                return holder5;
            }
        };

        rlist.setAdapter(adapter);
        rlist2.setAdapter(adapter1);
        adapter.startListening();
        adapter1.startListening();


    }

    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//                mAuth.signOut();
//                finish();
//            }
//        }, 200000000);
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
