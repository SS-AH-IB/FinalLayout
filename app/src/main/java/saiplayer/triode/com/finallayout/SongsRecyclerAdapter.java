package saiplayer.triode.com.finallayout;

import android.content.Context;
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

    ArrayList<SongsDataProvider> songsDataProviders=new ArrayList<SongsDataProvider>();
    Context context;

    int previousPosition=0;

    private OnItemClickType onItemClickType;

    public interface OnItemClickType {

        void onSongItemClick(int itemPostition);

    }

    public void setOnItemClickType(final OnItemClickType onItemClickType){

        this.onItemClickType=onItemClickType;

    }

    public SongsRecyclerAdapter(ArrayList<SongsDataProvider> arrayList,Context context){

        this.context=context;
        this.songsDataProviders=arrayList;

    }

    @Override
    public SongsRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view_songs = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_song_fragment,parent,false);
        //View mainContentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view_songs,songsDataProviders,context);

        return recyclerViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        SongsDataProvider songsDataProvider=songsDataProviders.get(position);
        holder.imageView.setImageBitmap(songsDataProvider.getAllAlbumarts());
        holder.songTitle.setText(songsDataProvider.getSongTitle());
        holder.songDetalsText.setText(songsDataProvider.getSongDetails());

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

        return songsDataProviders.size();
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

        public RecyclerViewHolder(View itemView,ArrayList<SongsDataProvider> songsDataProviders,Context context) {

            super(itemView);
            this.dataProviders=songsDataProviders;
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
