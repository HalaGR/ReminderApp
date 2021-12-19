package com.example.locationreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupTabFrag extends Fragment {
    TextView email;
    TextView mobilenum;
    TextView pass;
    TextView confpass;
    Button signup;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;
    public static final String TAG = "";

    float v=0;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState){
            ViewGroup root=(ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container, false);
            email=root.findViewById(R.id.email);
            mobilenum=root.findViewById(R.id.mobilenum);
            pass=root.findViewById(R.id.pass);
            confpass=root.findViewById(R.id.confpass);
            signup=root.findViewById(R.id.button);

            fAuth= FirebaseAuth.getInstance();
            progressBar= root.findViewById(R.id.progressBar);
            fStore=FirebaseFirestore.getInstance();

            if(fAuth.getCurrentUser()!=null){

                startActivity(new Intent(getContext(),home_page_Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();

            }

            email.setTranslationX(800);
            mobilenum.setTranslationX(800);
            pass.setTranslationX(800);
            confpass.setTranslationX(800);
            signup.setTranslationX(800);

            email.setAlpha(v);
            mobilenum.setAlpha(v);
            pass.setAlpha(v);
            confpass.setAlpha(v);
            signup.setAlpha(v);

            email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
            mobilenum.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
            confpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
            signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mEmail=email.getText().toString().trim();
                    String mPass=pass.getText().toString().trim();
                    String mMobilenum=mobilenum.getText().toString().trim();
                    String mConfpass=confpass.getText().toString().trim();

                    if (TextUtils.isEmpty(mEmail)){
                        email.setError("Email is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(mPass)){
                        pass.setError("Password is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(mMobilenum)){
                        mobilenum.setError("NUmber Phone is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(mConfpass)){
                        confpass.setError("Confirm Password is Required.");
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                        email.setError("Please provide valid email !");
                        email.requestFocus();
                        return;
                    }
                    if(mPass.length()<6) {
                        pass.setError("Password Must be more that 5 Characters");
                    return;
                    }
                    if(!mPass.equals(mConfpass)) {
                        confpass.setError("the password confirmation does not match");
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    //register the user in firebase
                    fAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //send verification link
                                FirebaseUser user_V=fAuth.getCurrentUser();
                                user_V.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                      Toast.makeText(SignupTabFrag.this.getActivity(),"Verification Email Has been Sent",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent"+e.getMessage());
                                    }
                                });

                                //create User
                                Toast.makeText(SignupTabFrag.this.getActivity(),"User Created",Toast.LENGTH_SHORT).show();
                                userId=fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference= fStore.collection("users").document(userId);
                                Map<String,Object> user=new HashMap<>();
                                user.put("phone",mMobilenum);
                                user.put("email",mEmail);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                        Log.d(TAG, "onSuccess: user Profile is created ");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+e.toString());
                                    }
                                });
                                startActivity(new Intent(getContext(),home_page_Activity.class));



                            }
                            else{
                                Toast.makeText(SignupTabFrag.this.getActivity(), "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                }
            });

            return root;
        }

    }
