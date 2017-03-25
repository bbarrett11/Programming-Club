package RPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Tile extends JPanel {
	public boolean occupied;
	public boolean inMoveRange;
	public boolean canAttack;
	public boolean canSpawn;
	public int xPos;
	public int yPos;

	public boolean walkable;
	public int defenseMod;
	public int avoMod;

	public JLabel character;
	public Unit occupyingUnit;

	public Tile colorTile;
	SpringLayout layout;

	public Tile() {
		layout = new SpringLayout();

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

	public Tile(int x, int y, int terrainType) {
		setOpaque(false);

		xPos = x;
		yPos = y;

		layout = new SpringLayout();

		occupied = false;
		inMoveRange = false;
		canAttack = false;
		canSpawn = false;
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

	public Tile(int joke) {
		inMoveRange = false;
		canAttack = false;
		canSpawn = false;
		setOpaque(false);
	}

	public void updateIcon(boolean down) {
		if (occupied&&occupyingUnit.active) {
			remove(character);
			occupyingUnit.calculateState();
				character = new JLabel(occupyingUnit.graphic);
				if(!down)
				{
					layout.putConstraint(SpringLayout.NORTH, character, (occupyingUnit.state)*2+1, SpringLayout.NORTH, this);
					setLayout(layout);
				}
				else
				{
					layout.putConstraint(SpringLayout.NORTH, character, 0, SpringLayout.NORTH, this);
					setLayout(layout);
				}
				add(character);
		}
	}

	public void place(Unit characterName) {
		character = new JLabel(characterName.graphic);
		add(character);
		characterName.setPlaced(true);
		characterName.occupiedSpace = this;
		occupyingUnit = characterName;
		occupied = true;
	}

	public void placeInactive(Unit characterName) {
		character = new JLabel(characterName.inactiveGraphic);
		add(character);
		characterName.setPlaced(true);
		characterName.occupiedSpace = this;
		occupyingUnit = characterName;
		occupied = true;
	}

	public void remove(Unit characterName) {
		remove(character);
		characterName.setPlaced(false);
		occupyingUnit = null;
		occupied = false;
		character = null;
		characterName.occupiedSpace = null;
	}

	public void setMoveRange(boolean input) {
		inMoveRange = input;
	}

	public void setMoving() {
		setBackground(new Color(0, 0, 255, 50));
		inMoveRange = true;
		setOpaque(true);
	}

	public void setNotMoving() {
		setOpaque(false);
		inMoveRange = false;
	}

	public void setAttacking() {
		setBackground(new Color(255, 0, 0, 50));
		canAttack = true;
		setOpaque(true);
	}

	public void setNotAttacking() {
		setOpaque(false);
		canAttack = false;
	}

	public void setSpawning() {
		setBackground(new Color(0, 255, 0, 80));
		canSpawn = true;
		setOpaque(true);
	}

	public void setNotSpawning() {
		setOpaque(false);
		canSpawn = false;
	}

	private void drawHealthBar(Graphics g) {
		g.drawRect(character.getX(), character.getY() + character.getHeight() + 7, character.getWidth(), 5);

		occupyingUnit.calculateState();

		if (occupyingUnit.state == 1) {
			g.setColor(new Color(106, 239, 97));
		} else if (occupyingUnit.state == 0) {
			g.setColor(new Color(228, 221, 37));
		} else {
			g.setColor(new Color(210, 56, 39));
		}

		double hpPercent = (double) occupyingUnit.enthusiasm / (double) occupyingUnit.maxEnthusiasm;
		int length = (int) (hpPercent * character.getWidth()) - 2;
		g.fillRect(character.getX() + 1, character.getY() + character.getHeight() + 8, length, 3);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (occupied) {
			drawHealthBar(g);
		}
	}
	
	private Image resizeImage(String imageName, int width, int height) throws Exception{
		int scaledWidth = width;
		int scaledHeight = height;
		//
		File inputFile = new File(imageName + ".png");
		BufferedImage inputImage = ImageIO.read(inputFile);
		
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
		
		Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        
        //ImageIO.write(outputImage, "png", new File(imageName + "Resize.png"));
        
		return outputImage;
	}

}
