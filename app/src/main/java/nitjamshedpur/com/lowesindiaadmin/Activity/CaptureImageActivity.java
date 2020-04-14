package nitjamshedpur.com.lowesindiaadmin.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import nitjamshedpur.com.lowesindiaadmin.MyFileContentProvider;
import nitjamshedpur.com.lowesindiaadmin.R;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureImageActivity extends Activity {

    private final int CAMERA_RESULT = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button permission;
    ImageView imageView1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        permission=findViewById(R.id.permission);
        imageView1 = (ImageView)findViewById(R.id.imageView1);

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    //main logic or main code
                    openCamera();
                } else {
                    requestPermission();
                }
            }
        });


    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openCamera();
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CaptureImageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void openCamera() {
        PackageManager pm = getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
            startActivityForResult(i, CAMERA_RESULT);
        } else {
            Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            File out = new File(getFilesDir(), "newImage.jpg");
            if(!out.exists()) {
                Toast.makeText(getBaseContext(),"Error while capturing image!", Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
            //rotating bitMap by 90 degrees
            float degrees = 90;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            Bitmap output=Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            imageView1.setImageBitmap(output);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView1 = null;
    }

}
