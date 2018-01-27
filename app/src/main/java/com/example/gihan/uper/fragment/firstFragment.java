package com.example.gihan.uper.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.gihan.uper.R;
import com.example.gihan.uper.modle.User;
import com.example.gihan.uper.ui.WelcomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class firstFragment extends Fragment {

    Button mLogin, mRegester;
    RelativeLayout rootLayout;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);

        //INITALIZE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mDatabase.getReference("users");


        //INTIALIZE COMPNENT
        mLogin = (Button) v.findViewById(R.id.btn_first_fragment_sign);
        mRegester = (Button) v.findViewById(R.id.btn_first_fragment_regester);
        rootLayout = (RelativeLayout) v.findViewById(R.id.root_layout);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();

            }


        });
        mRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegesterDialog();
            }
        });

        return v;
    }


    @SuppressLint("ResourceType")
    private void showRegesterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.first_fragment_regester));
        builder.setMessage(getString(R.string.first_fragment_message));

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View regester_layout = inflater.inflate(R.layout.layout_regester, null);

        final MaterialEditText etEmail = regester_layout.findViewById(R.id.et_regester_email);
        final MaterialEditText etPassword = regester_layout.findViewById(R.id.et_regester_password);
        final MaterialEditText etName = regester_layout.findViewById(R.id.et_regester_name);
        final MaterialEditText etPhone = regester_layout.findViewById(R.id.et_regester_phone);

        builder.setView(regester_layout);

        //SET BUTTON
        builder.setPositiveButton("Regester", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                //Check Validation

                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_email), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_password), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_name), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (etPassword.getText().length() < 6) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_password_length), Snackbar.LENGTH_LONG).show();
                    return;
                }


                try {

                //REGESTER NEW USER
                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //SAVE USER DATA IN DATABASE
                                User user = new User();
                                user.setEmail(etEmail.getText().toString());
                                user.setName(etName.getText().toString());
                                user.setPassword(etPassword.getText().toString());
                                user.setPhone(etPhone.getText().toString());

                                mUser.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_sucessfully_regester), Snackbar.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout, " failed " + e.getMessage(), Snackbar.LENGTH_LONG).show();

                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, " failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                });

                }catch (Exception ee){
                    String dd= ee.toString();
                }
            }

        });


        builder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        builder.show();
    }


    private void showLoginDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.first_fragment_sign_in));
        builder.setMessage(getString(R.string.first_fragment_message_sign_in));

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText etEmail = login_layout.findViewById(R.id.et_logi_email);
        final MaterialEditText etPassword = login_layout.findViewById(R.id.et_login_password);


        builder.setView(login_layout);

        //SET BUTTON
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                //Check Validation

                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_email), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Snackbar.make(rootLayout, getString(R.string.first_fragment_snackbar_password), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //SIGN IN
                mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                startActivity(new Intent(getContext(), WelcomeActivity.class));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Snackbar.make(rootLayout," failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                    }
                });




            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
