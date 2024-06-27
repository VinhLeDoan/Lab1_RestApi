package com.example.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore database;
    Button btnInsert, btnUpdate, btnDelete;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult=findViewById(R.id.demo151tv1);
        btnInsert=findViewById(R.id.demo151btnInsert);
        database = FirebaseFirestore.getInstance();// khoi tao database
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFileBase(tvResult);
            }
        });
        btnUpdate=findViewById(R.id.demo151btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirebase(tvResult);
            }
        });
        btnDelete=findViewById(R.id.demo151btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFirebase(tvResult);
            }
        });
        SelectDataFromFirebase(tvResult);
    }

    String id="";
    ToDo toDo = null;


    public void insertFileBase(TextView tvResult){
        id = UUID.randomUUID().toString();//lay 1 id bat ky
        //tao doi tuong de insert
        toDo = new ToDo(id, "title 2", "content 2");
        //Chuyen doi sang doi tuong co the thao tac voi filebase
        HashMap<String, Object> mapTodo = toDo.convertHashMap();
        //insert vao database
        database.collection("TODO").document(id)
                .set(mapTodo)   // doi tuong can insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Them thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void updateFirebase(TextView tvResult){
        id="524be348-2ab7-40d9-960f-2fe849a39378";
        toDo=new ToDo(id, "Sua title 1", "Sua content 1");
        database.collection("TODO").document(toDo.getId()).update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Sua thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void deleteFirebase(TextView tvResult){
        id="524be348-2ab7-40d9-960f-2fe849a39378";
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Xoa thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    String strResult="";
    public ArrayList<ToDo> SelectDataFromFirebase(TextView tvResult){
        ArrayList<ToDo> list=new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){ //sau khi lay du lieu thanh cong
                            strResult="";
                            for (QueryDocumentSnapshot document: task.getResult()){
                                //chuyen dong doc dc sang doi tuong
                                ToDo toDo1=document.toObject(ToDo.class);
                                //chuyen doi tuong thanh chuoi
                                strResult += "Id: "+toDo1.getId()+"\n";
                                list.add(toDo1); // them vao list

                            }
                            //hien thi ket qua
                            tvResult.setText(strResult);
                        }
                        else {
                            tvResult.setText("Doc du lieu that bai");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return list;
    }
}