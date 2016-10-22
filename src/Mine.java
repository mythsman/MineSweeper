import java.awt.Color;

import javax.swing.JLabel;

public class Mine extends JLabel{
	private static final long serialVersionUID = 1L;
	private int value;
	private boolean isMine;
	private boolean isFlag;
	private boolean isShown;
	private int column,row;
	public static final Color COLORRELEASED=new Color(223, 223, 223);
	public static final Color COLORPRESSED=new Color(20, 150, 190);
	public static final Color COLORFLAG=new Color(170,23,70);
	public static final Color COLORSHOWN=new Color(110,145,123);
	public static final Color COLORMINE=new Color(100, 100, 100);
	public Mine() {
		this.setBackground(Mine.COLORRELEASED);
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean hasFlag(){
		return this.isFlag;
	}
	public void setFlag(){
		this.isFlag=true;
	}
	public void removeFlag(){
		this.isFlag=false;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public boolean isMine() {
		return isMine;
	}
	public void toggleMine() {
		this.isMine = !isMine;
	}
	public boolean isShown() {
		return isShown;
	}
	public void toggleShown() {
		this.isShown = !isShown;
	}


}
