package info.androidhive.firebase;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by yassi on 08/08/2016.
 */
public class PicassoClient {

    public static void downloadImage(Context c, String Url, ImageView img){


        if(Url!=null && Url.length()>0 ){

            Picasso.with(c).load(Url).placeholder(R.drawable.img_card).into(img);

        }
        else {

            Picasso.with(c).load(R.drawable.img_card).into(img);
        }


    }
}
