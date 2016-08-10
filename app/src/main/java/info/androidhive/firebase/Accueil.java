package info.androidhive.firebase;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //___________________________ Fire Base_______________________________________


    final static String DB_Url="https://appkwarti.firebaseio.com/";
    DatabaseReference db;

    FireBaseHelper helper;

    //___________________________________________________________________________________________________



    private ProgressBar progressBar;

    //___________________________  recycle variables _________________________________


    private RecyclerView recyclerView;
    private CardsAdapter adapter;
    private ArrayList<Card> cardList;



    //___________________________ ____________________ _________________________________

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//___________________________  recycle init________ _________________________________

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        cardList = new ArrayList<>();
        adapter = new CardsAdapter(this, cardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);


        //prepareCards();

// ___________________________ ____________________ _________________________________

        db= FirebaseDatabase.getInstance().getReference();
        helper= new FireBaseHelper(db);



     //   progressBar.setVisibility(View.VISIBLE);
        this.refreshData();
        recyclerView.setAdapter(adapter);
//****************************



        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String Email = user.getEmail();

        NavigationView navigationView1= (NavigationView) findViewById (R.id.nav_view);
        View header = navigationView1.getHeaderView(0);



        TextView emailuser = (TextView) header.findViewById(R.id.emailuser);
        emailuser.setText(Email);






        System.out.println("*********************************"+Email+"***********************************");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


                Intent intent = new Intent(Accueil.this,AddCardActivity.class);
                startActivity(intent);


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
        getMenuInflater().inflate(R.menu.accueil, menu);
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


            Intent intent = new Intent(Accueil.this,AccountActivity.class);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.home) {

            Intent intent = new Intent(Accueil.this,Accueil.class);
            startActivity(intent);
            this.finish();

        } else if (id == R.id.logout) {

            signOut();
            Intent intent = new Intent(Accueil.this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        else if (id == R.id.addcarte) {

            Intent intent = new Intent(Accueil.this,AddCardActivity.class);
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

/*

    private void prepareCards() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Card a = new Card("card1", "card1", covers[0]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[1]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[2]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[3]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[4]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[5]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[6]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[7]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[8]);
        cardList.add(a);

        a = new Card("card1", "card1", covers[9]);
        cardList.add(a);

        adapter.notifyDataSetChanged();
    }*/


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



    private void refreshData(){

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getUpdates(DataSnapshot dataSnapshot){

     //   progressBar.setVisibility(View.INVISIBLE);

        cardList.clear();

        for(DataSnapshot ds : dataSnapshot.getChildren()){




                Card c = new Card();



                c.setName(ds.getValue(Card.class).getName());

                c.setCardNumber(ds.getValue(Card.class).getCardNumber());
                c.setCardformat(ds.getValue(Card.class).getCardformat());
                c.setCompanyName(ds.getValue(Card.class).getCompanyName());
                c.setDescription(ds.getValue(Card.class).getDescription());
                c.setEmailUser(ds.getValue(Card.class).getEmailUser());
                c.setBrand(ds.getValue(Card.class).getBrand());
                c.setId(ds.getValue(Card.class).getId());
                c.setThumbnail(ds.getValue(Card.class).getThumbnail());

            if(c.getEmailUser().equals(auth.getCurrentUser().getEmail())) {
                cardList.add(c);

            }
        }

        if(cardList.size()>0){

        adapter = new CardsAdapter(Accueil.this,cardList);



        }
    }





}
