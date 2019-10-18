package com.danapps.polytech;

import com.google.firebase.auth.FirebaseAuth;

public class Auth {

    public void tryAuth(String email, String pass, boolean result) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass);
    }
}
