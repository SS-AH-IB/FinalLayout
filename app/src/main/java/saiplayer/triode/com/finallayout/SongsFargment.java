package saiplayer.triode.com.finallayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dell on 25-Jul-17.
 */

public class SongsFargment extends Fragment implements SongsRecyclerAdapter.OnItemClickType{



    ArrayList<String> allSongTitle = new ArrayList<String>();
    ArrayList<String> allArtists = new ArrayList<String>();
    ArrayList<String> songDetails = new ArrayList<String>();
    ArrayList<String> allSongPaths = new ArrayList<String>();
    ArrayList<Long> allSongAlbum_ID = new ArrayList<Long>();

    Cursor songCursor;
    //ArrayList<File> allSongs = new ArrayList<File>();
    //ArrayList<Bitmap> allAlbums = new ArrayList<Bitmap>();
    //ArrayList<SongsDataProvider> songsDataProviderArrayList = new ArrayList<SongsDataProvider>();

    Bundle globalsavedInstanceState;


    RecyclerView recyclerView;
    SongsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


    View view;
    Context context;

    OnSongPressedListener onSongPressedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalsavedInstanceState=savedInstanceState;

        CheckPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.songs_container_fragment,container,false);
        context = container.getContext();
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

            //dostuff();

        }
        return view;
    }

    public class loadingSongs extends AsyncTask<Void,Void,Void> {

        private ProgressDialog dialog;


        @Override
        protected Void doInBackground(Void... voids) {
            getmusic();
            //dostuff();
            return null;
        }

        @Override
        protected void onPreExecute() {

            dialog=new ProgressDialog(getActivity());
            dialog.setTitle("Loading");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            dostuff();
            //Toast.makeText(getContext(),String.valueOf(allAlbums.size()+"  "+artistContainer.size()), Toast.LENGTH_LONG).show();
        }

    }

    public void CheckPermission(){

        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }
            else {
                new loadingSongs().execute();
                //getmusic();
            }
        }
        else {
            new loadingSongs().execute();
            //getmusic();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    //getmusic();
                    //dostuff();
                    new loadingSongs().execute();
                }

                else {
                    CheckPermission();
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void dostuff()
    {

        recyclerView = (RecyclerView)view.findViewById(R.id.list_song_items);
        adapter=new SongsRecyclerAdapter(allSongTitle,songDetails,allSongAlbum_ID,context);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(50);
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
        //Uri albumUri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        final Uri ART_CONTENT_URI=Uri.parse("content://media/external/audio/albumart");

        //Uri artUri;

        songCursor = contentResolver.query(songUri,null,null,null,null);
        //Cursor albumCursor = contentResolver.query(albumUri,null,null,null,null);

        if(songCursor!=null && songCursor.moveToFirst())
        {

            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songYear = songCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            //int songGenre=songCursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songPosition = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{

                long songAlbumid = songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                /*artUri = ContentUris.withAppendedId(ART_CONTENT_URI,songAlbumid);
                Bitmap album_art_bitmap_temp = null;
                Bitmap album_art_bitmap = null;
                try{

                    album_art_bitmap_temp = (MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),artUri));
                    album_art_bitmap=album_art_bitmap_temp.copy(Bitmap.Config.RGB_565,false);

                }
                catch (Exception e){

                    e.printStackTrace();
                }*/

                String currentArtist = songCursor.getString(songArtist);
                String currentAlbum = songCursor.getString(songAlbum);
                String currentYear = songCursor.getString(songYear);
                double totalDuration = Double.parseDouble(songCursor.getString(songDuration))/60000;
                long durationMinute = (long) totalDuration;
                long durationSecond = (long) ((totalDuration-durationMinute)*60);
                String currentDuration = String.valueOf(durationMinute)+":"+String.valueOf(durationSecond)+ "mins";
                String currentTitle = songCursor.getString(songTitle);

                String songDetailString = currentArtist + " | " + currentYear + " | " + currentDuration;

                allSongTitle.add(currentTitle);
                allSongPaths.add(songCursor.getString(songPosition));
                allArtists.add(currentArtist);
                songDetails.add(songDetailString);
                allSongAlbum_ID.add(songAlbumid);

                //File singlesong = new File(songCursor.getString(songPosition));
                //allSongs.add(singlesong);
                //allAlbums.add(album_art_bitmap);

                //SongsDataProvider songsDataProvider = new SongsDataProvider(currentTitle,songDetailString,singlesong,album_art_bitmap);
                //songsDataProviderArrayList.add(songsDataProvider);

            }while (songCursor.moveToNext());
        }


    }


    @Override
    public void onSongItemClick(int itemPostition) {



        onSongPressedListener.onSongPressed(itemPostition,
                allSongTitle,
                allArtists,
                songDetails,
                allSongPaths,
                allSongAlbum_ID,
                songCursor);

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

        void onSongPressed(int songPosition,
                           ArrayList<String> allSongTitle,
                           ArrayList<String> allArtists,
                           ArrayList<String> songDetails,
                           ArrayList<String> allSongPaths,
                           ArrayList<Long> allSongAlbum_ID,
                           Cursor songCursor);


    }

}



    /*SharedPreferences answer_store=getSharedPreferences("answer_store", Context.MODE_PRIVATE);
    final SharedPreferences.Editor answer_store_editor=answer_store.edit();
    answer_store_editor.putString("Answer",result.getText().toString());
    answer_store_editor.commit();



     SharedPreferences answer_store=getSharedPreferences("answer_store", Context.MODE_PRIVATE);
     Answers=answer_store.getString("Answer","0");*/