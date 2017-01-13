package RPG;
//import needs;
//import needed classes;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import java.awt.*;

public class RPGApp extends JFrame {
	public ArrayList<Unit> p1Units = new ArrayList<Unit>();
	public ArrayList<Unit> p2Units = new ArrayList<Unit>();
	
	public int activePlayer = 0;
	
	public ArrayList<Unit>[] players;
	
	public static void main(String[] args){
		RPGApp start = new RPGApp();
	}
		
	Unit senorSavesTheDay = new Unit("SenorSavesTheDay",0, "swordIron Sword");
	Unit pizzaJew = new Unit("PizzaJew",1, "Fists");
	Unit elLady = new Unit("ElLady",0, "bowCrappy Bow");
	Unit christmasNinja = new Unit("ChristmasNinja",1, "throwThrowing Knives");
	SpringLayout layout;
	
	public void myAttackWillRainDownFromTheSky(){
		for(int n = 0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				array[n][j].colorTile.setAttacking();
			}
		}
	}
	
	public RPGApp(){
		
		UIManager.put("Label.font", new Font("Garamond", Font.BOLD, 14));
		
		setTitle("Grid");
		setSize(1000,750);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(new SpawnListener());
				
		buildGrid();		
				
		add(grid, BorderLayout.CENTER);
		setVisible(true);
		refreshGrid();
	}
	
	public void refreshGrid(){
		setVisible(false);
		remove(grid);
		characterDetailPanel = new JPanel();
		characterDetailPanel.setLayout(new BorderLayout());
		layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, grid, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, grid, this.getWidth() - grid.getWidth()/16 - 146, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, grid, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, grid, this.getHeight() - grid.getHeight()/16, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, characterDetailPanel, 0, SpringLayout.EAST, grid);
		layout.putConstraint(SpringLayout.EAST, characterDetailPanel, this.getWidth(), SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, characterDetailPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, characterDetailPanel, this.getHeight() - grid.getHeight()/16, SpringLayout.NORTH, this);
		
		setLayout(layout);
		add(grid);
		add(characterDetailPanel);
		setVisible(true);
	}
	
	ActionListener scrollRight = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			scroll(visibleArray[0][0],1,0);
		}
	};
	
	ActionListener scrollLeft = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			scroll(visibleArray[0][0],-1,0);
		}
	};
	
	ActionListener scrollUp = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			scroll(visibleArray[0][0],0,1);
		}
	};
	
	ActionListener scrollDown = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			scroll(visibleArray[0][0], 0,-1);
		}
	};
	
	JPanel grid;

	Tile[][] array = new Tile[20][20];
	Tile[][] visibleArray = new Tile[8][8];
	
	
	private void buildGrid(){
		System.out.println(array.length);

		grid = new JPanel();
		
		grid.setLayout(new GridLayout(array.length,array.length));

		
		for(int n =0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				array[n][j] = new Tile(n, j, 0);
				array[n][j].addMouseListener(new MouseListenerTest());
				if(j==array[n].length - 1){
					//array[n][j].addMouseListener(new ScrollRightMouseListener());
				}
				
			//	array[n][j].setBackground(new Color(0,0,0,0));
				grid.add(array[n][j]);
			}
		}
		
	}
		
	JPanel characterDetailPanel;
	JPanel characterDetailAll;
	JPanel characterPortrait;
	JPanel characterStats;
	JPanel characterItems;
	
	private void buildCharacterDetailPanel(Tile space){
		
		characterDetailPanel.removeAll();
		JLabel portraitName = new JLabel(space.occupyingUnit.name);
		characterDetailPanel.add(portraitName, BorderLayout.NORTH);
		
		characterDetailAll = new JPanel();
		characterDetailAll.setLayout(new BorderLayout());
		
		characterPortrait = new JPanel();
		JLabel portraitImage = new JLabel(new ImageIcon(space.occupyingUnit.name + "Portrait.png"));
		characterPortrait.add(portraitImage);
		characterPortrait.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		characterStats = new JPanel();
		characterStats.setLayout(new BoxLayout(characterStats, BoxLayout.Y_AXIS));
		JLabel enthusiasmLabel = new JLabel("Enthusiasm: " + space.occupyingUnit.enthusiasm + " / " + space.occupyingUnit.maxEnthusiasm);
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
	
	public void scroll(Tile topLeft, int xChange, int yChange){
		int x = topLeft.xPos;
		int y = topLeft.yPos;
		
		if(x<visibleArray[y].length-1 && xChange>0){
			x+=xChange;
		}
		if(x>0 && xChange<0){
			x-=xChange;
		}
		if(y<visibleArray.length-1 && yChange>0){
			y+=yChange;
		}
		if(y>0 && yChange<0){
			y-=yChange;
		}
		
		array[0][0].removeMouseListener(array[0][0].getMouseListeners()[1]);
		
		for(int n = 0;n<visibleArray.length;n++){
			for(int j = 0;j<visibleArray[n].length;j++){
				visibleArray[n][j] = array[x+n][y+j];
			}
		}
	}
		
	public void beginTurn(int playerNumber){
		if(playerNumber==1){
			for(int n = 0;n < p1Units.size();n++){
				p1Units.get(n).active = true;
			}
		}
		else if(playerNumber==2){
			for(int n = 0;n < p2Units.size();n++){
				p2Units.get(n).active = true;
			}
		}
		for(int n = 0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				if(array[n][j].occupied){
					Unit temp = array[n][j].occupyingUnit;
					array[n][j].remove(temp);
					array[n][j].place(temp);
				}
			}
		}
		
		JOptionPane.showMessageDialog(null, "Player " + playerNumber + " turn");
		
		for(int n = 0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				if(array[n][j].occupied){
					Unit temp = array[n][j].occupyingUnit;
					array[n][j].remove(temp);
					array[n][j].place(temp);
				}
			}
		}
	}
	
	public boolean moving = false;
	public boolean actionMenuOpen = false;
	
	public void refresh(){
		for(int n = 0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				array[n][j].updateUI();
			}
		}
	}
	
	public class SpawnListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(p1Units.size()<2){
				p1Units.add(senorSavesTheDay);
				p1Units.add(elLady);
				p2Units.add(pizzaJew);
				p2Units.add(christmasNinja);
			}
			if(!senorSavesTheDay.placed){
				array[1][1].place(senorSavesTheDay);
				beginTurn(1);
			}
			if(!pizzaJew.placed){
				array[2][2].place(pizzaJew);
			}
			if(!elLady.placed){
				array[4][6].place(elLady);
			}
			if(!christmasNinja.placed){
				array[3][0].place(christmasNinja);
			}
			refresh();
			System.out.println(arg0.getKeyCode());
				
			if(arg0.getKeyCode()==32){
				if(attackFound){
					cancelAttack();
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	Unit movingUnit;
	Tile moveFromTile;
	Tile moveToTile;
	Tile attackFromTile;
	
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
	
	public void setAttack(Tile inputTile, int attackingAllignment, int attackRange){
		int x = inputTile.xPos;
		int y = inputTile.yPos;
		if(attackRange>=0){
			if(inputTile.occupied && inputTile.occupyingUnit.allignment != attackingAllignment){
				inputTile.colorTile.setAttacking();
				attackFound = true;
				refresh();
			}
		}
		
		if(attackRange!=0){
			if(x>0){
				if(attackRange>0){
					setAttack(array[x-1][y], attackingAllignment, attackRange-1);
				}
				else{
					setAttack(array[x-1][y], attackingAllignment, attackRange+1);
				}
			}
			if(x<array.length-1){
				if(attackRange>0){
					setAttack(array[x+1][y], attackingAllignment, attackRange-1);
				}
				else{
					setAttack(array[x+1][y], attackingAllignment, attackRange+1);
				}
			}
			//attackRange = 0;
			if(y>0){
				if(attackRange>0){
					setAttack(array[x][y-1], attackingAllignment, attackRange-1);
				}
				else{
					setAttack(array[x][y-1], attackingAllignment, attackRange+1);
				}
			}
			if(y<array[x].length-1){
				if(attackRange>0){
					setAttack(array[x][y+1], attackingAllignment, attackRange-1);
				}
				else{
					setAttack(array[x][y+1], attackingAllignment, attackRange+1);
				}
			}
		}
	}
	
	public void cancelAttack(){
		attackFound = false;
		for(int n = 0;n<array.length;n++){
			for(int j = 0;j<array[n].length;j++){
				array[n][j].colorTile.setNotAttacking();
			}
		}
		
		if(movingUnit.active){
			moveToTile.remove(movingUnit);
			attackFromTile.place(movingUnit);
			moveFromTile = attackFromTile;
		}
		
		attackFromTile = null;
		attackUnit = null;
		defendUnit = null;
	}
	
	public class MouseListenerTest implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int x = 0;
			int y = 0;
			Tile temp = (Tile)arg0.getSource();
			System.out.println(temp.getWidth());
			System.out.println(temp.getHeight());
			for(int n = 0;n<array.length;n++){
				for(int j = 0;j<array[n].length;j++){
					if(arg0.getSource().equals	(array[n][j])){
						System.out.println("EUREKA " + n + "," + j);
						x = n;
						y = j;
												
					}
				}
			}
			if(attackFound){
				if(temp.colorTile.canAttack){
					createAttackPreview(moveToTile.occupyingUnit, temp.occupyingUnit);
				}
			}
			else if(!moving){
				if(temp.occupied && temp.occupyingUnit.active){
					moving = true;
					movingUnit = temp.occupyingUnit;
					moveFromTile = temp;
					move(array,x,y,movingUnit.moveRange);
				}
			}
			else if(!temp.colorTile.inMoveRange){
				cancelMove();
			}
			else{
				moveFromTile.remove(movingUnit);
				temp.place(movingUnit);
				moveToTile = temp;
				createActionMenu(temp, x, y);
				cancelMove();
			}
			refresh();
	
		}

		public void move(Tile[][] allSpaces,int n, int j, int moveRange){
			moveRange -= 1;
			allSpaces[n][j].colorTile.setMoving();
			if(moveRange>0){
				if(n>0){
					if(allSpaces[n-1][j].walkable){
						if(allSpaces[n-1][j].occupied && !allSpaces[n-1][j].occupyingUnit.equals(movingUnit)){
							
						}
						else{
							move(allSpaces,(n-1),j,moveRange);
						}
					}
				}
				if(n< (allSpaces.length - 1)){
					if(allSpaces[n+1][j].walkable){
						if(allSpaces[n+1][j].occupied && !allSpaces[n+1][j].occupyingUnit.equals(movingUnit)){
							
						}
						else{
							move(allSpaces,(n+1),j,moveRange);
						}
					}
				}
				if(j>0){
					if(allSpaces[n][j-1].walkable){
						if(allSpaces[n][j-1].occupied && !allSpaces[n][j-1].occupyingUnit.equals(movingUnit)){
							
						}
						else{
							move(allSpaces,n,(j-1),moveRange);
						}
					}
				}
				if(j< (allSpaces[n].length - 1)){
					if(allSpaces[n][j+1].walkable){
						if(allSpaces[n][j+1].occupied && !allSpaces[n][j+1].occupyingUnit.equals(movingUnit)){
							
						}
						else{
							move(allSpaces,n,(j+1),moveRange);
						}
					}
				}
			}
		}
				
		public void checkAttack(Tile inputTile, int attackingAllignment, int attackRange){
			int x = inputTile.xPos;
			int y = inputTile.yPos;
			if(attackRange>=0){
				if(inputTile.occupied && inputTile.occupyingUnit.allignment != attackingAllignment){
					if(attackUnit==null){
						attackFound = true;
					}
					else if(inputTile.occupyingUnit.equals(attackUnit)){
						attackFound = true;
					}
				}
			}
			
			if(attackRange!=0){
				if(x>0){
					if(attackRange>0){
						checkAttack(array[x-1][y], attackingAllignment, attackRange-1);
					}
					else{
						checkAttack(array[x-1][y], attackingAllignment, attackRange+1);
					}
				}
				if(x<array.length-1){
					if(attackRange>0){
						checkAttack(array[x+1][y], attackingAllignment, attackRange-1);
					}
					else{
						checkAttack(array[x+1][y], attackingAllignment, attackRange+1);
					}
				}
				//attackRange = 0;
				if(y>0){
					if(attackRange>0){
						checkAttack(array[x][y-1], attackingAllignment, attackRange-1);
					}
					else{
						checkAttack(array[x][y-1], attackingAllignment, attackRange+1);
					}
				}
				if(y<array[x].length-1){
					if(attackRange>0){
						checkAttack(array[x][y+1], attackingAllignment, attackRange-1);
					}
					else{
						checkAttack(array[x][y+1], attackingAllignment, attackRange+1);
					}
				}
			}
		}
		
		public void myAttacksWillRainDownFromTheSky(){
			for(int n = 0;n<array.length;n++){
				for(int j = 0;j<array[n].length;j++){
					array[n][j].colorTile.setAttacking();
				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			Tile temp = (Tile)e.getSource();
			if(temp.occupied && !actionMenuOpen && !attackFound){
				buildCharacterDetailPanel(temp);
				//createAggressivePortraitWindow(temp, temp.occupyingUnit);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(portraitWindowOpen){
				portraitWindow.dispose();
				portraitWindowOpen = false;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}
		
		public void cancelMove(){
			moving = false;
			for(int n = 0;n<array.length;n++){
				for(int j = 0;j<array[n].length;j++){
					array[n][j].colorTile.setNotMoving();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
				
		public void createAggressivePortraitWindow(Tile space, Unit character){
			portraitWindow = new JFrame();
			portraitWindow.setSize(146, 78);
			portraitWindow.setUndecorated(true);
			
			portraitWindow.setLocation(getX() + space.getX() + space.getWidth() + 10, getY() + space.getY()+ space.getHeight()/2);
			
			portraitPanel = new JPanel();
			portraitPanel.setBackground(new Color(25, 50, 75));
			
			portrait = new JLabel(character.portrait);
			portraitPanel.add(portrait);
			portraitWindow.add(portraitPanel);

			portraitWindow.setVisible(true);
			portraitWindowOpen = true;
			
		}
		
		public void createActionMenu(Tile actingSpace, int xPos, int yPos){
			actionList = new ArrayList<String>();
			
			if(false){  //will be changed to check if talkable character adjacent
				actionList.add("Talk");
			}
			
			checkAttack(moveToTile, movingUnit.allignment, movingUnit.attackRange);
			
			if(attackFound){
				actionList.add("Attack");
			}
			
			actionList.add("Item");
			
			actionList.add("Wait");
			
			actionList.add("Info"); // open a window with full character details - includes skill list and descriptions
			
			actionWindow = new JFrame();
			actionWindow.setUndecorated(true);
			actionWindow.setSize(100, 100);
			
			actionWindow.setLocation(getX() + actingSpace.getX() + actingSpace.getWidth() + 10, 
					getY() + actingSpace.getY()+ actingSpace.getHeight()/2);
			
			actionPanel = new JPanel();
			actionPanel.setBackground(Color.BLUE);
			actionPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5, true));
			actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
			actionWindow.setSize(55,actionList.size() * 21);
			
			for(int n = 0;n<actionList.size();n++){
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
		
		public void createAttackPreview(Unit attackingUnit, Unit defendingUnit){
			attackUnit = attackingUnit;
			defendUnit = defendingUnit;
			
			attackPreviewWindow = new JFrame();
			attackPreviewWindow.setLayout(new BorderLayout());
			
			JPanel containingPanel = new JPanel();
			containingPanel.setLayout(new GridLayout(0,2));
			
			Color attackingBackground = Color.GREEN;
			if(attackingUnit.allignment==0){
				attackingBackground = new Color(45, 167, 255);
			}
			else if(attackingUnit.allignment == 1){
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
			JLabel attackingEnthusiasmLabel = new JLabel("Enthusiasm: " + attackingUnit.enthusiasm + " / " + attackingUnit.maxEnthusiasm);
			JLabel attackingStateLabel = new JLabel();
			if(attackingUnit.state==1){
				attackingStateLabel.setText("State: ALL FIRED UP!");		//use html to spice up the font
			}
			else if(attackingUnit.state == 0){
				attackingStateLabel.setText("State: Discouraged");
			}
			else{
				attackingStateLabel.setText("State: Desperate");
			}
			int dmg = defendingUnit.receiveAttack(attackingUnit, defendingUnit.occupiedSpace);
			if(dmg<0){
				dmg = 0;
			}
			JLabel attackingDamage = new JLabel("Damage: " + dmg);
			int atkAccuracy = attackingUnit.weapon.accuracy - defendingUnit.avoidance - defendingUnit.occupiedSpace.avoMod;
			if(atkAccuracy>100){
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
			if(defendingUnit.allignment==0){
				defendingBackground = new Color(45, 167, 255);
			}
			else if(defendingUnit.allignment==1){
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
			JLabel defendingEnthusiasmLabel = new JLabel("Enthusiasm: " + defendingUnit.enthusiasm + " / " + defendingUnit.maxEnthusiasm);
			JLabel defendingStateLabel = new JLabel();
			if(defendingUnit.state==1){
				defendingStateLabel.setText("State: ALL FIRED UP!");		//use html to spice up the font
			}
			else if(defendingUnit.state == 0){
				defendingStateLabel.setText("State: Discouraged");
			}
			else{
				defendingStateLabel.setText("State: Desperate");
			}
			dmg = attackingUnit.receiveAttack(defendingUnit, attackingUnit.occupiedSpace);
			if(dmg<0){
				dmg = 0;
			}
			JLabel defendingDamage = new JLabel("Damage: " + dmg);
			int defAccuracy = defendingUnit.weapon.accuracy - attackingUnit.avoidance - attackingUnit.occupiedSpace.avoMod;
			if(defAccuracy>100){
				defAccuracy = 100;
			}
			JLabel defendingAccuracy = new JLabel("Accuracy: " + defAccuracy);
			
			attackFound = false;
			checkAttack(defendingUnit.occupiedSpace, defendingUnit.allignment, defendingUnit.attackRange);
			if(!attackFound){
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
			commandPanel.setLayout(new GridLayout(0,2));
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
			attackPreviewWindow.setSize(310, 270); //y value needs to depend on number of buffs
			attackPreviewWindow.setLocation(getX() + attackingUnit.occupiedSpace.getX() + 40, getY() + attackingUnit.occupiedSpace.getY());
			attackPreviewWindow.setVisible(true);
			
		} 
		
	}

	private class ScrollRightMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		Timer scrollRightTimer = new Timer(250, scrollRight);
				
		public void mouseEntered(MouseEvent e) {
			//start timer
			scrollRightTimer.start();
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			//stop timer
			scrollRightTimer.stop();
		}
		
	}
	
	private class ScrollLeftMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		Timer scrollLeftTimer = new Timer(250, scrollLeft);
				
		public void mouseEntered(MouseEvent e) {
			//start timer
			scrollLeftTimer.start();
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			//stop timer
			scrollLeftTimer.stop();
		}
		
	}
	
	private class ScrollUpMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		Timer scrollUpTimer = new Timer(250, scrollUp);
				
		public void mouseEntered(MouseEvent e) {
			//start timer
			scrollUpTimer.start();
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			//stop timer
			scrollUpTimer.stop();
		}
		
	}
	
	private class ScrollDownMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		Timer scrollDownTimer = new Timer(250, scrollDown);
				
		public void mouseEntered(MouseEvent e) {
			//start timer
			scrollDownTimer.start();
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			//stop timer
			scrollDownTimer.stop();
		}
		
	}
	
	/**
	 * Does something
	 * @param action String that tells us something
	 */
	public void act(String action){
		if(action.equals("Wait")){
			movingUnit.active = false;
			moveFromTile = moveToTile;
			actionWindow.dispose();
			actionMenuOpen = false;
			
		}
		if(action.equals("Attack")){
			setAttack(moveToTile, movingUnit.allignment, movingUnit.attackRange);
			attackFromTile = moveFromTile;
			moveFromTile = moveToTile;
			actionWindow.dispose();
			actionMenuOpen = false;
			
			//myAttackWillRainDownFromTheSky();
			//checkAttack(moveToTile, movingUnit.allignment, movingUnit.attackRange);
		}
		if(action.equals("Item")){
			//myAttackWillRainDownFromTheSky();
		}
		
		checkEnd(movingUnit.allignment);
	}
	
	public void checkEnd(int team){
		if(!moveFromTile.occupyingUnit.active){
			Unit temp = moveFromTile.occupyingUnit;
			moveFromTile.remove(temp);
			moveFromTile.placeInactive(temp);
		}
		if(team == 0){
			for(int n = 0;n<p1Units.size();n++){
				if(p1Units.get(n).active){
					break;
				}
				if(n==p1Units.size()-1){
					beginTurn(2);
				}
			}
		}
		if(team == 1){
			for(int n = 0;n<p2Units.size();n++){
				if(p2Units.get(n).active){
					break;
				}
				if(n==p2Units.size()-1){
					beginTurn(1);
				}
			}
		}
		refresh();
	}
	
	private class ActionMenuLabelMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel temp = (JLabel)e.getSource();
			act(temp.getText());
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel temp = (JLabel)e.getSource();
			temp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel temp = (JLabel)e.getSource();
			temp.setBorder(null);
		}
		
	}
	
	
	private class ActionMenuKeyListener implements KeyListener{

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==27){ // escape
				if(actionMenuOpen){
					actionWindow.dispose();
					actionMenuOpen = false;
				}
			}
			else if(e.getKeyCode()==37){ //left arrow key
				actionWindow.setLocation(getX() + moveToTile.getX() - moveToTile.getWidth() - 20,
						getY() + moveToTile.getY() + actionWindow.getHeight()/2);
			}
			else if(e.getKeyCode()==39){ //right arrow key
				actionWindow.setLocation(getX() + moveToTile.getX() + moveToTile.getWidth() + 10, 
						getY() +  moveToTile.getY() + actionWindow.getHeight()/2);
			}
			/*
			
			else if(e.getKeyCode()==38){ //up arrow key
				actionWindow.setLocation(moveToTile.getX() - moveToTile.getWidth() - 20, actionWindow.getY());
				System.out.println("working");
			}
			else if(e.getKeyCode()==40){ //down arrow key
				actionWindow.setLocation(moveToTile.getX() - moveToTile.getWidth() - 20, actionWindow.getY());
				System.out.println("working");
			}
			*/
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	private class ActionWindowListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowDeactivated(WindowEvent arg0) {
			System.out.println("REMEMBER ME");
			moveToTile.remove(movingUnit);
			moveFromTile.place(movingUnit);
			actionWindow.dispose();
			actionMenuOpen = false;
			if(attackFromTile==null && attackFound){
				System.out.println("not borked");
				attackFound = false;
			}
			refresh();
			
			if(!moveFromTile.occupyingUnit.active){
				Unit temp = moveFromTile.occupyingUnit;
				moveFromTile.remove(temp);
				moveFromTile.placeInactive(temp);
			}
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	private class AttackPreviewWindowListener implements WindowListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			attackPreviewWindow.dispose();
			
		}
		
	}
	
	
	private class ConfirmAttackButtonListener implements ActionListener{
		
		ArrayList<JLabel> combatText;
		boolean counterAttack;

		public void actionPerformed(ActionEvent e) {
			counterAttack = false;
			attackPreviewWindow.dispose();
			combatText = new ArrayList<JLabel>();
			try{
				attack(attackUnit, defendUnit);
				
				attackFound = false;
				setAttack(defendUnit.occupiedSpace, defendUnit.allignment, defendUnit.attackRange);
				if(attackFound){
					counterAttack = true;
					attack(defendUnit, attackUnit);
				}
				
				if(attackUnit.speed >= defendUnit.speed + 5){
					attack(attackUnit, defendUnit);
				}
				else if(attackFound && defendUnit.speed >= attackUnit.speed + 5){
					attack(defendUnit, attackUnit);
				}
			}
			catch(NullPointerException o){
				System.out.println("not reached?");
			}
			
			if(attackUnit.enthusiasm > attackUnit.maxEnthusiasm){
				attackUnit.enthusiasm = attackUnit.maxEnthusiasm;
			}
			
			if(defendUnit.enthusiasm > defendUnit.maxEnthusiasm){
				defendUnit.enthusiasm = defendUnit.maxEnthusiasm;
			}
			
			attackFromTile = moveToTile;
			attackUnit.active = false;
			
			checkEnd(attackUnit.allignment);
			cancelAttack();
			animationOpen = false;
			
		}
		
		private void attack(Unit attackingUnit, Unit defendingUnit){
			
			int atkAccuracy = attackingUnit.weapon.accuracy - defendingUnit.avoidance - defendingUnit.occupiedSpace.avoMod;
			if(Math.random()*100 < atkAccuracy){
				int damage = defendingUnit.receiveAttack(attackingUnit, defendingUnit.occupiedSpace);
				if(damage <= 0){
					damage = 0;
					defendingUnit.enthusiasm += defendingUnit.glory / 2;
					combatText.add(new JLabel(attackingUnit.name + " did no damage."));
					combatText.add(new JLabel(defendingUnit.name + " gained " + defendingUnit.glory/2 + " enthusiasm from a successful deflection"));
				}
				else{
					combatText.add(new JLabel(attackingUnit.name + " did " + damage + " damage."));
				}
				defendingUnit.enthusiasm -= damage;
				if(damage >= defendingUnit.bleedThreshold && attackingUnit.state != 2){
					attackingUnit.enthusiasm += attackingUnit.glory;
					combatText.add(new JLabel(attackingUnit.name + " gained " + attackingUnit.glory + " enthusiasm from a devastating blow."));
				}
				if(damage < defendingUnit.bleedThreshold && attackingUnit.state == 1){
					attackingUnit.enthusiasm -= attackingUnit.glory / 2;
					combatText.add(new JLabel(attackingUnit.name + " lost " + attackingUnit.glory/2 + " enthusiasm in disappointment."));
				}
			}
			else{
				combatText.add(new JLabel(attackingUnit.name + " missed."));
			}
			
			if(defendingUnit.enthusiasm<=0){
				kill(defendingUnit);
				combatText.add(new JLabel(defendingUnit.name + " perished."));
				if(attackingUnit.state != 2){
					attackingUnit.enthusiasm += attackingUnit.glory;
					combatText.add(new JLabel(attackingUnit.name + " gained " + attackingUnit.glory + " enthusiasm for deafeating a foe."));
				}
			}
			System.out.println("Should be");
			buildAnimationWindow();
			
		}
		
		boolean animationOpen = false;
		JPanel animationTextPanel;
		JPanel animationInfoPanel;
		
		private void buildAnimationWindow(){
			if(!animationOpen){
				animationOpen = true;
				labelCount = 0;
				animationWindow = new JFrame();

				animationWindow.setUndecorated(true);
				animationWindow.setSize(350, 300);
				animationWindow.setLocation(getX() + WIDTH/2, getY() + HEIGHT/2);
				
				animationWindow.setLayout(new BorderLayout());
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
			}
			else{
				
			}
			
			moveAttackTextTimer.start();
		}
				
		ActionListener moveAttackText = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				animationTextPanel.add(combatText.get(labelCount));
				labelCount++;
				
				animationTextPanel.updateUI();
				if(labelCount==combatText.size()){
					moveAttackTextTimer.stop();
				}
			}
		};
		
		Timer moveAttackTextTimer = new Timer(500, moveAttackText);
		
		int labelCount;
		
	}
	
	public void kill(Unit deadGuy){
		if(p1Units.contains(deadGuy)){
			p1Units.remove(deadGuy);
		}
		else if(p2Units.contains(deadGuy)){
			p2Units.remove(deadGuy);
		}
		deadGuy.die();
		deadGuy = null;
		
		checkWinLoss();
	}
	
	public void checkWinLoss(){
		if(p1Units.size()==0){
			JOptionPane.showMessageDialog(null, "Player 2 Wins!");
		}
		else if(p2Units.size()==0){
			JOptionPane.showMessageDialog(null, "Player 1 Wins!");
		}
	}
	
	private class StopAttackingButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			attackPreviewWindow.dispose();
			cancelAttack();
			refresh();
		}
	}
	
}
