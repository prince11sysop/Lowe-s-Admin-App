package nitjamshedpur.com.lowesindiaadmin.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesindiaadmin.R;

public class UploadImage extends AppCompatActivity {

    private Uri filePath;
    private  final int PICK_IMAGE_REQUEST=71;
    Button chooseBtn,uploadBtn;
    ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    static  String imageUrl;
    String ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        setTitle("Upload Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    chooseBtn=(Button)findViewById(R.id.chooseImage);
    uploadBtn=(Button)findViewById(R.id.uploadImage);
    imageView=(ImageView)findViewById(R.id.imageView);

    storage= FirebaseStorage.getInstance();
    storageReference=storage.getReference();

    chooseBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseImage();
        }
    });
    uploadBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uploadImage();
        }
    });
    }

    //to upload the chosen image
    private void uploadImage() {

        if(filePath!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //"images/"+UUID.randomUUID().toString()
           final StorageReference ref=storageReference.child("images/"+System.currentTimeMillis()+"."+getFileExtension(filePath));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl=uri.toString();
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadImage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage((int)progress+"% uploaded");
                        }
                    });
        }else{
            //user haven't chosen an image
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    //to choose an image
    private void chooseImage() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null&& data.getData()!=null ){
            filePath=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }catch(Exception e){

            }
        }
    }
    //to get file extension from image like jpg
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
