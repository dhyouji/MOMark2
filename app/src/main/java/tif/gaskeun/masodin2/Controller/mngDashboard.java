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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tif.gaskeun.masodin2.Iface.ListViewHolder1;
import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.R;

public class mngDashboard extends AppCompatActivity{

    private DatabaseReference dbref;
    public Button btn_add,btn_del,btn_edit;
    public RecyclerView rlist;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Menu> DataDaftarMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_add = findViewById(R.id.btn_addMenu);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mngMenuAdd.class));
            }
        });

        rlist = findViewById(R.id.rcy_list_menu);
        rlist.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rlist.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbref = FirebaseDatabase.getInstance().getReference("DataResto").child("DaftarMenu");

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
        rlist.setAdapter(adapter);
        adapter.startListening();
    }
}
