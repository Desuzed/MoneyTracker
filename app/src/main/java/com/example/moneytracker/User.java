package com.example.moneytracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String UID = currentUser.getUid();
    public static int maxID = 0;


    public String getUID() {
        return UID;
    }

}
