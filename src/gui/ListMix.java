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
		soundLevels = new JSlider(SwingConstants.VERTICAL,0,100,100);
    	soundLevels.setMajorTickSpacing(10);
    	soundLevels.setMinorTickSpacing(5);
    	soundLevels.setPaintTicks(true);
    	Dimension d=soundLevels.getPreferredSize();
    	soundLevels.setPreferredSize(new Dimension(d.width+50, d.height+160));
    	Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    	for(int i=0;i<11;i++){
    		String percent = ""+i*10+"%";
    		labels.put(i*10, new JLabel(percent));
    	}
    	soundLevels.setLabelTable(labels);
    	soundLevels.setPaintLabels(true);
    	soundLevels.addChangeListener(this);
    	JPanel p = new JPanel();
    	p.setLayout(new BorderLayout());
    	player = new Player();
    	lib = new Library();
    	player.setLibrary(lib);
    	lib.setPlayer(player);
    	p.add(player, BorderLayout.SOUTH);
    	p.add(soundLevels,BorderLayout.NORTH);
    	
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
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==soundLevels)
		{
			int percent = soundLevels.getValue();
			player.changeSoundLevel(percent);
		}
	}
}
