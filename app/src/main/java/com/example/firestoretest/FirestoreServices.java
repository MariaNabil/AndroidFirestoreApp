package com.example.firestoretest;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreServices {
    private static FirebaseFirestore instance;
   /* public static FirebaseFirestore Instance
    {
        get
        {
            return  FirebaseFirestore.getInstance();;
        }
    }*/
   public static FirebaseFirestore getInstance(){ return  instance;}

   public static void Init(){
       instance = FirebaseFirestore.getInstance();
   }
}
