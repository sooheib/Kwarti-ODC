package info.androidhive.firebase;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class cardDetails extends AppCompatActivity {

    Dialog dialog;
TextView cardbarenumber;
    TextView CompanyName,Name,Brand,Description;
ImageView detailImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        createDialog();

        CompanyName =(TextView)findViewById(R.id.detailCompanyName);
        Name =(TextView)findViewById(R.id.detailName);
        Brand =(TextView)findViewById(R.id.detailBrand);
        Description =(TextView)findViewById(R.id.detailDesciption);
        detailImage =(ImageView)findViewById(R.id.imageDetail);


        CompanyName.setText(SharedInfo.cardShared.getCompanyName());

       // cardbarenumber.setText(SharedInfo.cardShared.getCardNumber() );
        Name.setText(SharedInfo.cardShared.getName());

        Brand.setText(SharedInfo.cardShared.getBrand());

        Description.setText(SharedInfo.cardShared.getDescription());

        byte[] decodedString = Base64.decode(SharedInfo.cardShared.getThumbnail(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        detailImage.setImageBitmap(bitmap);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                dialog.show();
            }
        });
    }


    private void createDialog()
    {
        dialog=new Dialog(this);

        //SET TITLE
        dialog.setTitle("Bar Code");

        //set content
        dialog.setContentView(R.layout.dialog_l);

cardbarenumber= (TextView) findViewById(R.id.bareCardNumber);
       // cardbarenumber.setText(SharedInfo.cardShared.getCardNumber()+" "+" " +" "+ "Format = "+SharedInfo.cardShared.getCardformat() );

    }



}
