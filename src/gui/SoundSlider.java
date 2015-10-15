package gui;

import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SoundSlider extends JSlider implements ChangeListener{
	private Player player;
	private int percent;
	public SoundSlider(Player player){
		super(SwingConstants.VERTICAL,0,100,100);
		this.player = player;
		setMajorTickSpacing(10);
    	setMinorTickSpacing(5);
    	setPaintTicks(true);
    	Dimension d=getPreferredSize();
    	setPreferredSize(new Dimension(d.width+50, d.height+160));
    	Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    	for(int i=0;i<11;i++){
    		String percent = ""+i*10+"%";
    		labels.put(i*10, new JLabel(percent));
    	}
    	setLabelTable(labels);
    	setPaintLabels(true);
    	addChangeListener(this);
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		int percent = this.getValue();
		player.changeSoundLevel(percent);
	}
	
}
