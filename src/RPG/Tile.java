package RPG;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;


public class Tile extends JPanel{
	public boolean occupied;
	public boolean inMoveRange;
	public boolean canAttack;
	public int xPos;
	public int yPos;
	
	public boolean walkable;
	public int defenseMod;
	public int avoMod;
	
	public JLabel character;
	public Unit occupyingUnit;
	
	public Tile colorTile;
	
	public Tile(){
		SpringLayout layout = new SpringLayout();
		
		occupied = false;
		inMoveRange = false;
		canAttack = false;
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		colorTile = new Tile(4);
		
		walkable = true;
		defenseMod = 0;
		avoMod = 0;
		
		layout.putConstraint(SpringLayout.WEST, colorTile, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, colorTile, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, colorTile, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, colorTile, 0, SpringLayout.SOUTH, this);
		setLayout(layout);
		
		add(colorTile);
		
	}
	
	public Tile(int x, int y, int terrainType){
		xPos = x;
		yPos = y;
		
		SpringLayout layout = new SpringLayout();
		
		occupied = false;
		inMoveRange = false;
		canAttack = false;
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		colorTile = new Tile(4);
		
		walkable = true;
		defenseMod = 0;
		avoMod = 0;
		
		layout.putConstraint(SpringLayout.WEST, colorTile, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, colorTile, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, colorTile, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, colorTile, 0, SpringLayout.SOUTH, this);
		setLayout(layout);
		
		add(colorTile);
				
	}
	
	public Tile(int joke){
		inMoveRange = false;
		canAttack = false;
		setOpaque(false);
	}
	
	public void updateIcon(){
		if(occupied){
			if(occupyingUnit.active){
				character = new JLabel(occupyingUnit.graphic);
				add(character);
			}
			else{
				character = new JLabel(occupyingUnit.inactiveGraphic);
				add(character);
			}
		}
	}
		
	public void place(Unit characterName){
		character = new JLabel(characterName.graphic);
		add(character);
		characterName.setPlaced(true);
		characterName.occupiedSpace = this;
		occupyingUnit = characterName;
		occupied = true;
	}
	
	public void placeInactive(Unit characterName){
		character = new JLabel(characterName.inactiveGraphic);
		add(character);
		characterName.setPlaced(true);
		characterName.occupiedSpace = this;
		occupyingUnit = characterName;
		occupied = true;
	}
	
	public void remove(Unit characterName){
		remove(character);
		characterName.setPlaced(false);
		occupyingUnit = null;
		occupied = false;
		character = null;
		characterName.occupiedSpace = null;
	}
	
	public void setMoveRange(boolean input){
		inMoveRange = input;
	}
	
	public void setMoving(){
		setBackground(new Color(0,0,255,50));
		inMoveRange = true;
		setOpaque(true);
	}
	
	public void setNotMoving(){
		setOpaque(false);
		inMoveRange = false;
	}
	
	public void setAttacking(){
		setBackground(new Color(255,0,0,50));
		canAttack = true;
		setOpaque(true);
	}
	
	public void setNotAttacking(){
		setOpaque(false);
		canAttack = false;
	}
	
	private void drawHealthBar(Graphics g){
		g.drawRect(character.getX(), character.getY() + character.getHeight() + 7, character.getWidth(), 5);
		
		occupyingUnit.calculateState();
		
		if(occupyingUnit.state == 1){
			g.setColor(new Color(106, 239, 97));
		}
		else if(occupyingUnit.state == 0){
			g.setColor(new Color(228, 221, 37));
		}
		else{
			g.setColor(new Color(210, 56, 39));
		}
		
		double hpPercent = (double)occupyingUnit.enthusiasm / (double)occupyingUnit.maxEnthusiasm;
		int length = (int)(hpPercent * character.getWidth()) - 2;
		g.fillRect(character.getX() + 1, character.getY() + character.getHeight() + 8, length, 3);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(occupied){
			drawHealthBar(g);
		}
	}
	
}
