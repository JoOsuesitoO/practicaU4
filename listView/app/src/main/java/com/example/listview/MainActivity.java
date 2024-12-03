package com.example.listview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myListView;
    ArrayList<String> myArrayList = new ArrayList<>();

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, myArrayList);

        myListView = (ListView) findViewById(R.id.listview1);
        myListView.setAdapter(myArrayAdapter);

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                String value = snapshot.getValue(String.class);

                myArrayList.add(key + " : " + value);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String key = snapshot.getKey();
                String newValue = snapshot.getValue(String.class);


                int index = -1;
                for (int i = 0; i < myArrayList.size(); i++) {
                    if (myArrayList.get(i).startsWith(key + " : ")) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    myArrayList.set(index, key + " : " + newValue);
                    myArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();

                int index = -1;
                for (int i = 0; i < myArrayList.size(); i++) {
                    if (myArrayList.get(i).startsWith(key + " : ")) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    myArrayList.remove(index);
                    myArrayAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = myArrayList.get(position);
                String[] parts = item.split(":"); // Divide clave y valor
                String value = parts[1]; // Obtén el valor

                Toast.makeText(MainActivity.this, "Has pulsado: " + value,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}