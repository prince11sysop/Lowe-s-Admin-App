package nitjamshedpur.com.lowesindiaadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesindiaadmin.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addItem=(RelativeLayout)findViewById(R.id.addItem);
//        updateItemDetails=(Button)findViewById(R.id.updateItemDetails);
//        captureImage=findViewById(R.id.captureImage);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddItemActivity.class));
            }
        });

//        updateItemDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, UpdateItemDetailsActivity.class));
//
//            }
//        });
//
//        captureImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,CaptureImageActivity.class));
//            }
//        });
    }
}
