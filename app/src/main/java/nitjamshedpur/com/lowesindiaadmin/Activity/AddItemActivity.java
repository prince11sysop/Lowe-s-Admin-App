package nitjamshedpur.com.lowesindiaadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import nitjamshedpur.com.lowesindiaadmin.Modal.ListItem;
import nitjamshedpur.com.lowesindiaadmin.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    String[] categoryItems={"Appliances","Tools","Outdoors","Bath","Lighting & Fans","Flooring"};
    String[] floorNum={"GF","1","2","3","4","5","6"};
    Button send,uploadImage;
    Spinner category, floor;
    EditText  subCategory,price, shelf, desc,name,itemCount;
    private DatabaseReference mMessagesDatabaseReference;
    String categoryRes,floorNumRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send=(Button)findViewById(R.id.send);
        uploadImage=findViewById(R.id.uploadImage);
        category=findViewById(R.id.category);
        floor=findViewById(R.id.floor);
        subCategory=findViewById(R.id.subCategory);
        price=findViewById(R.id.price);
        shelf=findViewById(R.id.shelf);
        desc=findViewById(R.id.desc);
        name=findViewById(R.id.name);
        itemCount=findViewById(R.id.itemCount);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UploadImage.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String mImageUrl,mCategory,mSubCategory,mPrice,mFloor,mShelf,mDesc,mName;
            int mItemCount;
            boolean mStatus=false;

            mImageUrl=UploadImage.imageUrl;
            mCategory=categoryRes;
            mSubCategory=subCategory.getText().toString();
            mPrice=price.getText().toString();
            mFloor=floorNumRes;
            mShelf=shelf.getText().toString();
            mDesc=desc.getText().toString();
            mName=name.getText().toString();
            mItemCount=Integer.parseInt(itemCount.getText().toString());

            if(mImageUrl==null){
                Toast.makeText(AddItemActivity.this, "Please upload Image First!", Toast.LENGTH_SHORT).show();
            }else {
                mMessagesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("shopName").child("items");

                String childKey = mMessagesDatabaseReference.push().getKey();

                ListItem listItem = new ListItem(mImageUrl, mCategory, mSubCategory, mPrice, mFloor, mShelf, mDesc, mName, mItemCount, mStatus);

                mMessagesDatabaseReference.child(childKey).setValue(listItem);
                Toast.makeText(AddItemActivity.this, "Item Added successfully!", Toast.LENGTH_SHORT).show();
                send.setText("");
                subCategory.setText("");
                price.setText("");
                shelf.setText("");
                desc.setText("");
                name.setText("");
                itemCount.setText("");
            }
            }
        });

        category.setOnItemSelectedListener(this);
        floor.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter categoryAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categoryItems);
        ArrayAdapter floorAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,floorNum);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        category.setAdapter(categoryAdapter);
        floor.setAdapter(floorAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.category){
            categoryRes=categoryItems[position];
        }
        else if (parent.getId()==R.id.floor){
            floorNumRes=floorNum[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
