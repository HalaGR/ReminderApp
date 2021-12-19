package com.example.locationreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginTabFragment extends Fragment {
       TextView email;
       TextView pass;
       TextView forgetpass;
       Button login;
       ProgressBar progressBar;
       FirebaseAuth fAuth;
       float v=0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState){
            ViewGroup root=(ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
            email=root.findViewById(R.id.email);
            pass=root.findViewById(R.id.pass);
            forgetpass=root.findViewById(R.id.forgetpass);
            login=root.findViewById(R.id.button);
            progressBar=root.findViewById(R.id.progressBar);
            fAuth=FirebaseAuth.getInstance();

            email.setTranslationX(800);
            pass.setTranslationX(800);
            forgetpass.setTranslationX(800);
            login.setTranslationX(800);

            email.setAlpha(v);
            pass.setAlpha(v);
            forgetpass.setAlpha(v);
            login.setAlpha(v);
            email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
            pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            forgetpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mEmail=email.getText().toString().trim();
                    String mPass=pass.getText().toString().trim();

                    if (TextUtils.isEmpty(mEmail)){
                        email.setError("Email is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(mPass)){
                        pass.setError("Password is Required.");
                        return;
                    }
                    if(mPass.length()<6) {
                        pass.setError("Password Must be more that 5 Characters");
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                     //authenticate the user
                    fAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(LoginTabFragment.this.getActivity(),"Logged in Successfully", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getContext(),home_page_Activity.class));
                           }
                           else{
                               Toast.makeText(LoginTabFragment.this.getActivity(), "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.GONE);
                           }
                        }
                    });

                }
            });
            forgetpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText resetMail=new EditText(view.getContext());
                    AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                    passwordResetDialog.setTitle("Reset Password ?");
                    passwordResetDialog.setMessage("Enter Your Email To Received Reset Link. ");
                    passwordResetDialog.setView(resetMail);

                    passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // extract the email and send reset link
                            if(resetMail.getText().toString().matches("")){
                                Toast.makeText(LoginTabFragment.this.getActivity(),"Email is Required.", Toast.LENGTH_SHORT).show();


                            }else {
                                String mail = resetMail.getText().toString();
                                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(@NonNull Void unused) {
                                        Toast.makeText(LoginTabFragment.this.getActivity(), "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginTabFragment.this.getActivity(), "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //close the dialog
                        }
                    });
                    passwordResetDialog.create().show();

                }
            });
            return root;
        }

    }
