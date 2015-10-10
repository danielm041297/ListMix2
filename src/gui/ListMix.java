package gui;
/* Daniel Moore
 * March 25 2015
 * ListMix.java
 * The main frame
 */
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

import javafx.embed.swing.JFXPanel;

import java.util.*;
import java.util.concurrent.CountDownLatch;
/**
 * ListMix
 * The main frame for this gui application
 */
public class ListMix extends JFrame {
    private static Library lib;
    private JSplitPane splitpane;
    private static Player player;
    /**
     *Sets up the gui application 
     */
	public ListMix(){
		final CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        new JFXPanel(); // initializes JavaFX environment
		        latch.countDown();
		    }
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
    	p.add(player, BorderLayout.SOUTH);
    	
    	
    	splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		                lib, p);
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
}
