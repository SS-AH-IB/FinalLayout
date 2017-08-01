package saiplayer.triode.com.finallayout;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

/**
 * Created by dell on 25-Jul-17.
 */

public class SongsRecyclerAdapter extends RecyclerView.Adapter <SongsRecyclerAdapter.RecyclerViewHolder>{

    ArrayList<String> allSongTitle = new ArrayList<String>();
    ArrayList<String> songDetails = new ArrayList<String>();
    ArrayList<Long> allSongAlbum_ID = new ArrayList<Long>();
    Cursor songCursor;
    Context context;
    Uri artUri;

    final Uri ART_CONTENT_URI=Uri.parse("content://media/external/audio/albumart");

    int previousPosition=0;


    private OnItemClickType onItemClickType;

    public interface OnItemClickType {

        void onSongItemClick(int itemPostition);

    }

    public void setOnItemClickType(final OnItemClickType onItemClickType){

        this.onItemClickType=onItemClickType;

    }



    public SongsRecyclerAdapter(ArrayList<String> allSongTitle, ArrayList<String> songDetails, ArrayList<Long> allSongAlbum_ID, Context context){

        this.allSongTitle = allSongTitle;
        this.songDetails = songDetails;
        this.allSongAlbum_ID = allSongAlbum_ID;
        this.context=context;

    }

    @Override
    public SongsRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view_songs = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_song_fragment,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view_songs,context);

        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        artUri = ContentUris.withAppendedId(ART_CONTENT_URI,allSongAlbum_ID.get(position));
        Bitmap album_art_bitmap_temp = null;
        Bitmap album_art_bitmap = null;
        try{

            album_art_bitmap_temp = (MediaStore.Images.Media.getBitmap(context.getContentResolver(),artUri));
            album_art_bitmap=album_art_bitmap_temp.copy(Bitmap.Config.RGB_565,false);

        }
        catch (Exception e){

            e.printStackTrace();
        }

        holder.imageView.setImageBitmap(album_art_bitmap);
        //album_art_bitmap.recycle();

        holder.songTitle.setText(allSongTitle.get(position));
        holder.songDetalsText.setText(songDetails.get(position));

        if (position>previousPosition){

            SongsScrollAnimation.animateSongList(holder,true);
        }
        else {
            SongsScrollAnimation.animateSongList(holder,false);
        }

        previousPosition=position;

    }


    @Override
    public int getItemCount() {

        return allSongTitle.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout item_container_root;
        CircleImageView imageView;
        AutofitTextView songTitle;
        AutofitTextView songDetalsText;
        ArrayList<SongsDataProvider> dataProviders = new ArrayList<SongsDataProvider>();

        Context context;


        //DrawerLayout mainDrawer;
        //LinearLayout mainLinearLayout;
        //View mainContent;

        public RecyclerViewHolder(View itemView,Context context) {

            super(itemView);
            this.context=context;

            item_container_root=(LinearLayout)itemView.findViewById(R.id.item_container_root);
            imageView=(CircleImageView)itemView.findViewById(R.id.album_art_of_songs);
            songTitle=(AutofitTextView)itemView.findViewById(R.id.songs_title_on_songs);
            songDetalsText=(AutofitTextView)itemView.findViewById(R.id.songs_detail_on_songs);

            item_container_root.setOnClickListener(this);

            //this.mainContent=mainContent;
            //mainDrawer=(DrawerLayout)mainContent.findViewById(R.id.main_drawer_container);
            //mainLinearLayout=(LinearLayout) mainContent.findViewById(R.id.main_content);

        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.item_container_root){

                onItemClickType.onSongItemClick(getAdapterPosition());

            }



            /*int position=getAdapterPosition();
            SongsDataProvider provider = this.dataProviders.get(position);
            Toast.makeText(context, provider.getSongDetails() , Toast.LENGTH_SHORT).show();*/

            //mainLinearLayout.setBackground(new BitmapDrawable(context.getResources(),provider.getAllAlbumarts()));

            /*Uri u=Uri.parse(provider.getAllSongFiles().toString());
            MediaPlayer mp= MediaPlayer.create(context,u);
            mp.start();*/

        }
    }

}
