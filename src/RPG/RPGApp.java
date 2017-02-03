package RPG;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.*;

public class RPGApp extends JFrame {
	public ArrayList<Unit> p1Units = new ArrayList<Unit>();
	public ArrayList<Unit> p2Units = new ArrayList<Unit>();
	public ArrayList<Unit> roster = new ArrayList<Unit>(); // this will actually
															// be made in
															// overworld menus
															// class

	public int activePlayer = 0;

	public ArrayList<Unit>[] players;

	public static void main(String[] args) throws Exception {
		// the actual roster will probably be maintained in the class that
		// handles overworld menus, this is just combat scenarios
		RPGApp start = new RPGApp("Test");
	}

	Unit senorSavesTheDay = new Unit("SenorSavesTheDay", 0, "swordIron Sword");
	Unit pizzaJew = new Unit("PizzaJew", 1, "Fists");
	Unit elLady = new Unit("ElLady", 0, "bowCrappy Bow");
	Unit christmasNinja = new Unit("ChristmasNinja", 0, "throwThrowing Knives");
	SpringLayout layout;

	public void myAttackWillRainDownFromTheSky() {
		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				array[n][j].colorTile.setAttacking();
			}
		}
	}

	public RPGApp() {

		UIManager.put("Label.font", new Font("Garamond", Font.BOLD, 14));

		setTitle("Grid");
		setSize(1000, 750);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(new SpawnListener());

		buildGrid();
		add(grid);
		characterDetailPanel = new JPanel();
		characterDetailPanel.setLayout(new BorderLayout());

		setVisible(true);

		refreshGrid();
		add(characterDetailPanel);

		scrollTimer = new Timer(250, Scroll); // start the Scroll timer, every
												// .25 seconds will check to see
												// if the screen has to move
		scrollTimer.start();

	}

	String levelName;
	public RPGApp(String levelName) throws Exception {
		UIManager.put("Label.font", new Font("Garamond", Font.BOLD, 14));

		setTitle(levelName);
		setSize(1000, 750);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		roster.add(senorSavesTheDay);
		roster.add(elLady);
		roster.add(christmasNinja);

		this.levelName = levelName;
		mapImage = new ImageIcon("Art\\Maps\\" + levelName + ".png").getImage();
		buildGrid(levelName);
		
		add(grid);
		characterDetailPanel = new JPanel();
		characterDetailPanel.setLayout(new BorderLayout());
		
		setVisible(true);

		// refreshGrid();
		add(characterDetailPanel);
		
		addKeyListener(new Zoom());
		scrollTimer = new Timer(250, Scroll); // start the Scroll timer, every
												// .25 seconds will check to see
												// if the screen has to move
		scrollTimer.start();
		
		mapImage = resizeImage("Art\\Maps\\" + levelName, array.length * array[0][0].getWidth(), 
				array[0].length*array[0][0].getHeight());
		
		playBackGroundMusic("HonorMedley");
	}

	public void refreshGrid() {
		// setVisible(false);
		remove(grid);
		layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, grid, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, grid, this.getWidth() - grid.getWidth() / 16 - 146, SpringLayout.WEST,
				this);
		layout.putConstraint(SpringLayout.NORTH, grid, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, grid, this.getHeight() - grid.getHeight() / 16, SpringLayout.NORTH,
				this);

		layout.putConstraint(SpringLayout.WEST, characterDetailPanel, 0, SpringLayout.EAST, grid);
		layout.putConstraint(SpringLayout.EAST, characterDetailPanel, this.getWidth(), SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, characterDetailPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, characterDetailPanel, this.getHeight() - grid.getHeight() / 16,
				SpringLayout.NORTH, this);

		setLayout(layout);
		add(grid);

		// setVisible(true);
	}

	public Timer scrollTimer;// timer that runs the Scroll method

	public int startY = 0, startX = 0, // give the top left corner where the
										// visible grid starts to build from
										// array
			currentSize = 20; // how big the camera/visibleGrid will be
	public boolean moveNorth = false, moveSouth = false, moveEast = false, moveWest = false; 
	// tells if the mouse is in a square that should make the camera move

	/**
	 * ActionListener that runs every .25 seconds on the scrollTimer
	 * (initialized in constructor) It checks to see if the screen should
	 * scroll, then refreshes the grid based on the startX and startY variables
	 */
	ActionListener Scroll = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			// System.out.println("scroll" + startX + " "+startY);
			checkMoveCamera();
			buildVisibleGrid();

			add(grid);
			refreshGrid();
			refresh();
			invalidate();
			validate();

		}
	};

	/**
	 * Method that checks to see if the camera should be moved based on
	 * variables north/south/east/west These variables are changed in the main
	 * MouseListener method
	 */
	public void checkMoveCamera() {
		if (moveNorth && startY > 0)
			startY--;
		else if (moveSouth && startY < array.length - currentSize)
			startY++;
		else if (moveEast && startX < array.length - currentSize)
			startX++;
		else if (moveWest && startX > 0)
			startX--;
	}

	JPanel grid;

	Tile[][] array = new Tile[100][100];

	private void buildGrid() {
		System.out.println(array.length);

		grid = new JPanel() {

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(mapImage, array[startY][startX].getWidth() * -startX + 4,
						array[startY][startX].getHeight() * -startY + 3, null);
			}

			public void update(Graphics g) {
				paint(g);
			}
		};

		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				array[n][j] = new Tile(n, j, 0);
				array[n][j].addMouseListener(new MouseListenerTest());
				
				if (j == array[n].length - 1) {
					// array[n][j].addMouseListener(new
					// ScrollRightMouseListener());
				}

				// array[n][j].setBackground(new Color(0,0,0,0));
				// grid.add(array[n][j]);
			}
		}
		buildVisibleGrid();

		repaint();
	}

	private int unfilledSpawns = 0;
	Image mapImage;
	
	private void buildGrid(String levelNamed) throws Exception {
		//System.out.println(array.length);

		grid = new JPanel() {

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.drawImage(mapImage, array[0][0].getWidth() * -startX + 4,
						array[0][0].getHeight() * -startY + 3, null);

			}
			
			public void update(Graphics g) {
				paint(g);
			}

		};

		File initializer = new File("Levels\\" + levelName + ".txt");
		Scanner reader = new Scanner(initializer);

		if (!reader.next().equals(levelName)) {
			System.out.println("There is a problem with the level text file");
		}

		int xLength = reader.nextInt();
		int yLength = reader.nextInt();

		array = new Tile[yLength][xLength];
		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				array[n][j] = new Tile(n, j, reader.nextInt());
				array[n][j].addMouseListener(new PlayerSpawnMouseListener());

			}
		}

		String placingUnit = reader.next();

		while (!placingUnit.equals("Player")) {
			FileInputStream inStream = new FileInputStream("Units\\" + levelName + placingUnit + ".dat");
			ObjectInputStream objectInputFile = new ObjectInputStream(inStream);

			Unit initializedUnit = (Unit) objectInputFile.readObject();
			initializedUnit.findImages();
			p2Units.add(initializedUnit);

			int xSpawn = reader.nextInt();
			int ySpawn = reader.nextInt();

			array[ySpawn][xSpawn].place(initializedUnit);

			objectInputFile.close();
			placingUnit = reader.next();
		}

		while (reader.hasNext()) {
			// read coordinates for set spawning
			int xSpawn = reader.nextInt();
			int ySpawn = reader.nextInt();

			array[ySpawn][xSpawn].colorTile.setSpawning();
			if (unfilledSpawns != roster.size())
				unfilledSpawns++;

			System.out.println("Unfilled Spawns: " + unfilledSpawns);
		}
		
		reader.close();
		buildVisibleGrid();

		repaint();
	}

	
	
	public Image resizeImage(String imageName, int width, int height) throws Exception{
		int scaledWidth = width;
		int scaledHeight = height;
		//
		File inputFile = new File(imageName + ".png");
		BufferedImage inputImage = ImageIO.read(inputFile);
		
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
		
		Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        
        ImageIO.write(outputImage, "png", new File(imageName + "Resize.png"));
        
		return outputImage;
	}
	
	/**
	 * Builds the grid/camera
	 */
	public void buildVisibleGrid() {
		grid.removeAll();

		if (currentSize > array.length) { // this will be relevant if we can get
											// the background and characters to
			currentSize = array.length;// scale, allowing small maps and zooming
		}
		if (currentSize > array[0].length) {
			currentSize = array[0].length;
		}

		grid.setLayout(new GridLayout(currentSize, currentSize));

		for (int i = startY; i < currentSize + startY; i++)
			for (int h = startX; h < currentSize + startX; h++)
				grid.add(array[i][h]);

	}

	JPanel characterDetailPanel;
	JPanel characterDetailAll;
	JPanel characterPortrait;
	JPanel characterStats;
	JPanel characterItems;

	private void buildCharacterDetailPanel(Tile space) {

		characterDetailPanel.removeAll();
		JLabel portraitName = new JLabel(space.occupyingUnit.name);
		characterDetailPanel.add(portraitName, BorderLayout.NORTH);

		characterDetailAll = new JPanel();
		characterDetailAll.setLayout(new BorderLayout());

		characterPortrait = new JPanel();
		JLabel portraitImage = new JLabel(space.occupyingUnit.portrait);
		characterPortrait.add(portraitImage);
		characterPortrait.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		characterStats = new JPanel();
		characterStats.setLayout(new BoxLayout(characterStats, BoxLayout.Y_AXIS));
		JLabel enthusiasmLabel = new JLabel(
				"Enthusiasm: " + space.occupyingUnit.enthusiasm + " / " + space.occupyingUnit.maxEnthusiasm);
		JLabel focusLabel = new JLabel("Focus: " + space.occupyingUnit.focus + " / " + space.occupyingUnit.maxFocus);
		JLabel toughnessLabel = new JLabel("Toughness: " + space.occupyingUnit.toughness);
		JLabel speedLabel = new JLabel("Speed: " + space.occupyingUnit.speed);
		JLabel dilligenceLabel = new JLabel("Dilligence: " + space.occupyingUnit.dilligence);
		characterStats.add(enthusiasmLabel);
		characterStats.add(focusLabel);
		characterStats.add(toughnessLabel);
		characterStats.add(speedLabel);
		characterStats.add(dilligenceLabel);
		characterStats.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		characterItems = new JPanel();
		JLabel equippedWeapon = new JLabel("Equipped: " + space.occupyingUnit.weapon.name);
		characterItems.add(equippedWeapon);
		characterItems.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		characterDetailAll.add(characterPortrait, BorderLayout.NORTH);
		characterDetailAll.add(characterStats, BorderLayout.CENTER);
		characterDetailAll.add(characterItems, BorderLayout.SOUTH);

		characterDetailPanel.add(characterDetailAll, BorderLayout.CENTER);
		characterDetailPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		characterDetailPanel.updateUI();
	}

	public void beginTurn(int playerNumber) {
		if (playerNumber == 1) {
			for (int n = 0; n < p1Units.size(); n++) {
				p1Units.get(n).active = true;
			}
		} else if (playerNumber == 2) {
			for (int n = 0; n < p2Units.size(); n++) {
				p2Units.get(n).active = true;
			}
		}
		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				if (array[n][j].occupied) {
					Unit temp = array[n][j].occupyingUnit;
					array[n][j].remove(temp);
					array[n][j].place(temp);
				}
			}
		}

		JOptionPane.showMessageDialog(null, "Player " + playerNumber + " turn");

		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				if (array[n][j].occupied) {
					Unit temp = array[n][j].occupyingUnit;
					array[n][j].remove(temp);
					array[n][j].place(temp);
				}
			}
		}
	}

	public boolean moving = false;
	public boolean actionMenuOpen = false;

	public void refresh() {
		grid.repaint();

		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				array[n][j].updateUI();
			}
		}
	}

	public class SpawnListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (p1Units.size() < 2) {
				p1Units.add(senorSavesTheDay);
				p1Units.add(elLady);
				p1Units.add(christmasNinja);
				if (p2Units.size() < 1) {
					p2Units.add(pizzaJew);
					p2Units.add(christmasNinja);
				}
			}
			/*
			 * if(!senorSavesTheDay.placed){
			 * array[1][1].place(senorSavesTheDay); beginTurn(1); }
			 * if(!pizzaJew.placed){ array[2][2].place(pizzaJew); }
			 * if(!elLady.placed){ array[4][6].place(elLady); }
			 * if(!christmasNinja.placed){ array[4][0].place(christmasNinja); }
			 */
			refresh();
			System.out.println(arg0.getKeyCode());

			if (arg0.getKeyCode() == 32) {
				if (attackFound) {
					cancelAttack();
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

	}

	Unit movingUnit;
	Tile moveFromTile;
	Tile moveToTile;
	Tile attackFromTile;

	JFrame spawnSelectWindow;
	JPanel spawnSelectPanel;
	Tile spawningTile;

	JFrame actionWindow;
	JPanel actionPanel;
	ArrayList<String> actionList;

	JFrame portraitWindow;
	JLabel portrait;
	JPanel portraitPanel;

	boolean attackFound;
	boolean portraitWindowOpen;

	JFrame attackPreviewWindow;
	JButton confirmAttack;
	JButton stopAttacking;

	Unit attackUnit;
	Unit defendUnit;

	JFrame animationWindow;

	public void setAttack(Tile inputTile, int attackingAllignment, int attackRange) {
		int x = inputTile.xPos;
		int y = inputTile.yPos;
		if (attackRange >= 0) {
			if (inputTile.occupied && inputTile.occupyingUnit.allignment != attackingAllignment) {
				inputTile.colorTile.setAttacking();
				attackFound = true;
				refresh();
			}
		}

		if (attackRange != 0) {
			if (x > 0 && x - movingUnit.occupiedSpace.xPos <= 0) {
				if (attackRange > 0) {
					setAttack(array[x - 1][y], attackingAllignment, attackRange - 1);
				} else {
					setAttack(array[x - 1][y], attackingAllignment, attackRange + 1);
				}
			}
			if (x < array.length - 1 && x - movingUnit.occupiedSpace.xPos >= 0) {
				if (attackRange > 0) {
					setAttack(array[x + 1][y], attackingAllignment, attackRange - 1);
				} else {
					setAttack(array[x + 1][y], attackingAllignment, attackRange + 1);
				}
			}
			// attackRange = 0;
			if (y > 0 && y - movingUnit.occupiedSpace.yPos <= 0) {
				if (attackRange > 0) {
					setAttack(array[x][y - 1], attackingAllignment, attackRange - 1);
				} else {
					setAttack(array[x][y - 1], attackingAllignment, attackRange + 1);
				}
			}
			if (y < array[x].length - 1 && y - movingUnit.occupiedSpace.yPos >= 0) {
				if (attackRange > 0) {
					setAttack(array[x][y + 1], attackingAllignment, attackRange - 1);
				} else {
					setAttack(array[x][y + 1], attackingAllignment, attackRange + 1);
				}
			}
		}
	}

	public void checkCounterAttack(Tile inputTile, int attackingAllignment, int attackRange) {
		int x = inputTile.xPos;
		int y = inputTile.yPos;
		if (attackRange >= 0) {
			if (inputTile.occupied && inputTile.occupyingUnit.equals(attackUnit)) {
				attackFound = true;
				refresh();
			}
		}

		if (attackRange != 0) {
			if (x > 0 && x - defendUnit.occupiedSpace.xPos <= 0) {
				if (attackRange > 0) {
					checkCounterAttack(array[x - 1][y], attackingAllignment, attackRange - 1);
				} else {
					checkCounterAttack(array[x - 1][y], attackingAllignment, attackRange + 1);
				}
			}
			if (x < array.length - 1 && x - defendUnit.occupiedSpace.xPos >= 0) {
				if (attackRange > 0) {
					checkCounterAttack(array[x + 1][y], attackingAllignment, attackRange - 1);
				} else {
					checkCounterAttack(array[x + 1][y], attackingAllignment, attackRange + 1);
				}
			}
			if (y > 0 && y - defendUnit.occupiedSpace.yPos <= 0) {
				if (attackRange > 0) {
					checkCounterAttack(array[x][y - 1], attackingAllignment, attackRange - 1);
				} else {
					checkCounterAttack(array[x][y - 1], attackingAllignment, attackRange + 1);
				}
			}
			if (y < array[x].length - 1 && y - defendUnit.occupiedSpace.yPos >= 0) {
				if (attackRange > 0) {
					checkCounterAttack(array[x][y + 1], attackingAllignment, attackRange - 1);
				} else {
					checkCounterAttack(array[x][y + 1], attackingAllignment, attackRange + 1);
				}
			}
		}
	}

	public void cancelAttack() {
		attackFound = false;
		for (int n = 0; n < array.length; n++) {
			for (int j = 0; j < array[n].length; j++) {
				array[n][j].colorTile.setNotAttacking();
			}
		}

		if (movingUnit.active) {
			moveToTile.remove(movingUnit);
			attackFromTile.place(movingUnit);
			moveFromTile = attackFromTile;
		}

		attackFromTile = null;
		attackUnit = null;
		defendUnit = null;
	}

	/**
	 * Plays the given Background Song
	 * @param s the name of the mp3 file in Sounds/Songs
	 */
	public void playBackGroundMusic(String s)
	{
		File file = new File("Sounds/Songs/"+s+".mp3");
		try {
			FileInputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			Player player = new Player(bis);
			player.play();
			
		} catch (FileNotFoundException | JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Plays the mp3 sound in Sounds/effects
	 * @param s the name of the effect to be played
	 */
	public void playSoundEffect(String s)
	{
		File file = new File("Sounds/Effects/"+s+".mp3");
		try {
			FileInputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			Player player = new Player(bis);
			player.play();
			
		} catch (FileNotFoundException | JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class MouseListenerTest implements MouseListener,MouseWheelListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int x = 0;
			int y = 0;
			Tile temp = (Tile) arg0.getSource();
			//System.out.println(temp.getWidth());
			//System.out.println(temp.getHeight());
			for (int n = 0; n < array.length; n++) {
				for (int j = 0; j < array[n].length; j++) {
					if (arg0.getSource().equals(array[n][j])) {
						//System.out.println("EUREKA " + n + "," + j);
						x = n;
						y = j;

					}
				}
			}
			
			if (attackFound) {
				if (temp.colorTile.canAttack) {
					createAttackPreview(moveToTile.occupyingUnit, temp.occupyingUnit);
				}
			} else if (!moving) {
				if (temp.occupied && temp.occupyingUnit.active) {
					moving = true;
					movingUnit = temp.occupyingUnit;
					moveFromTile = temp;
					move(array, x, y, movingUnit.moveRange);

				}
			} else if (!temp.colorTile.inMoveRange) {
				cancelMove();

			} else {
				moveFromTile.remove(movingUnit);
				temp.place(movingUnit);
				moveToTile = temp;
				createActionMenu(temp, x, y);
				cancelMove();
			}
			if(temp.occupied)
				playSoundEffect("selectUnit");
			else
				playSoundEffect("click");
			
			refresh();
			

		}

		public void move(Tile[][] allSpaces, int n, int j, int moveRange) {
			moveRange -= 1;
			allSpaces[n][j].colorTile.setMoving();
			if (moveRange > 0) {
				if (n > 0) {
					if (allSpaces[n - 1][j].walkable) {
						if (allSpaces[n - 1][j].occupied && !allSpaces[n - 1][j].occupyingUnit.equals(movingUnit)) {

						} else {
							move(allSpaces, (n - 1), j, moveRange);
						}
					}
				}
				if (n < (allSpaces.length - 1)) {
					if (allSpaces[n + 1][j].walkable) {
						if (allSpaces[n + 1][j].occupied && !allSpaces[n + 1][j].occupyingUnit.equals(movingUnit)) {

						} else {
							move(allSpaces, (n + 1), j, moveRange);
						}
					}
				}
				if (j > 0) {
					if (allSpaces[n][j - 1].walkable) {
						if (allSpaces[n][j - 1].occupied && !allSpaces[n][j - 1].occupyingUnit.equals(movingUnit)) {

						} else {
							move(allSpaces, n, (j - 1), moveRange);
						}
					}
				}
				if (j < (allSpaces[n].length - 1)) {
					if (allSpaces[n][j + 1].walkable) {
						if (allSpaces[n][j + 1].occupied && !allSpaces[n][j + 1].occupyingUnit.equals(movingUnit)) {

						} else {
							move(allSpaces, n, (j + 1), moveRange);
						}
					}
				}
			}

		}

		public void checkAttack(Tile inputTile, int attackingAllignment, int attackRange) {
			int x = inputTile.xPos;
			int y = inputTile.yPos;
			if (attackRange >= 0) {
				if (inputTile.occupied && inputTile.occupyingUnit.allignment != attackingAllignment) {
					if (attackUnit == null) {
						attackFound = true;
					} else if (inputTile.occupyingUnit.equals(attackUnit)) {
						attackFound = true;
					}
				}
			}

			if (attackRange != 0) {
				if (x > 0 && x - movingUnit.occupiedSpace.xPos <= 0) { // go up (because x and y are switched)
					if (attackRange > 0) {
						checkAttack(array[x - 1][y], attackingAllignment, attackRange - 1);
					} else {
						checkAttack(array[x - 1][y], attackingAllignment, attackRange + 1);
					}
				}
				if (x < array.length - 1 && x - movingUnit.occupiedSpace.xPos >= 0) { // go
																						// down
																						// (because
																						// x
																						// and
																						// y
																						// are
																						// switched)
					if (attackRange > 0) {
						checkAttack(array[x + 1][y], attackingAllignment, attackRange - 1);
					} else {
						checkAttack(array[x + 1][y], attackingAllignment, attackRange + 1);
					}
				}
				// attackRange = 0;
				if (y > 0 && y - movingUnit.occupiedSpace.yPos <= 0) { // go
																		// left
					if (attackRange > 0) {
						checkAttack(array[x][y - 1], attackingAllignment, attackRange - 1);
					} else {
						checkAttack(array[x][y - 1], attackingAllignment, attackRange + 1);
					}
				}
				if (y < array[x].length - 1 && y - movingUnit.occupiedSpace.yPos >= 0) { // go
																							// right
					if (attackRange > 0) {
						checkAttack(array[x][y + 1], attackingAllignment, attackRange - 1);
					} else {
						checkAttack(array[x][y + 1], attackingAllignment, attackRange + 1);
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			Tile temp = (Tile) e.getSource();
			if (temp.occupied && !actionMenuOpen && !attackFound) {
				buildCharacterDetailPanel(temp);
				// createAggressivePortraitWindow(temp, temp.occupyingUnit);
			}
			// System.out.println("x: " +temp.xPos + " y: " + temp.yPos);
			// System.out.println("entered: ");
			if (startY + currentSize - 1 == temp.xPos)
				moveSouth = true;
			else if (startY == temp.xPos)
				moveNorth = true;
			else if (startX == temp.yPos)
				moveWest = true;
			else if (startX + currentSize - 1 == temp.yPos)
				moveEast = true;
			else {
				moveEast = false;
				moveWest = false;
				moveNorth = false;
				moveSouth = false;

			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Tile temp = (Tile) e.getSource();
			if (portraitWindowOpen) {
				portraitWindow.dispose();
				portraitWindowOpen = false;
			}

			if (startY + currentSize - 1 == temp.xPos)
				moveSouth = false;
			if (startY == temp.xPos)
				moveNorth = false;
			if (startX == temp.yPos)
				moveWest = false;
			if (startX + currentSize - 1 == temp.yPos)
				moveEast = false;

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		public void cancelMove() {
			moving = false;
			for (int n = 0; n < array.length; n++) {
				for (int j = 0; j < array[n].length; j++) {
					array[n][j].colorTile.setNotMoving();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void createAggressivePortraitWindow(Tile space, Unit character) {
			portraitWindow = new JFrame();
			portraitWindow.setSize(146, 78);
			portraitWindow.setUndecorated(true);

			portraitWindow.setLocation(getX() + space.getX() + space.getWidth() + 10,
					getY() + space.getY() + space.getHeight() / 2);

			portraitPanel = new JPanel();
			portraitPanel.setBackground(new Color(25, 50, 75));

			portrait = new JLabel(character.portrait);
			portraitPanel.add(portrait);
			portraitWindow.add(portraitPanel);

			portraitWindow.setVisible(true);
			portraitWindowOpen = true;

		}

		public void createActionMenu(Tile actingSpace, int xPos, int yPos) {
			actionList = new ArrayList<String>();

			if (false) { // will be changed to check if talkable character
							// adjacent
				actionList.add("Talk");
			}

			for (int range : movingUnit.attackRange) {
				checkAttack(moveToTile, movingUnit.allignment, range);
			}

			if (attackFound) {
				actionList.add("Attack");
			}

			actionList.add("Item");

			actionList.add("Wait");

			actionList.add("Info"); // open a window with full character details
									// - includes skill list and descriptions

			actionWindow = new JFrame();
			actionWindow.setUndecorated(true);
			actionWindow.setSize(100, 100);

			actionWindow.setLocation(getX() + actingSpace.getX() + actingSpace.getWidth() + 10,
					getY() + actingSpace.getY() + actingSpace.getHeight() / 2);

			actionPanel = new JPanel();
			actionPanel.setBackground(Color.BLUE);
			actionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5, true));
			actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
			actionWindow.setSize(55, actionList.size() * 21);

			for (int n = 0; n < actionList.size(); n++) {
				JLabel label = new JLabel(actionList.get(n));
				label.setForeground(Color.YELLOW);
				label.addMouseListener(new ActionMenuLabelMouseListener());
				actionPanel.add(label);

			}

			actionMenuOpen = true;

			actionWindow.addKeyListener(new ActionMenuKeyListener());
			actionWindow.addWindowListener(new ActionWindowListener());
			actionWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			actionWindow.add(actionPanel);
			actionWindow.setAlwaysOnTop(true);
			actionWindow.setVisible(true);
		}

		public void createAttackPreview(Unit attackingUnit, Unit defendingUnit) {
			attackUnit = attackingUnit;
			defendUnit = defendingUnit;

			attackPreviewWindow = new JFrame();
			attackPreviewWindow.setLayout(new BorderLayout());

			JPanel containingPanel = new JPanel();
			containingPanel.setLayout(new GridLayout(0, 2));

			Color attackingBackground = Color.GREEN;
			if (attackingUnit.allignment == 0) {
				attackingBackground = new Color(45, 167, 255);
			} else if (attackingUnit.allignment == 1) {
				attackingBackground = Color.RED;
			}

			JPanel attackingPanel = new JPanel();
			attackingPanel.setLayout(new BorderLayout());
			attackingPanel.setBackground(attackingBackground);
			attackingPanel.setBorder(BorderFactory.createTitledBorder(attackingUnit.name));

			JPanel attackingPortraitPanel = new JPanel();
			JLabel attackingPortrait = new JLabel(attackingUnit.portrait);
			attackingPortraitPanel.add(attackingPortrait);
			attackingPortraitPanel.setBackground(attackingBackground);
			attackingPortraitPanel.setBorder(BorderFactory.createEtchedBorder());

			JPanel attackingInfoPanel = new JPanel();
			JLabel attackingBattleCryLabel = new JLabel(attackingUnit.battleCry);
			JLabel attackingWeaponLabel = new JLabel(attackingUnit.weapon.name);
			JLabel attackingEnthusiasmLabel = new JLabel(
					"Enthusiasm: " + attackingUnit.enthusiasm + " / " + attackingUnit.maxEnthusiasm);
			JLabel attackingStateLabel = new JLabel();
			if (attackingUnit.state == 1) {
				attackingStateLabel.setText("State: ALL FIRED UP!"); // use html
																		// to
																		// spice
																		// up
																		// the
																		// font
			} else if (attackingUnit.state == 0) {
				attackingStateLabel.setText("State: Discouraged");
			} else {
				attackingStateLabel.setText("State: Desperate");
			}
			int dmg = defendingUnit.receiveAttack(attackingUnit, defendingUnit.occupiedSpace);
			if (dmg < 0) {
				dmg = 0;
			}
			JLabel attackingDamage = new JLabel("Damage: " + dmg);
			int atkAccuracy = attackingUnit.weapon.accuracy - defendingUnit.avoidance
					- defendingUnit.occupiedSpace.avoMod;
			if (atkAccuracy > 100) {
				atkAccuracy = 100;
			}
			JLabel attackingAccuracy = new JLabel("Accuracy: " + atkAccuracy);

			attackingInfoPanel.add(attackingBattleCryLabel);
			attackingInfoPanel.add(attackingWeaponLabel);
			attackingInfoPanel.add(attackingEnthusiasmLabel);
			attackingInfoPanel.add(attackingStateLabel);
			attackingInfoPanel.add(attackingDamage);
			attackingInfoPanel.add(attackingAccuracy);
			attackingInfoPanel.setLayout(new BoxLayout(attackingInfoPanel, BoxLayout.Y_AXIS));
			attackingInfoPanel.setBackground(attackingBackground);
			attackingInfoPanel.setBorder(BorderFactory.createEtchedBorder());

			JPanel attackingBuffPanel = new JPanel();
			attackingBuffPanel.setBackground(attackingBackground);
			attackingBuffPanel.setBorder(BorderFactory.createEtchedBorder());

			attackingPanel.add(attackingPortraitPanel, BorderLayout.NORTH);
			attackingPanel.add(attackingInfoPanel, BorderLayout.CENTER);
			attackingPanel.add(attackingBuffPanel, BorderLayout.SOUTH);

			Color defendingBackground = Color.GREEN;
			if (defendingUnit.allignment == 0) {
				defendingBackground = new Color(45, 167, 255);
			} else if (defendingUnit.allignment == 1) {
				defendingBackground = Color.RED;
			}

			JPanel defendingPanel = new JPanel();
			defendingPanel.setLayout(new BorderLayout());
			defendingPanel.setBackground(defendingBackground);
			defendingPanel.setBorder(BorderFactory.createTitledBorder(defendingUnit.name));

			JPanel defendingPortraitPanel = new JPanel();
			JLabel defendingPortrait = new JLabel(defendingUnit.portrait);
			defendingPortraitPanel.add(defendingPortrait);
			defendingPortraitPanel.setBackground(defendingBackground);
			defendingPortraitPanel.setBorder(BorderFactory.createEtchedBorder());

			JPanel defendingInfoPanel = new JPanel();
			JLabel defendingBattleCryLabel = new JLabel(defendingUnit.battleCry);
			JLabel defendingWeaponLabel = new JLabel(defendingUnit.weapon.name);
			JLabel defendingEnthusiasmLabel = new JLabel(
					"Enthusiasm: " + defendingUnit.enthusiasm + " / " + defendingUnit.maxEnthusiasm);
			JLabel defendingStateLabel = new JLabel();
			if (defendingUnit.state == 1) {
				defendingStateLabel.setText("State: ALL FIRED UP!"); // use html
																		// to
																		// spice
																		// up
																		// the
																		// font
			} else if (defendingUnit.state == 0) {
				defendingStateLabel.setText("State: Discouraged");
			} else {
				defendingStateLabel.setText("State: Desperate");
			}
			dmg = attackingUnit.receiveAttack(defendingUnit, attackingUnit.occupiedSpace);
			if (dmg < 0) {
				dmg = 0;
			}
			JLabel defendingDamage = new JLabel("Damage: " + dmg);
			int defAccuracy = defendingUnit.weapon.accuracy - attackingUnit.avoidance
					- attackingUnit.occupiedSpace.avoMod;
			if (defAccuracy > 100) {
				defAccuracy = 100;
			}
			JLabel defendingAccuracy = new JLabel("Accuracy: " + defAccuracy);

			attackFound = false;
			for (int range : defendingUnit.attackRange) {
				checkCounterAttack(defendingUnit.occupiedSpace, defendingUnit.allignment, range);
			}
			if (!attackFound) {
				defendingDamage.setText("Damage: --");
				defendingAccuracy.setText("Accuracy: --");
				attackFound = true;
			}

			defendingInfoPanel.add(defendingBattleCryLabel);
			defendingInfoPanel.add(defendingWeaponLabel);
			defendingInfoPanel.add(defendingEnthusiasmLabel);
			defendingInfoPanel.add(defendingStateLabel);
			defendingInfoPanel.add(defendingDamage);
			defendingInfoPanel.add(defendingAccuracy);
			defendingInfoPanel.setLayout(new BoxLayout(defendingInfoPanel, BoxLayout.Y_AXIS));
			defendingInfoPanel.setBackground(defendingBackground);
			defendingInfoPanel.setBorder(BorderFactory.createEtchedBorder());

			JPanel defendingBuffPanel = new JPanel();
			defendingBuffPanel.setBackground(defendingBackground);
			defendingBuffPanel.setBorder(BorderFactory.createEtchedBorder());

			defendingPanel.add(defendingPortraitPanel, BorderLayout.NORTH);
			defendingPanel.add(defendingInfoPanel, BorderLayout.CENTER);
			defendingPanel.add(defendingBuffPanel, BorderLayout.SOUTH);

			containingPanel.add(attackingPanel);
			containingPanel.add(defendingPanel);

			JPanel commandPanel = new JPanel();
			commandPanel.setLayout(new GridLayout(0, 2));
			confirmAttack = new JButton("Attack!");
			confirmAttack.setBackground(Color.BLACK);
			confirmAttack.setForeground(Color.WHITE);
			confirmAttack.addActionListener(new ConfirmAttackButtonListener());
			commandPanel.add(confirmAttack);
			stopAttacking = new JButton("Cancel Attacks");
			stopAttacking.setBackground(Color.BLACK);
			stopAttacking.setForeground(Color.WHITE);
			stopAttacking.addActionListener(new StopAttackingButtonListener());
			commandPanel.add(stopAttacking);

			attackPreviewWindow.add(containingPanel, BorderLayout.CENTER);
			attackPreviewWindow.add(commandPanel, BorderLayout.SOUTH);

			attackPreviewWindow.addWindowListener(new AttackPreviewWindowListener());
			attackPreviewWindow.setUndecorated(true);
			attackPreviewWindow.setSize(310, 270); // y value needs to depend on
													// number of buffs
			attackPreviewWindow.setLocation(getX() + attackingUnit.occupiedSpace.getX() + 40,
					getY() + attackingUnit.occupiedSpace.getY());
			attackPreviewWindow.setVisible(true);

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			
			System.out.println(e.getPreciseWheelRotation());
			
		}

	}

	private class PlayerSpawnMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			Tile temp = (Tile) e.getSource();
			if (temp.colorTile.canSpawn) {
				spawningTile = temp;
				createSpawnSelectWindow();
			}

			refresh();
		}

		private void createSpawnSelectWindow() {
			spawnSelectWindow = new JFrame();

			spawnSelectPanel = new JPanel();
			spawnSelectPanel.setBackground(Color.WHITE);
			spawnSelectPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			spawnSelectPanel.setLayout(new BoxLayout(spawnSelectPanel, BoxLayout.Y_AXIS));
			spawnSelectWindow.setSize(130, roster.size() * 24);
			spawnSelectWindow.add(spawnSelectPanel);
			spawnSelectWindow.addWindowListener(new SpawnSelectWindowListener());
			spawnSelectWindow.setUndecorated(true);
			spawnSelectWindow.setLocation(getX() + spawningTile.getX() + spawningTile.getWidth() + 10,
					getY() + spawningTile.getY() + spawningTile.getHeight() / 2);

			for (int n = 0; n < roster.size(); n++) {
				JLabel label = new JLabel(roster.get(n).name);
				label.addMouseListener(new SpawnSelectLabelListener());
				spawnSelectPanel.add(label);
			}
			spawnSelectWindow.setVisible(true);
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			Tile temp = (Tile) e.getSource();
			if (temp.occupied && !actionMenuOpen && !attackFound) {
				buildCharacterDetailPanel(temp);
				// createAggressivePortraitWindow(temp, temp.occupyingUnit);
			}
			// System.out.println("x: " +temp.xPos + " y: " + temp.yPos);
			// System.out.println("startX: "+startX + "startY: "+startY);
			if (startY + currentSize - 1 == temp.xPos)
				moveSouth = true;
			else if (startY == temp.xPos)
				moveNorth = true;
			else if (startX == temp.yPos)
				moveWest = true;
			else if (startX + currentSize - 1 == temp.yPos)
				moveEast = true;
			else {
				moveEast = false;
				moveWest = false;
				moveNorth = false;
				moveSouth = false;

			}
			// System.out.println("East: "+moveEast + " West: "+moveWest + "
			// South: "+moveSouth+" North: "+moveNorth);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Tile temp = (Tile) e.getSource();
			if (portraitWindowOpen) {
				portraitWindow.dispose();
				portraitWindowOpen = false;
			}

			if (startY + currentSize - 1 == temp.xPos)
				moveSouth = false;
			if (startY == temp.xPos)
				moveNorth = false;
			if (startX == temp.yPos)
				moveWest = false;
			if (startX + currentSize - 1 == temp.yPos)
				moveEast = false;

		}

	}

	private class SpawnSelectWindowListener implements WindowListener {

		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			if (unfilledSpawns == 0) {
				for (int n = 0; n < array.length; n++) {
					for (int j = 0; j < array[n].length; j++) {
						array[n][j].colorTile.setNotSpawning();
						array[n][j].removeMouseListener(new PlayerSpawnMouseListener());
						array[n][j].addMouseListener(new MouseListenerTest());
					}
				}
				beginTurn(1);
			}
			spawnSelectWindow.dispose();
		}

	}

	private class SpawnSelectLabelListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			String unitName = ((JLabel) e.getSource()).getText();
			for (int n = 0; n < roster.size(); n++) {
				if (unitName.equals(roster.get(n).name)) {
					spawningTile.place(roster.get(n));
					spawningTile.colorTile.setNotSpawning();
					p1Units.add(roster.get(n));
					roster.remove(n);
					unfilledSpawns--;
					System.out.println("Unfilled Spawns: " + unfilledSpawns);
					spawnSelectWindow.dispose();
					break;
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			JLabel temp = (JLabel) e.getSource();
			temp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		}

		public void mouseExited(MouseEvent e) {
			JLabel temp = (JLabel) e.getSource();
			temp.setBorder(null);
		}

	}

	/**
	 * Does something
	 * 
	 * @param action
	 *            String that tells us something
	 */
	public void act(String action) {
		if (action.equals("Wait")) {
			movingUnit.active = false;
			moveFromTile = moveToTile;
			actionWindow.dispose();
			actionMenuOpen = false;

		}
		if (action.equals("Attack")) {
			for (int range : movingUnit.attackRange) {
				setAttack(moveToTile, movingUnit.allignment, range);
			}
			attackFromTile = moveFromTile;
			moveFromTile = moveToTile;
			actionWindow.dispose();
			actionMenuOpen = false;

			// myAttackWillRainDownFromTheSky();
			// checkAttack(moveToTile, movingUnit.allignment,
			// movingUnit.attackRange);
		}
		if (action.equals("Item")) {
			// myAttackWillRainDownFromTheSky();
		}

		checkEnd(movingUnit.allignment);
	}

	public void checkEnd(int team) {
		if (!moveFromTile.occupyingUnit.active) {
			Unit temp = moveFromTile.occupyingUnit;
			moveFromTile.remove(temp);
			moveFromTile.placeInactive(temp);
		}
		if (team == 0) {
			for (int n = 0; n < p1Units.size(); n++) {
				if (p1Units.get(n).active) {
					break;
				}
				if (n == p1Units.size() - 1) {
					beginTurn(2);
				}
			}
		}
		if (team == 1) {
			for (int n = 0; n < p2Units.size(); n++) {
				if (p2Units.get(n).active) {
					break;
				}
				if (n == p2Units.size() - 1) {
					beginTurn(1);
				}
			}
		}
		refresh();
	}

	private class ActionMenuLabelMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel temp = (JLabel) e.getSource();
			act(temp.getText());

		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			JLabel temp = (JLabel) e.getSource();
			temp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		}

		public void mouseExited(MouseEvent e) {
			JLabel temp = (JLabel) e.getSource();
			temp.setBorder(null);
		}

	}

	private class ActionMenuKeyListener implements KeyListener {

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 27) { // escape
				if (actionMenuOpen) {
					actionWindow.dispose();
					actionMenuOpen = false;
				}
			} else if (e.getKeyCode() == 37) { // left arrow key
				actionWindow.setLocation(getX() + moveToTile.getX() - moveToTile.getWidth() - 20,
						getY() + moveToTile.getY() + actionWindow.getHeight() / 2);
			} else if (e.getKeyCode() == 39) { // right arrow key
				actionWindow.setLocation(getX() + moveToTile.getX() + moveToTile.getWidth() + 10,
						getY() + moveToTile.getY() + actionWindow.getHeight() / 2);
			}

		}

		public void keyReleased(KeyEvent e) {
		}

	}

	private class ActionWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		}

		public void windowDeactivated(WindowEvent arg0) {
			System.out.println("REMEMBER ME");
			moveToTile.remove(movingUnit);
			moveFromTile.place(movingUnit);
			actionWindow.dispose();
			actionMenuOpen = false;
			if (attackFromTile == null && attackFound) {
				System.out.println("not borked");
				attackFound = false;
			}
			refresh();

			if (!moveFromTile.occupyingUnit.active) {
				Unit temp = moveFromTile.occupyingUnit;
				moveFromTile.remove(temp);
				moveFromTile.placeInactive(temp);
			}
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}

	}

	private class AttackPreviewWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			attackPreviewWindow.dispose();

		}

	}

	private class ConfirmAttackButtonListener implements ActionListener {

		ArrayList<JLabel> combatText;
		boolean counterAttack;

		public void actionPerformed(ActionEvent e) {
			counterAttack = false;
			attackPreviewWindow.dispose();
			combatText = new ArrayList<JLabel>();
			try {
				attack(attackUnit, defendUnit);

				attackFound = false;
				for (int range : defendUnit.attackRange) {
					checkCounterAttack(defendUnit.occupiedSpace, defendUnit.allignment, range);
				}
				if (attackFound) {
					counterAttack = true;
					attack(defendUnit, attackUnit);
				}

				if (attackUnit.speed >= defendUnit.speed + 5) {
					attack(attackUnit, defendUnit);
				} else if (attackFound && defendUnit.speed >= attackUnit.speed + 5) {
					attack(defendUnit, attackUnit);
				}
			} catch (NullPointerException o) {
				System.out.println("not reached?");
			}

			if (attackUnit.enthusiasm > attackUnit.maxEnthusiasm) {
				attackUnit.enthusiasm = attackUnit.maxEnthusiasm;
			}

			if (defendUnit.enthusiasm > defendUnit.maxEnthusiasm) {
				defendUnit.enthusiasm = defendUnit.maxEnthusiasm;
			}

			attackFromTile = moveToTile;
			attackUnit.active = false;

			// checkEnd(attackUnit.allignment);
			cancelAttack();
			animationOpen = false;

		}

		private void attack(Unit attackingUnit, Unit defendingUnit) {

			int atkAccuracy = attackingUnit.weapon.accuracy - defendingUnit.avoidance
					- defendingUnit.occupiedSpace.avoMod;
			if (Math.random() * 100 < atkAccuracy) {
				int damage = defendingUnit.receiveAttack(attackingUnit, defendingUnit.occupiedSpace);
				if (damage <= 0) {
					damage = 0;
					defendingUnit.enthusiasm += defendingUnit.glory / 2;
					combatText.add(new JLabel(attackingUnit.name + " did no damage."));
					combatText.add(new JLabel(defendingUnit.name + " gained " + defendingUnit.glory / 2
							+ " enthusiasm from a successful deflection"));
				} else {
					combatText.add(new JLabel(attackingUnit.name + " did " + damage + " damage."));
				}
				defendingUnit.enthusiasm -= damage;
				if (damage >= defendingUnit.bleedThreshold && attackingUnit.state != 2) {
					attackingUnit.enthusiasm += attackingUnit.glory;
					combatText.add(new JLabel(attackingUnit.name + " gained " + attackingUnit.glory
							+ " enthusiasm from a devastating blow."));
				}
				if (damage < defendingUnit.bleedThreshold && attackingUnit.state == 1) {
					attackingUnit.enthusiasm -= attackingUnit.glory / 2;
					combatText.add(new JLabel(attackingUnit.name + " lost " + attackingUnit.glory / 2
							+ " enthusiasm in disappointment."));
				}
			} else {
				combatText.add(new JLabel(attackingUnit.name + " missed."));
			}

			if (defendingUnit.enthusiasm <= 0) {
				kill(defendingUnit);
				combatText.add(new JLabel(defendingUnit.name + " perished."));
				if (attackingUnit.state != 2) {
					attackingUnit.enthusiasm += attackingUnit.glory;
					combatText.add(new JLabel(attackingUnit.name + " gained " + attackingUnit.glory
							+ " enthusiasm for defeating a foe."));
				}
			}
			System.out.println("Should be");
			buildAnimationWindow();

		}

		boolean animationOpen = false;
		JPanel animationTextPanel;
		JPanel animationInfoPanel;

		private void buildAnimationWindow() {
			if (!animationOpen) {
				animationOpen = true;
				labelCount = 0;
				animationWindow = new JFrame();

				animationWindow.setUndecorated(true);
				animationWindow.setSize(350, 300);
				animationWindow.setLocation(getX() + getWidth() / 2,
						getY() + getHeight() / 2 - animationWindow.getHeight() / 2);

				animationWindow.setLayout(new BorderLayout());
				animationWindow.addWindowListener(new AnimationWindowListener());
				JPanel animationPanel = new JPanel();
				JPanel emptyPanel1 = new JPanel();
				emptyPanel1.setBackground(Color.CYAN);
				emptyPanel1.add(new JLabel("                         "));
				JPanel emptyPanel2 = new JPanel();
				emptyPanel2.setBackground(Color.CYAN);
				emptyPanel2.add(new JLabel("                         "));
				JPanel fightPanel = new JPanel();
				fightPanel.setBackground(Color.CYAN);
				animationPanel.setLayout(new BorderLayout());
				animationPanel.setBackground(Color.CYAN);
				JLabel attackLabel = new JLabel(attackUnit.createAttackAnimation());
				JLabel defendLabel = new JLabel(defendUnit.graphic);
				animationPanel.add(emptyPanel1, BorderLayout.WEST);
				animationPanel.add(emptyPanel2, BorderLayout.EAST);
				fightPanel.setLayout(new BorderLayout());
				fightPanel.add(attackLabel, BorderLayout.WEST);
				fightPanel.add(defendLabel, BorderLayout.EAST);
				animationPanel.add(fightPanel, BorderLayout.CENTER);

				animationWindow.add(animationPanel, BorderLayout.CENTER);

				animationInfoPanel = new JPanel();
				animationInfoPanel.setLayout(new BorderLayout());
				animationTextPanel = new JPanel();
				animationTextPanel.setLayout(new BoxLayout(animationTextPanel, BoxLayout.Y_AXIS));
				animationInfoPanel.add(animationTextPanel, BorderLayout.CENTER);

				animationWindow.add(animationInfoPanel, BorderLayout.SOUTH);

				System.out.println("reached");
				animationWindow.setVisible(true);
			} else {

			}

			moveAttackTextTimer.start();
		}

		ActionListener moveAttackText = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animationTextPanel.add(combatText.get(labelCount));
				labelCount++;

				animationTextPanel.updateUI();
				if (labelCount == combatText.size()) {
					moveAttackTextTimer.stop();
				}
			}
		};

		Timer moveAttackTextTimer = new Timer(500, moveAttackText);

		int labelCount;

	}

	private class AnimationWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			animationWindow.dispose();
			checkEnd(movingUnit.allignment);
		}

	}

	
	private class Zoom implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println(e.getKeyChar());
			if(e.getKeyChar() == '-')
				Zoom(currentSize+1);
			
			if(e.getKeyChar() == '+')
				Zoom(currentSize-1);
			
			try {
				mapImage = resizeImage("Art\\Maps\\" + levelName, array.length * array[startY][startX].getWidth(), 
						array[0].length*array[startY][startX].getHeight());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	public void Zoom(int n)
	{
		currentSize = n;
		
	}
	public void kill(Unit deadGuy) {
		if (p1Units.contains(deadGuy)) {
			p1Units.remove(deadGuy);
		} else if (p2Units.contains(deadGuy)) {
			p2Units.remove(deadGuy);
		}
		deadGuy.die();
		deadGuy = null;

		checkWinLoss();
	}

	public void checkWinLoss() {
		if (p1Units.size() == 0) {
			JOptionPane.showMessageDialog(null, "Player 2 Wins!");
		} else if (p2Units.size() == 0) {
			JOptionPane.showMessageDialog(null, "Player 1 Wins!");
		}
	}

	private class StopAttackingButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			attackPreviewWindow.dispose();
			cancelAttack();
			refresh();
		}
	}

}
