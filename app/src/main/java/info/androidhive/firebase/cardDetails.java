package info.androidhive.firebase;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class cardDetails extends AppCompatActivity {


    Dialog dialog;
TextView cardbarenumber;
    TextView CompanyName,Name,Brand,Description;
ImageView detailImage,BareCodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);









        dialog=new Dialog(this);

        //SET TITLE
        dialog.setTitle("Bar Code");

        //set content
        dialog.setContentView(R.layout.dialog_l);

        //cardbarenumber= (TextView) dialog.findViewById(R.id.bareCardNumber);
        BareCodeImage = (ImageView)dialog.findViewById(R.id.imageView);
        // cardbarenumber.setText(SharedInfo.cardShared.getCardNumber()+" "+" " +" "+ "Format = "+SharedInfo.cardShared.getCardformat() );

        try {
            generateQRCode_general(SharedInfo.cardShared.getCardNumber(),SharedInfo.cardShared.getCardformat(),BareCodeImage);

        } catch (WriterException e) {
            e.printStackTrace();
        }













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

       /* try {
            generateQRCode_general("blou9af",detailImage);
        } catch (WriterException e) {
            e.printStackTrace();
        }*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

              /*  Context context = getApplicationContext();
                Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
                intent.putExtra("ENCODE_TYPE", "barecode");
                intent.putExtra("ENCODE_DATA", "12345678901");
                intent.putExtra("ENCODE_FORMAT", "UPC_A");
                startActivity(intent);

*/



                dialog.show();
            }
        });
    }



    private void generateQRCode_general(String data,String format, ImageView img)throws WriterException {
        com.google.zxing. MultiFormatWriter writer =new MultiFormatWriter();


        String finaldata = Uri.encode(data, "utf-8");

        BitMatrix bm = writer.encode(finaldata, BarcodeFormat.valueOf(format),150, 150);
        Bitmap ImageBitmap = Bitmap.createBitmap(240, 100, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 240; i++) {//width
            for (int j = 0; j < 100; j++) {//height
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        if (ImageBitmap != null) {
            BareCodeImage.setImageBitmap(ImageBitmap);
        } else {
          /*  Toast.makeText(getApplicationContext(), getResources().getString(R.string.userInputError),
                    Toast.LENGTH_SHORT).show();*/
        }

    }









}
