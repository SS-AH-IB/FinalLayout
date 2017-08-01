package saiplayer.triode.com.finallayout;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import static saiplayer.triode.com.finallayout.R.id.drawer_view;

public class MainActivity extends FragmentActivity implements SongsFargment.OnSongPressedListener{

    private static final int MY_PERMISSION_REQYEST=1;

    ActionBarDrawerToggle actionBarDrawerToggle;

    DrawerLayout main_drawer_container;

    FrameLayout category_view;

    View drawerView;
    View drawerContent;
    View mainContent;
    View drawer_top_section_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        category_view=(FrameLayout)findViewById(R.id.category_content);

        main_drawer_container=(DrawerLayout) findViewById(R.id.main_drawer_container);

        Bitmap default_bitmap_background=BlurBitmap.Blur(this, BitmapFactory.decodeResource(getResources(),R.drawable.reside_background_1));
        BitmapDrawable default_drawable_background=new BitmapDrawable(getResources(),default_bitmap_background);
        main_drawer_container.setBackground(default_drawable_background);

        drawerView=(View)findViewById(drawer_view);
        drawerContent=(View)findViewById(R.id.drawer_content);
        mainContent=(View)findViewById(R.id.main_content);
        drawer_top_section_view=(View)findViewById(R.id.drawer_top_section_view);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,main_drawer_container,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                drawerContent.setX(drawerView.getWidth()*(1-slideOffset));
                mainContent.setX(drawerView.getWidth()*slideOffset);
            }
        };

        main_drawer_container.setDrawerListener(actionBarDrawerToggle);

        main_drawer_container.closeDrawer(drawerView);
        drawerContent.setX(drawerView.getWidth());

        int positionX=(default_bitmap_background.getWidth()/2);
        int positionY=((3*default_bitmap_background.getHeight())/4);

        /*drawer_background_imageview.buildDrawingCache();
        Bitmap background_cache=drawer_background_imageview.getDrawingCache();*/


        int pixelColor=default_bitmap_background.getPixel(positionX,positionY);

        int background_alpha= Color.alpha(pixelColor);
        int background_red= Color.red(pixelColor);
        int background_green= Color.green(pixelColor);
        int background_blue= Color.blue(pixelColor);
        background_alpha*=0.25;

        GradientDrawable gradientDrawable=(GradientDrawable)drawer_top_section_view.getBackground();
        gradientDrawable.setColor(pixelColor);
        pixelColor=Color.argb(background_alpha,background_red,background_green,background_blue);
        drawerContent.setBackgroundColor(pixelColor);


        //drawerContent.setAlpha((float) 0.40);


        SongsFargment songsFargment=new SongsFargment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.category_content,songsFargment);
        fragmentTransaction.commit();

    }


    @Override
    public void onSongPressed(int songPosition, ArrayList<String> allSongTitle, ArrayList<String> allArtists, ArrayList<String> songDetails, ArrayList<String> allSongPaths, ArrayList<Long> allSongAlbum_ID, Cursor songCursor) {




    }
}
