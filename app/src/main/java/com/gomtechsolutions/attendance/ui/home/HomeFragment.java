package com.gomtechsolutions.attendance.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gomtechsolutions.attendance.AndroidBMPUtil;
import com.gomtechsolutions.attendance.DBHandler;
import com.gomtechsolutions.attendance.R;
import com.gomtechsolutions.attendance.ScanActivity;
import com.gomtechsolutions.attendance.databinding.FragmentHomeBinding;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import asia.kanopi.fingerscan.Status;

import static android.app.Activity.RESULT_OK;
//import static asia.kanopi.fingerscan.UruImage.IMAGE_WIDTH;

public class HomeFragment extends Fragment {
    private static final int IMAGE_HEIGHT = 290;
    private static final int IMAGE_WIDTH = 384;
    ImageView ivFinger, ivTest;
    TextView tvMessage;
    byte[] img, probeFinger;
    Bitmap bm;
    Dialog dialog;
    Button scanClick,cm, Save, cancel;
    EditText c_code, c_title;
    DBHandler dbHandler;
    ArrayList<ArrayList<String>> stdList;
    private static final int SCAN_FINGER = 0;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHandler = new DBHandler(getContext());
        stdList = new ArrayList<>();
        dialog = new Dialog(getContext());
        String code, title;

        tvMessage = (TextView) root.findViewById(R.id.tvMessage);
        ivFinger = (ImageView) root.findViewById(R.id.ivFingerDisplay);
        ivTest = root.findViewById(R.id.ivTestDisplay);
        scanClick = root.findViewById(R.id.buttonScan);
        cm = root.findViewById(R.id.com);
        cm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                takeAttendance();
               // Toast.makeText(getContext(), "shiru", Toast.LENGTH_SHORT).show();

            }
        });
        scanClick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                dialog.setContentView(R.layout.activity_dialog_box2);
                dialog.create();
                dialog.show();
                Save = dialog.findViewById(R.id.add_course_save);
                cancel = dialog.findViewById(R.id.add_course_cancel);
                c_code = dialog.findViewById(R.id.course_code);
                c_title = dialog.findViewById(R.id.course_title);
                Save.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                    if (c_code.getText().toString().isEmpty() || c_title.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(), "Please add course details", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getActivity(), ScanActivity.class);
                        intent.putExtra("c_details", c_code.getText().toString());
                        intent.putExtra("c_details1", c_title.getText().toString());
                        startActivityForResult(intent, SCAN_FINGER);
                    }

                }
                });
               cancel.setOnClickListener(view1 -> dialog.dismiss());

            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void takeAttendance(){
        stdList = dbHandler.readStudent();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> fingers = new ArrayList<>();
        ArrayList<String> reg_nos = new ArrayList<>();

        fingers.add(String.valueOf(stdList.get(1)));
        byte[] finger = fingers.get(0).getBytes();
        Toast.makeText(getContext(),"Welcome!! Attendance taken",Toast.LENGTH_LONG).show();

       // Toast.makeText(getContext(),"Welcome!!  "+ ,Toast.LENGTH_LONG).show();

        Bitmap bm4 = BitmapFactory.decodeByteArray(finger, 0, finger.length);
       // Toast.makeText(getActivity(), bm4.toString(), Toast.LENGTH_SHORT).show();
        //byte[]  anothertry = bitmapToBase64(bm4, Bitmap.CompressFormat.JPEG, 100);
        ivTest.setImageBitmap(bm4);
//        if(compare(convert,finger)){
//                        dbHandler.addAttendance(name,reg_no);
//                        Toast.makeText(getContext(),"Welcome!!  "+name,Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(getContext(),"Finger mismatch!!",Toast.LENGTH_LONG).show();
//                    }

//        for(int i = 0; i < stdList.size(); i++){
//            names.add(String.valueOf(stdList.get(0)));
//            fingers.add(String.valueOf(stdList.get(1)));
//            reg_nos.add(String.valueOf(stdList.get(2)));
//            for(int j =0; j < fingers.size(); j++){
//                String name = names.get(j);
//                byte[] finger = fingers.get(j).getBytes();
//
//                String reg_no = reg_nos.get(j);
////                if(compare(img,finger)){
////                    dbHandler.addAttendance(name,reg_no);
////                    Toast.makeText(getContext(),"Welcome!!  "+name,Toast.LENGTH_LONG).show();
////                }else {
////                    Toast.makeText(getContext(),"Finger mismatch!!",Toast.LENGTH_LONG).show();
////                }
//
//               // Toast.makeText(getContext(), "value of img: " + Arrays.toString(img), Toast.LENGTH_SHORT).show();
//                if (!(finger == null)){
//                    //Toast.makeText(getContext(), "image isnt null", Toast.LENGTH_SHORT).show();
//                    //byte[] decodedImageBytes = Base64.decode(img, Base64.DEFAULT);
//                    byte[]  convert = AndroidBMPUtil.convertToBmp24bit(img);
//                    byte[]  convert1 = AndroidBMPUtil.convertToBmp24bit(finger);
//
//                    Bitmap bm4 = BitmapFactory.decodeByteArray(finger, 0, finger.length);
//                    //byte[]  anothertry = bitmapToBase64(bm4, Bitmap.CompressFormat.JPEG, 100);
//                    ivTest.setImageBitmap(bm4);
//                    Toast.makeText(getActivity(), Arrays.toString(convert), Toast.LENGTH_SHORT).show();
//
//                    if(compare(convert,finger)){
//                        dbHandler.addAttendance(name,reg_no);
//                        Toast.makeText(getContext(),"Welcome!!  "+name,Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(getContext(),"Finger mismatch!!",Toast.LENGTH_LONG).show();
//                    }
//                }else {
//                    Toast.makeText(getContext(),"Please take attendance first!!",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }
    private boolean compare(byte[] probe, byte[] candidate) {
        FingerprintImage probeImg,candidateImg;
        //byte[] decodedImageBytes = Base64.decode(probe, Base64.DEFAULT);
        //byte[] decodedImageBytes1 = Base64.decode(candidate, Base64.DEFAULT);
        double threshold = 5.0;
        Bitmap bm4 = BitmapFactory.decodeByteArray(candidate, 0, candidate.length);
        byte[]  anothertry = bitmapToBase64(bm4, Bitmap.CompressFormat.JPEG, 100);
        probeImg = new FingerprintImage(probe, new FingerprintImageOptions());
        candidateImg = new FingerprintImage(anothertry, new FingerprintImageOptions());

        FingerprintTemplate probeTemplate,candidateTemplate;
        probeTemplate = new FingerprintTemplate(probeImg);
        candidateTemplate = new FingerprintTemplate(candidateImg);

        double score = new FingerprintMatcher(probeTemplate).match(candidateTemplate);

        if (score >= threshold){
            return true;
        }else {
            return false;
        }



        //while (result.next()) {
//        byte[] imgCandidate = probe;
//        byte[] canCandidate = candidate;
//       // byte[] decodedImageBytes = Base64.decode(probe, Base64.DEFAULT);
//        FingerprintTemplate candidate1 = new FingerprintTemplate()
//                .dpi(500)
//                .create(imgCandidate);
//
//        FingerprintTemplate probeimg = new FingerprintTemplate().dpi(500).create(canCandidate);
//
//        double score = new FingerprintMatcher()
//                .index(probeimg)
//                .match(candidate1);
//
//        if (score >= 5.0) {
//            return  true;
//            // Found a match
//        }else{
//            return false;
//        }
        //}
    }

    public static byte[] bitmapToBase64(Bitmap image, Bitmap.CompressFormat  compressFormat, int quality)//u can pass 100 in quality or any integer
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        //return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        return Base64.encode(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        int status;
        String errorMesssage;
        switch(requestCode) {
            case (SCAN_FINGER) : {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        tvMessage.setText("Fingerprint captured");
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        ivFinger.setImageBitmap(bm);
//                        if(img != null){
//                            takeAttendance(); --
//                        }else {
//                            Toast.makeText(getContext(), "Damage", Toast.LENGTH_SHORT).show();
//                        }

                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        tvMessage.setText("-- Error: " +  errorMesssage + " --");
                    }
                }
                break;
            }
        }
    }

//   private static final int IMAGE_HEIGHT = 290;
//    private static final int IMAGE_WIDTH = 384;
}