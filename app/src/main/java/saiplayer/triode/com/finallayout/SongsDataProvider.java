package saiplayer.triode.com.finallayout;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by dell on 25-Jul-17.
 */

public class SongsDataProvider {

    private  String songTitle;
    private String songDetails;
    private File allSongFiles;
    private Bitmap allAlbumarts;

    public SongsDataProvider(String songTitle,String songDetails, File allSongFiles, Bitmap allAlbumarts) {
        this.songTitle = songTitle;
        this.songDetails = songDetails;
        this.allSongFiles = allSongFiles;
        this.allAlbumarts = allAlbumarts;
    }

    public String getSongDetails() {
        return songDetails;
    }

    public void setSongDetails(String songDetails) {
        this.songDetails = songDetails;
    }

    public File getAllSongFiles() {
        return allSongFiles;
    }

    public void setAllSongFiles(File allSongFiles) {
        this.allSongFiles = allSongFiles;
    }

    public Bitmap getAllAlbumarts() {
        return allAlbumarts;
    }

    public void setAllAlbumarts(Bitmap allAlbumarts) {
        this.allAlbumarts = allAlbumarts;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
