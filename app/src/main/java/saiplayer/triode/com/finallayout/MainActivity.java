package saiplayer.triode.com.finallayout;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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

    ArrayList<SongsDataProvider> dataProviders=new ArrayList<SongsDataProvider>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        SlideMenuFragment slideMenuFragment=new SlideMenuFragment();
        fragmentTransaction.add(R.id.drawer_view,slideMenuFragment);
        fragmentTransaction.commit();*/

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


        if(ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQYEST);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQYEST);
            }

        }
        else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            SongsFargment songsFargment=new SongsFargment();
            fragmentTransaction.add(R.id.category_content,songsFargment);
            fragmentTransaction.commit();
            //new loadingSongs().execute();


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case MY_PERMISSION_REQYEST:{
                if (grantResults.length> 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show();

                        SongsFargment songsFargment=new SongsFargment();
                        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction().detach(songsFargment).attach(songsFargment);
                        //FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentManager.add(R.id.category_content,songsFargment);
                        fragmentManager.commit();



                    }
                }
                else
                {
                    Toast.makeText(this,"not granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

    }


    @Override
    public void onSongPressed(int songPosition,ArrayList<SongsDataProvider> providerArrayList) {

        dataProviders=providerArrayList;
        main_drawer_container.setBackground(new BitmapDrawable(getResources(),dataProviders.get(songPosition).getAllAlbumarts()));

    }

}
