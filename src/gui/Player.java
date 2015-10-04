package gui;
/* Daniel Moore
 * March 25 2015
 * playComponent.java
 * the player Component which plays music and has controls for it.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import song.Song;

import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The player component of this gui project
 * @author daniel
 * has controls, and methods to run
 */
public class Player extends JComponent implements ActionListener{
	private Song removed;
	private JPanel p;
	private JTable table;
	private Song previous;
	private JButton next,prev,pause,play,shuffle;
	private Library library;
	private static ArrayList<Song> queue;
		/**
		 * Contructs a blank player
		 */
	public Player(){
		queue = new ArrayList<Song>();
		p= new JPanel(new GridLayout(1,5));
		next = new JButton();
		prev= new JButton();
		pause= new JButton();
		play= new JButton();
		shuffle= new JButton();
		try{
		Image img = ImageIO.read(getClass().getResource("/PrevTrack-32-1.png"));
		Image img1 = ImageIO.read(getClass().getResource("/NextTrack-32.png"));
		Image img2 = ImageIO.read(getClass().getResource("/shuffle1.png"));
		Image img3 = ImageIO.read(getClass().getResource("/Pause-64.png"));
		Image img4 = ImageIO.read(getClass().getResource("/Play-64.png"));
		prev.setIcon(new ImageIcon(img));
		next.setIcon(new ImageIcon(img1));
		shuffle.setIcon(new ImageIcon(img2));
		pause.setIcon(new ImageIcon(img3));
		play.setIcon(new ImageIcon(img4));
		}catch(IOException e){
			System.out.println("Problem reading images");
		}
		prev.setBackground(Color.GRAY);
		next.setBackground(Color.GRAY);
		shuffle.setBackground(Color.GRAY);
		pause.setBackground(Color.GRAY);
		play.setBackground(Color.GRAY);
		prev.setBorder(null);
		next.setBorder(null);
		shuffle.setBorder(null);
		pause.setBorder(null);
		play.setBorder(null);
		prev.addActionListener(this);
		next.addActionListener(this);
		shuffle.addActionListener(this);
		pause.addActionListener(this);
		play.addActionListener(this);
		
		p.add(prev);
		p.add(next);
		p.add(shuffle);
		p.add(pause);
	    p.add(play);
	    
	    Object[][] objs = new Object[queue.size()][2];
		String[] strs ={"Song Name",""};
		table = new JTable(new DefaultTableModel(strs,0));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    table.setTableHeader(null);
	    table.setRowHeight(20);
		table.setSelectionBackground(new Color(204,255,153));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);	
		table.getColumnModel().getColumn(1).setPreferredWidth(20);	
		table.setShowVerticalLines(false);
		table.getColumn("").setCellRenderer(new ButtonRenderer(true));
	    table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(20, 20));
		scrollPane.setAlignmentX(RIGHT_ALIGNMENT);
		scrollPane.getVerticalScrollBar().setBackground(Color.lightGray);
		scrollPane.getViewport().setBackground(new Color(32,32,32));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(130,200));
		add(scrollPane, BorderLayout.CENTER);
		add(p, BorderLayout.NORTH);
	}
	/**
	 * Updates the player
	 */
	public void updateTable(){
		String[] strs ={"Song Name",""};
		DefaultTableModel model = new DefaultTableModel(strs,0);
			
		for(int i=0;i<queue.size();i++){
			Song tmp = queue.get(i);
			Object[] objs = {tmp.getName(), ""};
			model.addRow(objs);
		}
		table.isCellEditable(0, 0);
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);	
		table.getColumnModel().getColumn(1).setPreferredWidth(20);
		table.getColumn("").setCellRenderer(new ButtonRenderer(true));
	    table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
	    table.revalidate(); 	
		table.setModel(model);
	}
	/**
	 * Gets a song from the player
	 * @param name the name desired
	 * @return the song with the given name
	 */
	public Song getSong(String name){
		Song ret=null;
		for(int i=0;i<queue.size();i++){
			if(name.equals(queue.get(i).getName())){
				ret= queue.get(i);
			}
		}
		
		return ret;
	}
	/**
	 * Removes a song from the player
	 * @param name the name desired
	 * @return the song with the given name
	 */
	public Song removeSong(String name){
		Song ret=null;
		for(int i=0;i<queue.size();i++){
			if(name.equals(queue.get(i).getName())){
				ret= queue.get(i);
				queue.remove(i);
			}
		}
		updateTable();
		return ret;
	}
	public void add(Song s){
		queue.add(s);
		updateTable();
	}
	public void shuffle(){
		Collections.shuffle(queue);
		updateTable();
	}
	public void previous() throws IOException{
		queue.get(0).stop();
		if(previous!=null){
			queue.add(0,previous);
			play();
			updateTable();
		}
		
	}
	public void next() throws IOException{
		queue.get(0).stop();
		if(queue.size()>1){
			queue.remove(0);
			play();
			updateTable();
		}
	}
	/**
	 * Pauses the current song
	 */
	public void pause(){
		if(queue.size()>0)
			queue.get(0).stop();
	}
	/**
	 * Plays the song at the beginning of the player
	 */
	public void play(){
		if(!queue.isEmpty())
			queue.get(0).play();
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==next){
			try {
				next();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource()==prev){
			try {
				previous();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		if(e.getSource()==pause){
			pause();
		}
		if(e.getSource()==shuffle){
			shuffle();
		}
		if(e.getSource()==play){
			play();
		}
	}
	public void setLibrary(Library library){
		this.library=library;
	}
	class ButtonEditor extends DefaultCellEditor {
		  private JButton button;
		  private String name;
		  public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		    name="";
			button = new JButton();
			try{
				Image img = ImageIO.read(getClass().getResource("/remove.jpg"));
				button.setIcon(new ImageIcon(img));
				button.setBorder(null);
			}catch(IOException e){System.out.println("Problem reading images");} 

		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	 		    	  
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
			  	DefaultTableModel d =(DefaultTableModel)table.getModel();
			  	name = (String)d.getValueAt(row,0);
			  	return button;
		  }
		}

}
