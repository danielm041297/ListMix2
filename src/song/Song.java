package song;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import gui.Player;
public class Song{
	
	public static Comparator<Song> NameComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getName().compareToIgnoreCase(s2.getName());
        }
    };
    public static Comparator<Song> PopularityComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
        	//TO DO: fix this compare method.
            return s2.getPopularity()-s1.getPopularity();
        }
    };
    public static Comparator<Song> ArtistComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }
    };
	private String uri;
	private String name;
	private int timesPlayed;
	private String artist;
    private MediaPlayer mediaplayer;
   
    public Song(String filePath, String name, String artist,Player player){
    	this.name =name;
    	this.artist = artist;
    	timesPlayed=0;
        uri =new File(filePath).toURI().toString();
		Media media = new Media(uri);
        mediaplayer = new MediaPlayer(media);
        mediaplayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				player.next();
				
			}
		});
        if(uri!=null&&!uri.isEmpty() && uri.endsWith(".mp3")){
            //add to database
        }
        else{
            uri = null;
        }
    }
    public void play(double volume){
        if(uri != null){
        	timesPlayed++; // replace with database
        	mediaplayer.setVolume(volume);
            mediaplayer.play();
        }
    }
    public void pause(){
        if(uri != null){
            if(mediaplayer.getStatus() == MediaPlayer.Status.PAUSED){
            	mediaplayer.play();
            }
            else{
            	mediaplayer.pause();
            }
        }
    }
    public void stop(){
        if(uri != null){
        	mediaplayer.stop();
        }
    }
    public void setVolume(double volume) {
		mediaplayer.setVolume(volume);
	}
    //TO DO: ADD MUTE, and LOOP buttons
    public void mute(){
        if(uri != null){
           if(mediaplayer.isMute()){
        	   mediaplayer.setMute(false);
           }else{
        	   mediaplayer.setMute(true);
           }
        }
    }
    public boolean isLoaded(){
        if(uri == null){
            return false;
        }
        else{
            return true;
        }
    }
    public String getName() {
		return name;
	}
	public int getPopularity() {
		return timesPlayed;
	}
	public String getArtist() {
		return artist;
	}
	public boolean isPlaying(){
		if(mediaplayer.getStatus()==MediaPlayer.Status.PLAYING){
			return true;
		}
		return false;
	}
	public boolean equals(Object o){
		Song s = (Song)o;
		return this.name.equals(s.getName()) &&	this.artist.equals(s.getArtist());
	}
	
 
}