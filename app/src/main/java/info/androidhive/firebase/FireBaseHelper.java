package info.androidhive.firebase;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by yassi on 05/08/2016.
 */
public class FireBaseHelper {

  DatabaseReference db;
  Boolean saved=null;
  ArrayList<String> card = new ArrayList<String>();


    public FireBaseHelper(DatabaseReference db) {
        this.db = db;
    }





    public Boolean save(Card card){

            if(card==null){
                saved= false;

            }
                    else {
                        try {
                        db.child("Card").push().setValue(card);
                      saved=true;
                        }
                        catch (DatabaseException e){

                            e.printStackTrace();
                            saved=false;
                        }
                    }


        return saved;

                }



    public ArrayList<String> retrieve(){

        db.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
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

return card;

    }



    private void fetchData(DataSnapshot dataSnapshot){


        for (DataSnapshot ds:dataSnapshot.getChildren()){

            String id= ds.getValue(Card.class).getId();
            String name= ds.getValue(Card.class).getName();
            String companyName= ds.getValue(Card.class).getCompanyName();
            String cardNumber= ds.getValue(Card.class).getCardNumber();
            String brand= ds.getValue(Card.class).getBrand();
            String description= ds.getValue(Card.class).getDescription();
            String emailUser= ds.getValue(Card.class).getEmailUser();

            card.add(brand);
            card.add(cardNumber);
            card.add(companyName);
            card.add(description);
            card.add(emailUser);
            card.add(id);
            card.add(name);

        }
    }



}








