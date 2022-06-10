package com.flashlight.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    //declaring variables
    TextView flashText;
    ToggleButton toggleButton;
    private CameraManager cameraManager;
    String mCameraId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing variavles

        toggleButton = findViewById(R.id.onOffButton);
        flashText = findViewById(R.id.flashText);

        //setting text to text view

        flashText.setText("FlashLight OFF");

        // checking  flash is available of not if flash is not available then it will show flashNotFoundError()

        boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        if (!isFlashAvailable) {
            flashNotFoundError();
        }


        //finding camera service and camera id
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


        try {
            mCameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        //setting action to toggle button
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                switchFlashLight(isChecked);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchFlashLight(boolean status) {
        try {
            if (status == true) {
                flashText.setText("FlashLight ON");
            } else {
                flashText.setText("FlashLight OFF");
            }
            cameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void flashNotFoundError() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash is not available on this Device.");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.show();

    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();

        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Exit");
        alert.setMessage("Are you sure to exit this app?");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }

        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cencel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Thanks for using this app! ❤", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }
}