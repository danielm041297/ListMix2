package gui;
/* Daniel Moore
 * March 25 2015
 * libComponent.java
 * contains a music library, and ways to organize it
 */
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import song.Song;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
/**
 * libComponent
 * A class which contains the music library
 * @author daniel
 *
 */
public class Library extends JComponent implements ActionListener{
	private JTextField search;
	private JComboBox<String> sort;
	private JPanel p;
	private JLabel l;
	private JTable table;
	private Player player;
    private JFileChooser fc;
    private JButton addSong;
    private JScrollPane scrollPane;
    private boolean flag;
    private ArrayList<Song> songs;
    /**
     * Constructs and sets up the music library
     */
	public Library(){
		flag=false;
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3", "mp3");
        fc.setFileFilter(filter);

		p = new JPanel();
		p.setLayout(new BorderLayout());
		l = new JLabel();
		l.setLayout(new GridLayout(1,2));
		
		addSong= new JButton("Add");
		addSong.addActionListener(this);
		
		search = new JTextField("Search: ");
		search.setFont(new Font("reg",Font.ITALIC,16));
		
		sort = new JComboBox<String>();
		sort.setSize(new Dimension(50, p.getHeight()));
		sort.addItem("A-Z");
		sort.addItem("Popularity");
		sort.addItem("Artist");
		sort.addActionListener(this);
		
		l.add(new Label("Sort By: "));
		l.add(sort);
		l.add(addSong);		
		
		
		JButton addButton = new JButton();
		try{
			Image img = ImageIO.read(getClass().getResource("/add.jpg"));
			addButton.setIcon(new ImageIcon(img));
			addButton.setBorder(null);
		}catch(IOException e){System.out.println("Problem reading images");} 
		String[] strs ={"Song Name","Artist",""};
		table = new JTable(new DefaultTableModel(strs,0));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    table.setRowHeight(20);
		table.setSelectionBackground(new Color(204,255,153));
		table.getColumnModel().getColumn(0).setMinWidth(179);	
		table.getColumnModel().getColumn(1).setMinWidth(85);
		table.getColumnModel().getColumn(2).setMaxWidth(20);
		table.setShowVerticalLines(false);
		table.getColumn("").setCellRenderer(new ButtonRenderer());
	    table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
	    
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(20, 20));
		scrollPane.setAlignmentX(RIGHT_ALIGNMENT);
		scrollPane.getVerticalScrollBar().setBackground(Color.lightGray);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getViewport().setBackground(new Color(32,32,32));
		setLayout(new BorderLayout());

		p.add(l);
		p.add(search, BorderLayout.EAST);
		setPreferredSize(new Dimension(300,400));
		add(scrollPane, BorderLayout.CENTER);
		
		add(p, BorderLayout.NORTH);
	}
	/**
	 * Updates the music library after files have been added
	 */
	public void updateLibrary(){
		String[] strs ={"Song Name","Artist",""};
		DefaultTableModel model = new DefaultTableModel(strs,0);
		for(int i=0;i<songs.size();i++){
			Song tmp = songs.get(i);
			Object[] objs = {tmp.getName(), tmp.getArtist(), ""};
			model.addRow(objs);
		}
		table.setModel(model);
		table.getColumnModel().getColumn(0).setMinWidth(179);	
		table.getColumnModel().getColumn(1).setMinWidth(85);
		table.getColumnModel().getColumn(2).setMaxWidth(20);
		table.getColumn("").setCellRenderer(new ButtonRenderer());
	    table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
	    table.revalidate(); 	
	}
	public void addAllMp3s(File f) throws FileNotFoundException{
	    File[] files;
	    if (f.isDirectory() && (files = f.listFiles()) != null) {
	        for (File fi : files) {
	            addAllMp3s(fi);
	        }
	    }
	    else {
	        String path = f.getPath();
	        Song s = new Song(path,"John","Cena");
	        songs.add(s);
	    }
	}
	public void actionPerformed(ActionEvent e){
		
			if (e.getSource() == addSong) {
	            int returnVal = fc.showDialog(Library.this,"Add");
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                try{
	        			addAllMp3s(file);
	        			updateLibrary();
	        		}catch(FileNotFoundException fnf){System.out.println("Problem loading files");}
	            }
			}
			else{
	           	JComboBox<String> cb=(JComboBox<String>)e.getSource();
	    		String which = (String)cb.getSelectedItem(); 
				switch(which){
					case "A-Z":	
						Collections.sort(songs,Song.NameComparator);
						updateLibrary();
						break;
					case "Popularity":
						Collections.sort(songs,Song.PopularityComparator);
						updateLibrary();
						break;
					case "Artist":
						Collections.sort(songs,Song.ArtistComparator);
						updateLibrary();
						break;
					default:break;
				}
			}
	}
	public void setPlayer(Player player){
		this.player=player;
	}
	/**
	 * A class used to add a song to the player component
	 * @author daniel
	 */
	class ButtonEditor extends DefaultCellEditor {
		  private JButton button;
		  private String name;
		  public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		    name="";
			button = new JButton();
			try{
				Image img = ImageIO.read(getClass().getResource("/add.jpg"));
				button.setIcon(new ImageIcon(img));
				button.setBorder(null);
			}catch(IOException e){System.out.println("Problem reading images");} 

		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  for(int i=0;i<songs.size();i++){
		    		  if(songs.get(i).getName().equals(name))
		    			  player.add(songs.get(i)); 
		    	  }
		    	  
		    	  
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