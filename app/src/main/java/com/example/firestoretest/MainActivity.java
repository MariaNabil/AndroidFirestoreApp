package com.example.firestoretest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText personIdEditText;
    FirebaseFirestore myDB;
    ListView personsListView;
    String[] personsArray ;
    List<Persons>personsList;
    List<String>names;

    Map<String, Object> data ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirestoreServices.Init();
        myDB = FirestoreServices.getInstance();
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        personIdEditText = (EditText) findViewById(R.id.personIdEditText);
        personsListView = (ListView)findViewById(R.id.personsListView);
        //getPersons();
        getAndListenPersons();
    }

    private void getPersons(){
        myDB.collection("persons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       personsList  = new ArrayList<Persons>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                personsList.add(new Persons(document.get("Name").toString(), document.get("Age").toString(), document.get("PersonId").toString()));
                            }
                            personsListView.setAdapter(new CustomAdapter(getApplicationContext(),R.layout.row,personsList));
                        } else {
                        }
                    }
                });
    }

    private void getAndListenPersons(){
        myDB.collection("persons").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                try {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
                List<DocumentSnapshot> documentSnapshots = snapshots.getDocuments();
                personsList  = new ArrayList<Persons>();
                if (documentSnapshots==null) {
                    Toast.makeText(getApplicationContext(),"Empty Document Snapshots",Toast.LENGTH_LONG).show();
                    personsListView.setAdapter(new CustomAdapter(getApplicationContext(),R.layout.row,personsList));
                    return;
                }
                for (DocumentSnapshot document : documentSnapshots) {
                    String name = (String) document.get("Name");
                    String age = (String)  document.get("Age");
                    String personId = (String)  document.get("PersonId");
                    personsList.add(new Persons(name,age, personId));
                }
                personsListView.setAdapter(new CustomAdapter(getApplicationContext(),R.layout.row,personsList));
            }catch (Exception ex){
                    String msg = ex.getMessage();
                }
            }
        });

    }

    /*private void getPersons(){

        myDB.collection("persons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        personsList  = new ArrayList<Persons>();
                        names  = new ArrayList<String >();
                        int i =0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                personsList.add(new Persons(document.get("Name").toString(), document.get("Age").toString(), document.get("PersonId").toString()));
                                names.add(document.get("Name").toString());
                            }
                            String [] n = new String[names.size()];
                            n=  names.toArray(n);
                            //String[] namesArray = (String[]) names.toArray();
                            //ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,n);
                            // personsListView.setAdapter(adapter);
                            personsListView.setAdapter(new CustomAdapter(getApplicationContext(),R.layout.row,personsList));
                        } else {
                        }
                    }
                });
        //return names;
    }*/
    public void addBtnClicked(View view) {
        try {
            myDB = FirestoreServices.getInstance();
            //myDB = FirebaseFirestore.getInstance();
            CollectionReference personsRef = myDB.collection("persons");
            Map<String, Object> person_map = new HashMap<>();
            String name = nameEditText.getText().toString();
            String age = ageEditText.getText().toString();
            String personId = personIdEditText.getText().toString();

            person_map.put("Name", name);
            person_map.put("Age", age);
            person_map.put("PersonId", personId);
            personsRef.add(person_map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
    }catch (Exception ex){
        String msg = ex.getMessage();
    }
}

    public void getAllBtnClicked(View view) {
        getPersons();
    }
}
