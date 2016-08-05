package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddCardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    final static String DB_Url="https://appkwarti.firebaseio.com/";

    EditText editName,editCompanyName,editCardNumber,editBrand,editDescription;


    ListView lv;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FireBaseHelper helper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Snackbar.make(view, "will be added soon add a photo ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });








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


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
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

}
