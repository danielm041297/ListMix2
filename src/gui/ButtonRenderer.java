package gui;
/* Daniel Moore
 * March 25 2015
 * ButtonRenderer.java
 * Allows buttons to be made and clicked inside a tablwe
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * A class used to create buttons for the JTable
 * @author daniel
 *
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
  private boolean flag; 
  /**
   * Constructs a buttonrenderer for an add button
   */
  public ButtonRenderer() {
	flag=false;
	setOpaque(true);
  }
  /**
   * Constructs a ButtonRenderer which toggles between the Remove button and add button
   * @param boo whether it is a remove button or add button
   */
  public ButtonRenderer(boolean boo) {
		flag=boo;
		setOpaque(true);
  }
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
	  
	  if (isSelected) {
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	  } 
	  else {
	    setForeground(table.getForeground());
	  	try{
	  		Image img;
	  		if(!flag)
	  			img = ImageIO.read(getClass().getResource("resources/add.jpg"));
	  		else
	  			img = ImageIO.read(getClass().getResource("resources/remove.jpg"));
			setIcon(new ImageIcon(img));
			setBorder(null);
		}catch(IOException e){System.out.println("Problem reading images! Check filepath.");} 
	     
	  }
	  
	  return this;
  }
}