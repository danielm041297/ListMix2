package gui;
/* Daniel Moore
 * March 25 2015
 * ListMix.java
 * The main frame
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.embed.swing.JFXPanel;

import java.util.*;
import java.util.concurrent.CountDownLatch;
/**
 * ListMix
 * The main frame for this gui application
 */
public class ListMix extends JFrame implements ChangeListener{
    private static Library lib;
    private JSplitPane splitpane;
    private static Player player;
    private JSlider soundLevels;
    /**
     *Sets up the gui application 
     */
	public ListMix(){
		final CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        new JFXPanel(); 
		        latch.countDown();
		    }
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
            // select Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        new JFrame("ListMix");
        setTitle("ListMix");
    	setLayout(new BorderLayout());
    	setSize(new Dimension(500,600));
		JPanel p = new JPanel();
    	p.setLayout(new BorderLayout());
    	player = new Player();
    	lib = new Library();
    	player.setLibrary(lib);
    	lib.setPlayer(player);
    	
    	File myMusicFolder = new File("/home/daniel/Documents/MySongs");
    	if(myMusicFolder.exists()){
    		try {
				lib.addAllMp3s(myMusicFolder);
		    	lib.updateLibrary(null);
			} catch (FileNotFoundException e) {
				System.out.println("File not found!");
				e.printStackTrace();
			}
    	}

    	SoundSlider slider = new SoundSlider(player);
    	p.add(player, BorderLayout.SOUTH);
    	p.add(slider,BorderLayout.NORTH);
    	splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,lib, p);
    	splitpane.setEnabled(false);
		splitpane.setResizeWeight(0.5);
		splitpane.setDividerSize(5);
		splitpane.setDividerLocation(300);
		splitpane.setContinuousLayout(true);
		
		add(splitpane, BorderLayout.CENTER); 	
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);
    }
	public static void main(String[] args){
		new ListMix();
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		
	}
}
