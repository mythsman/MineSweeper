import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

public class MineSweeper extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel, panelLeft;
	private boolean playing;
	private int status;
	private Clock clock;
	private JLabel name, labelCover;
	private JButton button;
	private JMenuBar menubar;
	private JMenu menubarGame, menubarAbout;
	private JMenuItem itemNew, itemAbout;
	private JRadioButtonMenuItem itemSimple, itemMiddle, itemHard;
	private Mine[][] mine;
	private int column, row, btnSize, windowWidth, windowHeight;
	private int menuHeight, margin, mineNum;
	private int shownCnt;

	public MineSweeper() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		initValue();
		initLayout();
		startGame();
	}

	private void initValue() {
		mineNum = 10;
		windowWidth = 720;
		windowHeight = 540;
		column = 10;
		row = 10;
		menuHeight = 30;
		margin = 10;
	}

	private void addListener() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				mine[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						Mine btn = (Mine) e.getSource();
						if (e.getButton() == MouseEvent.BUTTON3) {
							mineRightClick(btn);
						} else if (e.getButton() == MouseEvent.BUTTON1) {
							mineLeftClick(btn);
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {
						Mine btn = (Mine) e.getSource();
						btn.setBackground(Mine.COLORPRESSED);
					}
				});
			}
		}
	}

	private void removeListener() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				mine[i][j].removeMouseListener(mine[i][j].getMouseListeners()[0]);
			}
		}
	}

	private void winGame() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				if(mine[i][j].isMine())
					mine[i][j].setBackground(Mine.COLORMINE);
			}
		}
		clock.stop();
		playing = false;
		removeListener();
		JDialog dlg = new JDialog();
		JLabel lab = new JLabel("You win!");
		dlg.add(lab);
		dlg.setTitle("Great");
		dlg.setSize(300, 100);
		dlg.setModal(true);
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		
	}

	private void endGame() {
		status=0;
		if(playing){
			clock.stop();
			playing = false;
			if(status==1)
				removeListener();
		}
		panel.removeAll();
		panelLeft.removeAll();
		this.remove(panel);
		this.remove(panelLeft);

	}

	private void loseGame() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				if(mine[i][j].isMine())
					mine[i][j].setBackground(Mine.COLORMINE);
			}
		}
		clock.stop();
		playing = false;
		removeListener();
		JDialog dlg = new JDialog();
		JLabel lab = new JLabel("You lose!");
		dlg.add(lab);
		dlg.setTitle("Warning");
		dlg.setSize(300, 100);
		dlg.setModal(true);
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		
	}

	private void mineLeftClick(Mine btn) {
		if (btn.hasFlag()) {
			btn.setBackground(Mine.COLORFLAG);
			return;
		}
		btn.setBackground(Mine.COLORSHOWN);
		if (btn.isShown()) {
			return;
		}
		if (btn.getValue() == 0) {
			btn.setText("");
		} else {
			btn.setText(btn.getValue() + "");
			btn.setFont(new Font("中华行楷", Font.BOLD, 20));
		}
		btn.toggleShown();
		int x = btn.getRow();
		int y = btn.getColumn();
		if (btn.isMine()) {
			this.loseGame();
			return;
		}
		if (btn.getValue() == 0) {
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (isLeagal(i, j) && !(i == x && j == y) && !mine[i][j].isShown()) {
						mineLeftClick(mine[i][j]);
					}
				}
			}
		}
		this.shownCnt += 1;
		if (this.shownCnt + this.mineNum == this.column * this.row) {
			this.winGame();
			return;
		}
	}

	private void mineRightClick(Mine btn) {
		if (btn.isShown()) {
			btn.setBackground(Mine.COLORSHOWN);
		} else if (btn.hasFlag()) {
			btn.removeFlag();
			btn.setBackground(Mine.COLORRELEASED);
		} else {
			btn.setFlag();
			btn.setBackground(Mine.COLORFLAG);
		}
	}

	private void initLayout() {
		addMenu();
		this.setSize(windowWidth, windowHeight);
		this.setLocationRelativeTo(null);
		this.setTitle("MineSweeper");
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setIconImage(new ImageIcon("icon/title.png").getImage());
	}

	private void startGame() {
		btnSize = ((int) (this.getHeight() - this.menubar.getPreferredSize().getHeight() - 2 * margin - menuHeight))
				/ row;

		panel = new JPanel();
		panelLeft = new JPanel();
		panelLeft.setPreferredSize(new Dimension((int) (this.getWidth() * 0.3), 0));
		
		this.add(panel, BorderLayout.CENTER);
		this.add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(new GridLayout(8, 1));
		panel.repaint();
		panelLeft.repaint();
		name = new JLabel("MineSweeper");
		name.setFont(new Font("楷书", 0, 25));
		name.setHorizontalAlignment(SwingConstants.CENTER);
		clock = new Clock();
		clock.start();
		clock.stop();

		button = new JButton("Start");
		button.setBackground(new Color(238, 238, 238));
		button.setFont(new Font("楷书", 1, 30));
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JButton cur = (JButton) arg0.getSource();
				if (status == 0) {// Stopped
					playing = true;
					try {
						panel.remove(labelCover);
						panel.repaint();
					} catch (Exception e) {
						// Omit
					}
					cur.setText("Pause");
					addMine();
					addListener();
					clock.start();
					status = 1;
				} else if (status == 1) {// Running
					if (!playing) {
						return;
					}
					cur.setText("Start");
					removeListener();
					status = 0;
					clock.stop();
					labelCover = new JLabel("Pause");
					labelCover.setFont(new Font("楷书", 1, 150));
					labelCover.setHorizontalAlignment(SwingConstants.CENTER);
					labelCover.setOpaque(true);
					labelCover.setBackground(new Color(200, 200, 200));
					panel.add(labelCover);
					labelCover.setBounds(margin, margin, column * btnSize, row * btnSize);
				}
			}
		});

		panelLeft.add(name);
		panelLeft.add(new JLabel());
		panelLeft.add(clock);
		panelLeft.add(new JLabel());
		panelLeft.add(button);
		panelLeft.add(new JLabel());
		panel.setLayout(null);
		mine = new Mine[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				mine[i][j] = new Mine();
				mine[i][j].setRow(i);
				mine[i][j].setOpaque(true);
				mine[i][j].setColumn(j);
				mine[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				mine[i][j].setBounds(i * btnSize + margin, j * btnSize + margin, btnSize, btnSize);
				panel.add(mine[i][j]);
				mine[i][j].setBorder(BorderFactory.createRaisedBevelBorder());
			}
		}
	}

	private void addMenu() {
		menubar = new JMenuBar();
		menubarGame = new JMenu("Game");
		menubarAbout = new JMenu("About");
		itemAbout=new JMenuItem("about");
		menubarAbout.add(itemAbout);
		menubar.add(menubarGame);
		menubar.add(menubarAbout);
		
		menubar.setPreferredSize(new Dimension(0, menuHeight));
		itemNew = new JMenuItem("New");
		menubarGame.add(itemNew);
		ButtonGroup group = new ButtonGroup();
		itemSimple = new JRadioButtonMenuItem("Simple");
		menubarGame.add(itemSimple);
		itemMiddle = new JRadioButtonMenuItem("Middle");
		menubarGame.add(itemMiddle);
		itemHard = new JRadioButtonMenuItem("Hard");
		menubarGame.add(itemHard);
		group.add(itemSimple);
		group.add(itemMiddle);
		group.add(itemHard);
		itemSimple.setSelected(true);
		itemAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog dlg = new JDialog();
				JLabel lab = new JLabel("Minesweeper Authorized by myths");
				dlg.add(lab);
				dlg.setTitle("About");
				dlg.setSize(300, 100);
				dlg.setModal(true);
				dlg.setLocationRelativeTo(null);
				dlg.setVisible(true);
			}
		});
		itemSimple.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				column = 10;
				row = 10;
				mineNum = 10; 
				itemNew.doClick();
			}
		});
		itemMiddle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				column = 20;
				row = 20;
				mineNum = 40;
				itemNew.doClick();
			}
		});
		itemHard.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				column = 30;
				row = 30;
				mineNum = 90;
				itemNew.doClick();
			}
		});
		itemNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				endGame();
				startGame();
			}
		});
		this.setJMenuBar(menubar);
	}

	private void addMine() {
		Random rand = new Random();
		int cnt = 0;
		while (cnt < mineNum) {
			int x = rand.nextInt(row);
			int y = rand.nextInt(column);
			if (mine[x][y].isMine()) {
				continue;
			} else {
				mine[x][y].toggleMine();
				for (int i = x - 1; i <= x + 1; i++) {
					for (int j = y - 1; j <= y + 1; j++) {
						if (isLeagal(i, j)) {
							mine[i][j].setValue(mine[i][j].getValue() + 1);
						}
					}
				}
				cnt++;
			}
		}
		clock.start();
	}

	private boolean isLeagal(int x, int y) {
		if (x >= 0 && x < row && y >= 0 && y < column) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		MineSweeper mc = new MineSweeper();
		mc.setVisible(true);
	}
}
