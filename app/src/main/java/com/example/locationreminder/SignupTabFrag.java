package com.example.locationreminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SignupTabFrag extends Fragment {
    TextView email;
    TextView mobilenum;
    TextView pass;
    TextView confpass;
    Button signup;
    float v=0;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container , Bundle savedInstanceState){
            ViewGroup root=(ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container, false);
            email=root.findViewById(R.id.email);
            mobilenum=root.findViewById(R.id.mobilenum);
            pass=root.findViewById(R.id.pass);
            confpass=root.findViewById(R.id.confpass);
            signup=root.findViewById(R.id.button);

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

            return root;
        }

    }
