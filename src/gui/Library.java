package gui;
/* Daniel Moore
 * March 25 2015
 * libComponent.java
 * contains a music library, and ways to organize it
 */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
	private String filterString;
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
		//load songs from database
		filterString = "";
		songs = new ArrayList<Song>();
		flag=false;
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3", "mp3");
        fc.setFileFilter(filter);
        //REWORK GUI toolbar...
		p = new JPanel();
		p.setLayout(new BorderLayout());
		l = new JLabel();
		l.setLayout(new GridLayout(1,3));
		
		addSong= new JButton("Add");
		addSong.addActionListener(this);
		
		search = new JTextField();
		search.setFont(new Font("reg",Font.ITALIC,16));
		search.setPreferredSize(new Dimension(addSong.getPreferredSize().width+80,addSong.getPreferredSize().height));
		search.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				filterString = search.getText().trim();
				updateLibrary(filterString);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				filterString = search.getText().trim();
				updateLibrary(filterString);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				filterString = search.getText().trim();
				updateLibrary(filterString);
			}
		});
		
		sort = new JComboBox<String>();
		sort.setSize(new Dimension(50, p.getHeight()));
		sort.addItem("Unsorted");
		sort.addItem("A-Z");
		sort.addItem("Popularity");
		sort.addItem("Artist");
		sort.addActionListener(this);
		
		l.add(sort);
		l.add(addSong);		
//		l.add(search);
		
		String[] strs ={"Song Name","Artist",""};
		table = new JTable(new DefaultTableModel(strs,0));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    table.setRowHeight(20);
		table.getColumnModel().getColumn(0).setMinWidth(179);	
		table.getColumnModel().getColumn(1).setMinWidth(85);
		table.getColumnModel().getColumn(2).setMaxWidth(20);
		table.getColumn("").setCellRenderer(new ButtonRenderer());
	    table.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
	    
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(20, 20));
		scrollPane.setAlignmentX(RIGHT_ALIGNMENT);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
	public void updateLibrary(String filter){
		String[] strs ={"Song Name","Artist",""};
		DefaultTableModel model = new DefaultTableModel(strs,0);
		for(int i=0;i<songs.size();i++){
			Song tmp = songs.get(i);
			if(filter==null||filter.equals("")){
				Object[] objs = {tmp.getName(), tmp.getArtist(), ""};
				model.addRow(objs);
			}
			else{
				String artist = tmp.getArtist().toLowerCase();
				String song = tmp.getName().toLowerCase();
				if(artist.contains(filter.toLowerCase())||song.contains(filter.toLowerCase())){
					Object[] objs = {tmp.getName(), tmp.getArtist(), ""};
					model.addRow(objs);
				}
			}
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
	        	if(fi.getPath().endsWith(".mp3"))
	        		addAllMp3s(fi);
	        }
	    }
	    else {
	        String path = f.getPath();
	        if(path.indexOf("/")==-1){ // Windows path
		        String filename2 = path.substring(path.lastIndexOf("\\")+1, path.lastIndexOf(""));
		        String[] song_data=filename2.split(" - ");
		        String artist= song_data[0];
		        String song= song_data[1];
		        Song s = new Song(path,song,artist,player);
		        songs.add(s);
		        
		        //add to database
	        }
	        else{ //Linux path
		        String filename1 = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
		        String[] song_data = filename1.split(" - ");
		        if(song_data.length==2){
			        String artist= song_data[0];
			        String song= song_data[1];
			        Song s = new Song(path,song,artist,player);
			        songs.add(s);
		        }
		        else{
		        	JOptionPane.showMessageDialog(this, "Some songs failed to import! Artist - Song_Name.mp3 is the format.", "Warning",
		        	        JOptionPane.WARNING_MESSAGE);
		        }
		        //add to database
	        }
	        
	    }
	}
	public void actionPerformed(ActionEvent e){
		
			if (e.getSource() == addSong) {
	            int returnVal = fc.showDialog(Library.this,"Add");
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                try{
	        			addAllMp3s(file);
	        			updateLibrary(null);
	        		}catch(FileNotFoundException fnf){System.out.println("Problem loading files");}
	            }
			}
			else{
	           	JComboBox<String> cb=(JComboBox<String>)e.getSource();
	    		String which = (String)cb.getSelectedItem(); 
				switch(which){
					case "Unsorted":
						Collections.shuffle(songs);
						updateLibrary(null);
						break;
					case "A-Z":	
						Collections.sort(songs,Song.NameComparator);
						updateLibrary(null);
						break;
					case "Popularity":
						Collections.sort(songs,Song.PopularityComparator);
						updateLibrary(null);
						break;
					case "Artist":
						Collections.sort(songs,Song.ArtistComparator);
						updateLibrary(null);
						break;
					default:break;
				}
			}
	}
	public void setPlayer(Player player){
		this.player=player;
	}
	class ButtonEditor extends DefaultCellEditor {
		  private JButton button;
		  private String name;
		  public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		    name="";
			button = new JButton();
			try{
				Image img = ImageIO.read(getClass().getResource("/add.png"));
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