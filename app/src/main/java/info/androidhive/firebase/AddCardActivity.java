package info.androidhive.firebase;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddCardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//_____________________________________camera el 5afeya_______________________________________



    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri; // file url to store image/video


    Bitmap imagebit;


//____________________________________________________________________________________________



    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    final static String DB_Url="https://appkwarti.firebaseio.com/";

    EditText editName,editCompanyName,editCardNumber,editBrand,editDescription;


    ListView lv;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FireBaseHelper helper;
    String cardbrFormat;

    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //_________________________________________ camera el 5afeya init _______________________


        imageView = (ImageView) findViewById(R.id.imageView);

        //________________________________________________________________________________________













//Setup firebase

        db= FirebaseDatabase.getInstance().getReference();
        helper= new FireBaseHelper(db);



        editCardNumber = (EditText) findViewById(R.id.cardNumber);
        editName = (EditText) findViewById(R.id.Name);
        editCompanyName = (EditText) findViewById(R.id.CompanyName);
        editBrand = (EditText) findViewById(R.id.brand);
        editDescription = (EditText) findViewById(R.id.description);


        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String Email = user.getEmail();

        NavigationView navigationView1= (NavigationView) findViewById (R.id.nav_view);
        View header = navigationView1.getHeaderView(0);



        TextView emailuser = (TextView) header.findViewById(R.id.emailuser);
        emailuser.setText(Email);




        FloatingActionButton fabvalidate = (FloatingActionButton) findViewById(R.id.validate);
        fabvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name = editName.getText().toString();

                String companyName = editCompanyName.getText().toString();

                String cardNumber = editCardNumber.getText().toString();

                String brand = editBrand.getText().toString();

                String description = editDescription.getText().toString();



                Card card= new Card();
                card.setId("0");
                card.setName(name);
                card.setCompanyName(companyName);
                card.setCardNumber(cardNumber);
                card.setBrand(brand);
                card.setDescription(description);
                card.setEmailUser(user.getEmail());
                card.setCardformat(cardbrFormat);


                //Bitmap bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.img_card);//your image
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                imagebit.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                imagebit.recycle();
                byte[] byteArray = bYtE.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);


                card.setThumbnail(imageFile);







                if((name.length()>0 && name !=null) &&
                        (companyName.length()>0 && name !=null) &&
                        (cardNumber.length()>0 && name !=null) &&
                        (brand.length()>0 && name !=null)){


                    if(helper.save(card)){

                        editName.setText("");
                        editCompanyName.setText("");
                        editCardNumber.setText("");
                        editBrand.setText("");
                        editDescription.setText("");
                        Intent intent = new Intent(AddCardActivity.this,Accueil.class);
                        startActivity(intent);








                    }



                }
                else{
                    Snackbar.make(view, " entrer tous les champs obligatoires !! ...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                }

                /*Intent intent = new Intent(AddCardActivity.this,Accueil.class);
                startActivity(intent);*/


            }
        });



        FloatingActionButton addphoto = (FloatingActionButton) findViewById(R.id.addphoto);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                captureImage();


               /* Snackbar.make(view, "ya3tikom nam w 7ara 3dham hayka tzedet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }



    private void addCard(String id,String name,String companyName,String cardNumber,String brand,String description,String emailUser){

        Card card= new Card();
        card.setId(id);
        card.setName(name);
        card.setCompanyName(companyName);
        card.setCardNumber(cardNumber);
        card.setBrand(brand);
        card.setDescription(description);
        card.setEmailUser(emailUser);


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                editCardNumber.setText(result.getContents());
                cardbrFormat = result.getFormatName();

            }
        }




       else  if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }





        else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void previewCapturedImage() {
        try {
            // hide video preview


            imageView.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize =1;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imageView.setImageBitmap(bitmap);
            imagebit = bitmap;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }




    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanFromFragment();
                }
            });
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    toast = "Cancelled from fragment";
                } else {
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }









    }














    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_account) {


            Intent intent = new Intent(AddCardActivity.this,AccountActivity.class);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.home) {

            Intent intent = new Intent(AddCardActivity.this,Accueil.class);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.logout) {

            signOut();
            Intent intent = new Intent(AddCardActivity.this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        else if (id == R.id.addcarte) {

            Intent intent = new Intent(AddCardActivity.this,AddCardActivity.class);
            startActivity(intent);
            this.finish();

        }/* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signOut() {
        auth.signOut();
    }


    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }




    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }




}
