package saiplayer.triode.com.finallayout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dell on 25-Jul-17.
 */

public class SongsFargment extends Fragment implements SongsRecyclerAdapter.OnItemClickType {



    ArrayList<String> artistContainer = new ArrayList<String>();
    ArrayList<String> songDetails = new ArrayList<String>();
    ArrayList<File> allSongs = new ArrayList<File>();
    ArrayList<Bitmap> allAlbums = new ArrayList<Bitmap>();
    ArrayList<SongsDataProvider> songsDataProviderArrayList = new ArrayList<SongsDataProvider>();

    private static final int MY_PERMISSION_REQYEST=1;


    RecyclerView recyclerView;
    SongsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


    View view;
    View mainContentView;
    Context context;

    OnSongPressedListener onSongPressedListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.songs_container_fragment,container,false);
        mainContentView=inflater.inflate(R.layout.activity_main,container,false);
        context=container.getContext();


        recyclerView=(RecyclerView)view.findViewById(R.id.list_song_items);

        dostuff();

        return view;

    }



    public void dostuff()
    {
        getmusic();

        LinearLayout main_content_layout=(LinearLayout)mainContentView.findViewById(R.id.main_content);
        main_content_layout.setBackgroundResource(R.drawable.reside_background_2);

        adapter=new SongsRecyclerAdapter(songsDataProviderArrayList,context);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(250);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickType(this);


    }



    public void getmusic()
    {
        ContentResolver contentResolver=getActivity().getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        final Uri ART_CONTENT_URI=Uri.parse("content://media/external/audio/albumart");

        Uri artUri;

        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);
        Cursor albumCursor = contentResolver.query(albumUri,null,null,null,null);

        if(songCursor!=null && albumCursor!=null && songCursor.moveToFirst() && albumCursor.moveToFirst())
        {

            int songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum=songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songYear=songCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            //int songGenre=songCursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);
            int songDuration=songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songPosition=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{

                long songAlbumid=songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                artUri= ContentUris.withAppendedId(ART_CONTENT_URI,songAlbumid);
                Bitmap album_art_bitmap_temp=null;
                Bitmap album_art_bitmap=null;
                try{

                    album_art_bitmap_temp=(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),artUri));
                    album_art_bitmap=album_art_bitmap_temp.copy(Bitmap.Config.RGB_565,false);


                }
                catch (Exception e){

                    e.printStackTrace();
                }

                String currentArtist= songCursor.getString(songArtist);
                String currentAlbum= songCursor.getString(songAlbum);
                String currentYear= songCursor.getString(songYear);
                //String currentGenre= songCursor.getString(songGenre);
                double totalDuration = Double.parseDouble(songCursor.getString(songDuration))/60000;
                long durationMinute = (long) totalDuration;
                long durationSecond = (long) ((totalDuration-durationMinute)*60);
                String currentDuration= String.valueOf(durationMinute)+":"+String.valueOf(durationSecond)+ "mins";
                String currentTitle = songCursor.getString(songTitle);

                String songDetailString=currentArtist + " | " + currentYear + " | " + currentDuration;

                artistContainer.add(currentArtist);
                //songDetails.add(currentTitle + "\n" + currentAlbum + "." + currentYear + "." + currentDuration + "\n" + currentGenre);
                File singlesong=new File(songCursor.getString(songPosition));

                //allSongs.add(singlesong);
                //allAlbums.add(album_art_bitmap);

                SongsDataProvider songsDataProvider=new SongsDataProvider(currentTitle,songDetailString,singlesong,album_art_bitmap);
                songsDataProviderArrayList.add(songsDataProvider);

            }while (songCursor.moveToNext());



        }

    }


    @Override
    public void onSongItemClick(int itemPostition) {

        SongsDataProvider provider = songsDataProviderArrayList.get(itemPostition);
        Toast.makeText(context, provider.getSongDetails() , Toast.LENGTH_SHORT).show();

        onSongPressedListener.onSongPressed(itemPostition,songsDataProviderArrayList);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onSongPressedListener = (OnSongPressedListener) activity;
        }
        catch (Exception exception){

        };

    }




    public interface OnSongPressedListener {

        void onSongPressed(int songPosition,ArrayList<SongsDataProvider> dataProviders);


    }

}
