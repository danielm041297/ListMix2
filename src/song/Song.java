package song;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
public class Song implements Runnable{
	
	public static Comparator<Song> NameComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getName().compareTo(s2.getName());
        }
    };
    public static Comparator<Song> PopularityComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getPopularity()-s2.getPopularity();
        }
    };
    public static Comparator<Song> ArtistComparator = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            return s1.getArtist().compareTo(s2.getArtist());
        }
    };
	private File file;
	private String name;
	private int timesPlayed;
	private String artist;
    private boolean running, mute, pause, loop, restart;
    private final int bytesize = 1024;
    private byte[] muted;
    public Song(String filePath, String name, String artist){
    	this.name =name;
    	this.name = artist;
    	timesPlayed=0;
        file = null;
        running = false;
        mute = false;
        pause = false;
        loop = false;
        restart = false;
        muted = setMuteData();
        file = new File(filePath);
        if(file.exists() && file.getName().endsWith(".mp3")){
            //add to database
        }
        else{
            file = null;
        }
    }
    public void play(){
        if(file != null && !running){
        	timesPlayed++;
            running = true;
            try{
                Thread t = new Thread(this);
                t.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void run() {
        try{
            do{
                restart = false;
                AudioInputStream in = AudioSystem.getAudioInputStream(file);
                AudioInputStream din = null;
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                            baseFormat.getSampleRate(),
                                                            16,
                                                            baseFormat.getChannels(),
                                                            baseFormat.getChannels() * 2,
                                                            baseFormat.getSampleRate(),
                                                            false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                stream(decodedFormat, din);
                in.close();
            }while((loop || restart) && running);
            running = false;
        }catch(Exception e){
            System.err.println("Problem getting audio stream!");
            e.printStackTrace();
        }
    }
    private void stream(AudioFormat targetFormat, AudioInputStream din){
        try{
            byte[] data = new byte[bytesize];
            SourceDataLine line = getLine(targetFormat);
            if(line != null){
                line.start();
                int bytespos = 0;
                while(bytespos != -1 && running && !restart){
                	bytespos = din.read(data, 0, data.length);
                    if(bytespos != -1){
                        if(mute){
                            line.write(muted, 0, bytespos);
                        }
                        else{
                            line.write(data, 0, bytespos);
                        }
                    }
                    
                    while(pause && running){
                        wait(15);
                    }
                }
                line.drain();
                line.stop();
                line.close();
                din.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private SourceDataLine getLine(AudioFormat audioFormat){
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        
        try{
            res = (SourceDataLine) AudioSystem.getLine(info);
            res.open(audioFormat);
        }catch(Exception e){
            e.printStackTrace();
        }

        return res;
    }
    private void wait(int time){
        try{
            Thread.sleep(time);
        }catch(Exception e){
            System.err.println("Could not wait!");
            e.printStackTrace();
        }
    }
    private byte[] setMuteData(){
        byte[] x = new byte[bytesize];
        Arrays.fill(x, (byte)0);

        return x;
    }
    public void pause(){
        if(file != null){
            pause = pause?false:true;
        }
    }
    public void stop(){
        if(file != null){
            running = false;
        }
    }
    public void mute(){
        if(file != null){
            mute = mute?false:true;
        }
    }
    public void loop(){
        if(file != null){
            loop = loop?false:true;
        }
    }
    public void restart(){
        restart = true;
    }
    public boolean isLooping(){
        return loop;
    }
    public boolean isMuted(){
        return mute;
    }
    public boolean isPaused(){
        return pause;
    }
    public boolean isPlaying(){
        return running;
    }
    public boolean isLoaded(){
        if(file == null){
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
	public boolean equals(Object o){
		Song s = (Song)o;
		return this.name.equals(s.getName()) &&	this.artist.equals(s.getArtist())&&this.timesPlayed==s.getPopularity();
	}
 
}