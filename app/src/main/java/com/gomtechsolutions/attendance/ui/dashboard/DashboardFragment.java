package com.gomtechsolutions.attendance.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gomtechsolutions.attendance.AndroidBMPUtil;
import com.gomtechsolutions.attendance.DBHandler;
import com.gomtechsolutions.attendance.R;
import com.gomtechsolutions.attendance.ScanActivity;
import com.gomtechsolutions.attendance.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asia.kanopi.fingerscan.Status;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {


    Button signUp;
    TextInputEditText email_input, dept_input, course_input, reg_no_input, full_name;
    AlertDialog.Builder builder;
    AlertDialog[] alertDialog;
    ImageView left_thump_iv, right_thumb_iv;
    CardView left_card, right_card;

    FingerprintTemplate fingerprintTemplate;
    DBHandler dbHandler;
    byte[] img1, img2;
    Bitmap left_finger, right_finger;
    private static final int SCAN_FINGER = 0;
    private static final int SCAN_FINGERS = 1;

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
String[] courses={"male","female"};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setUpUi(root);
        Spinner spin=root.findViewById(R.id.gender);
        spin.setOnItemSelectedListiner(this);
        arrayAdapter ad=new arrayAdapter(this,
        android.R.simple_spinner_item,courses);
        ad.setDropDownViewResourses(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
        dbHandler = new DBHandler(getContext());
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserInfo(view);
            }
        });


//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void setUpUi(View root) {
        email_input = root.findViewById(R.id.signUp_email_edit_text);
        dept_input = root.findViewById(R.id.department_edit_text);
        course_input = root.findViewById(R.id.courseOfStudy_edit_text);
        signUp = root.findViewById(R.id.submit_button);
        reg_no_input = root.findViewById(R.id.signUp_phone_edit_text);
        full_name = root.findViewById(R.id.signUp_name_edit_text);
//        fingerleft = root.findViewById(R.id.left_card);
//        fingerright = root.findViewById(R.id.right_card);

    }

    private void validateUserInfo(final View view){
//        getBioInfo();
        if(TextUtils.isEmpty(Objects.requireNonNull(email_input.getText()).toString().trim()) || TextUtils.isEmpty(dept_input.getText().toString().trim()) || TextUtils.isEmpty(full_name.getText().toString().trim()) || TextUtils.isEmpty(reg_no_input.getText().toString().trim())){
            Snackbar.make(view,"Field Can't be empty", Snackbar.LENGTH_LONG).show();
        }
        else if(!emailValidator(email_input.getText().toString()))
        {
            email_input.setError("Please Enter Valid Email Address");
        }
        else {
            getBioInfo();
        }
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN;
        EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void getBioInfo(){
        builder = new AlertDialog.Builder(this.getContext());
        alertDialog = new AlertDialog[1];
        LayoutInflater inflater = getLayoutInflater();
        View myView = inflater.inflate(R.layout.layout_take_bio, null);
        builder.setView(myView);
        builder.setCancelable(false);
        builder.setTitle("Capture Finger Prints");
//        final CardView left_card, right_card;
        left_card = myView.findViewById(R.id.left_card);
        right_card = myView.findViewById(R.id.right_card);
        left_thump_iv = myView.findViewById(R.id.left_thump_iv);
        right_thumb_iv = myView.findViewById(R.id.right_thump_iv);
        left_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,"Left Card", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(intent, SCAN_FINGER);
            }
        });
        right_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,"Right Card", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(intent, SCAN_FINGERS);
            }
        });
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(img1 != null && img2 != null){ ;
//                    compare(img1,img2);
                    left_finger = BitmapFactory.decodeByteArray(img1,0,img1.length);
                    right_finger = BitmapFactory.decodeByteArray(img2,0,img2.length);
                    Toast.makeText(getActivity(), left_finger.toString(), Toast.LENGTH_SHORT).show();

                    dbHandler.addStudent(dept_input.getText().toString().trim(),course_input.getText().toString().trim(),reg_no_input.getText().toString().trim(),full_name.getText().toString().trim(),email_input.getText().toString().trim(),left_finger,right_finger);
                    Toast.makeText(getContext(), "Added Successfully!!", Toast.LENGTH_SHORT).show();
                    dept_input.setText("");
                    course_input.setText("");
                    reg_no_input.setText("");
                    full_name.setText("");
                    email_input.setText("");
                    img2 = null;
                    img1 = null;
                }else {
                    Snackbar.make(getView(),"Both Fingerprint Must be captured",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog[0].dismiss();
            }
        });
        alertDialog[0] = builder.create();
        alertDialog[0].show();
    }

    private void compare(byte[] probe, byte[] candidate) {


        FingerprintImage probeImg,candidateImg;
        double threshold = 10.0;
        probeImg = new FingerprintImage(probe, new FingerprintImageOptions());
        candidateImg = new FingerprintImage(candidate, new FingerprintImageOptions());

        FingerprintTemplate probeTemplate,candidateTemplate;
        probeTemplate = new FingerprintTemplate(probeImg);
        candidateTemplate = new FingerprintTemplate(candidateImg);

//        FingerprintTemplate prob = FingerprintCompatibility.importTemplate(probeTemplate.toByteArray());
//        FingerprintTemplate candidate1 = FingerprintCompatibility.importTemplate(candidateTemplate.toByteArray());

        double score = new FingerprintMatcher(probeTemplate).match(candidateTemplate);

        if (score >= threshold){
            Snackbar.make(getView(),"Congratulation Fingers Matched",Snackbar.LENGTH_LONG).show();
        }else {
            Log.i("Thrshold ",""+threshold);
            Snackbar.make(getView(),"oops!!! Fingers Miss Matched >>"+score,Snackbar.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        int status;
        String errorMesssage;
        switch(requestCode) {
            case (SCAN_FINGER): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Snackbar.make(getView(), "Left Fingerprint captured", Snackbar.LENGTH_LONG).show();
                        img1 = data.getByteArrayExtra("img");
                        //byte[]  convert1 = AndroidBMPUtil.convertToBmp24bit(img1);
                        left_finger = BitmapFactory.decodeByteArray(img1, 0, img1.length);
                        Toast.makeText(getActivity(), left_finger.toString(), Toast.LENGTH_SHORT).show();
                        left_thump_iv.setImageBitmap(left_finger);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Snackbar.make(getView(), "Error: " + errorMesssage + "--", Snackbar.LENGTH_LONG).show();

                    }
                }
                break;
            }case (SCAN_FINGERS) : {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Snackbar.make(getView(),"Right Fingerprint captured",Snackbar.LENGTH_LONG).show();
                        img2 = data.getByteArrayExtra("img");
                        //byte[]  convert1 = AndroidBMPUtil.convertToBmp24bit(img2);
                        right_finger = BitmapFactory.decodeByteArray(img2, 0, img2.length);
                        right_thumb_iv.setImageBitmap(right_finger);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Snackbar.make(getView(),"Error: "+ errorMesssage + "--",Snackbar.LENGTH_LONG).show();

                    }
                }
                break;
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        int status;
//        String errorMesssage;
//        switch(requestCode) {
//            case (SCAN_FINGER) : {
//                if (resultCode == RESULT_OK) {
//                    status = data.getIntExtra("status", Status.ERROR);
//                    if (status == Status.SUCCESS) {
//                        Snackbar.make(getView(),"Left Fingerprint captured",Snackbar.LENGTH_LONG).show();
//                        img1 = data.getByteArrayExtra("img");
//                        //byte[]  convert1 = AndroidBMPUtil.convertToBmp24bit(img1);
//                        left_finger = BitmapFactory.decodeByteArray(img1, 0, img1.length);
//                        fingerleft.setImageBitmap(left_finger);
//                    } else {
//                        errorMesssage = data.getStringExtra("errorMessage");
//                        Snackbar.make(getView(),"Error: "+ errorMesssage + "--",Snackbar.LENGTH_LONG).show();
//
//                    }
//                }
//                break;
//            }
//            case (SCAN_FINGERS) : {
//                if (resultCode == RESULT_OK) {
//                    status = data.getIntExtra("status", Status.ERROR);
//                    if (status == Status.SUCCESS) {
//                        Snackbar.make(getView(),"Right Fingerprint captured",Snackbar.LENGTH_LONG).show();
//                        img2 = data.getByteArrayExtra("img");
//                        //byte[]  convert1 = AndroidBMPUtil.convertToBmp24bit(img2);
//                        right_finger = BitmapFactory.decodeByteArray(img2, 0, img2.length);
//                        fingerright.setImageBitmap(right_finger);
//                    } else {
//                        errorMesssage = data.getStringExtra("errorMessage");
//                        Snackbar.make(getView(),"Error: "+ errorMesssage + "--",Snackbar.LENGTH_LONG).show();
//
//                    }
//                }
//                break;
//            }
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}