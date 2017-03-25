package RPG;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class InfoPanel extends JFrame{
	Unit describedUnit;
	JFrame buffInfo;
	public InfoPanel(Unit u)throws Exception
	{
		describedUnit = u;

		
		JTabbedPane tabbedPane = new JTabbedPane();
		/*
		JPanel bigPanel = new JPanel();
		bigPanel.setLayout(new GridLayout(2,2));
		bigPanel.setBorder(BorderFactory.createTitledBorder(describedUnit.name));
		bigPanel.add(Unit());
		bigPanel.add(Equipment());
		bigPanel.add(Stats());
		bigPanel.add(Effects());

		add(bigPanel);
		*/
		tabbedPane.addTab("Unit", null, Unit(),
                "Unit Tab");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		tabbedPane.addTab("Equipment", null, Equipment(),
                "Unit Tab");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		tabbedPane.addTab("Stats", null, Stats(),
                "Unit Tab");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		tabbedPane.addTab("Buffs", null, Effects(),
                "Unit Tab");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		add(tabbedPane);
		setUndecorated(true);
		setSize(250, 320);
		setLocation(getX() + describedUnit.occupiedSpace.getX() + 40,
				getY() + describedUnit.occupiedSpace.getY());
		setVisible(true);
	}
	
	private JPanel Equipment() throws Exception
	{
		JPanel equipmentPanel = new JPanel();
		equipmentPanel.setSize(250,320);
		equipmentPanel.setBorder(BorderFactory.createEtchedBorder());


		//Weapon
		JPanel weapon = new JPanel();
		weapon.setBorder(BorderFactory.createEtchedBorder());
		weapon.setSize(75, 75);
		weapon.add(new JLabel(new ImageIcon(resizeImage("Art\\equipment\\EmptyWeapon",75,75))));
		weapon.addMouseListener(mouseOverListener);
		weapon.setName(describedUnit.weapon.toString() + "");
		
		//Armor
		JPanel armor = new JPanel();
		armor.add(new JLabel(new ImageIcon(resizeImage("Art\\equipment\\EmptyArmor",75,75))));
		armor.setBorder(BorderFactory.createEtchedBorder());
		armor.setSize(75,75);
		armor.addMouseListener(mouseOverListener);
		armor.setName(describedUnit.armorSet.toString() + "");

		//Accessory
		JPanel accessory = new JPanel();
		accessory.setBorder(BorderFactory.createEtchedBorder());
		accessory.setSize(75,75);
		accessory.add(new JLabel(new ImageIcon(resizeImage("Art\\equipment\\EmptyAccessory",75,75))));
		accessory.addMouseListener(mouseOverListener);
		accessory.setName(describedUnit.weapon.toString() + "");

		SpringLayout s = new SpringLayout();
		
		equipmentPanel.add(weapon);
		equipmentPanel.add(armor);
		equipmentPanel.add(accessory);
		
		s.putConstraint(SpringLayout.NORTH, weapon, 10, SpringLayout.NORTH,equipmentPanel);
		s.putConstraint(SpringLayout.NORTH, armor, 5, SpringLayout.SOUTH,weapon);
		s.putConstraint(SpringLayout.NORTH, accessory, 5, SpringLayout.SOUTH,armor);
		s.putConstraint(SpringLayout.SOUTH, accessory, 25, SpringLayout.SOUTH,equipmentPanel);
		
		equipmentPanel.setLayout(s);
		
		return equipmentPanel;
	}
	
	/**
	 * returns panel w/ character's picture on it
	 * @return JPanel
	 */
	private JPanel Unit()
	{
		JPanel unitPanel = new JPanel();
		//unitPanel.setBorder(BorderFactory.createTitledBorder(describedUnit.name));

		JLabel portrait = new JLabel(describedUnit.portrait);
		unitPanel.add(portrait);
		
		unitPanel.setBorder(BorderFactory.createEtchedBorder());
		return unitPanel;

	}
	
	/**
	 * Creates a panel w/ character stats on it as labels
	 * @return JPanel
	 */
	private JPanel Stats() 
	{
		JPanel statPanel = new JPanel();
		statPanel.setLayout(new GridLayout(0,1));
		statPanel.setBorder(BorderFactory.createEtchedBorder());

		statPanel.add(new JLabel("Level: "+describedUnit.level));
		statPanel.add(new JLabel("Max Enthusiasm: "+describedUnit.maxEnthusiasm));
		statPanel.add(new JLabel("Enthusiasm: "+describedUnit.enthusiasm));
		statPanel.add(new JLabel("State: "+describedUnit.state));
		statPanel.add(new JLabel("Toughness: "+describedUnit.toughness));
		statPanel.add(new JLabel("Max Focus: "+describedUnit.maxFocus));
		statPanel.add(new JLabel("Focus: "+describedUnit.focus));
		statPanel.add(new JLabel("Dilligence: "+describedUnit.diligence));
		statPanel.add(new JLabel("Strength: "+describedUnit.strength));
		statPanel.add(new JLabel("Speed: "+describedUnit.speed));
		statPanel.add(new JLabel("Avoidance: "+describedUnit.avoidance));
		statPanel.add(new JLabel("Bleed Threshold: "+describedUnit.bleedThreshold));
		statPanel.add(new JLabel("Glory: "+describedUnit.glory));
		return statPanel;
	}
	
	/**
	 * Gives each buff a panel whose icon is the buff picture and whose name is a description of the buff.
	 * The name is displayed when the panel is moused over.
	 * @return JPanel
	 * @throws Exception IO things when finding picture of buffs
	 */
	private JPanel Effects() throws Exception
	{
		JPanel Effects = new JPanel();
		Effects.setLayout(new BorderLayout());
		Effects.setBorder(BorderFactory.createEtchedBorder());
		Effects.add(new JLabel("Buffs:"),BorderLayout.NORTH);
		JPanel buffPanel = new JPanel();
		buffPanel.setLayout(new FlowLayout());
		for(Buff b : describedUnit.buffList)
		{
			JPanel tempPanel = new JPanel();
			JLabel tempLabel = new JLabel();
			tempPanel.setName(b+"");
			String color = "";
			switch(b.name)
			{
			case "Bleed":
				color = "Red";
				break;
			case "Disarm":
				color = "Black";
				break;
			case "Stun":
				color = "Green";
				break;	
			default:
				break;
			}
			tempLabel.setIcon(new ImageIcon(resizeImage("Art\\buff\\"+color+"Poison",30,40)));
			tempPanel.add(tempLabel);
			tempPanel.addMouseListener(mouseOverListener);
			buffPanel.add(tempPanel);
		}
		Effects.add(buffPanel,BorderLayout.CENTER);
		return Effects;
	}
	
	/**
	 * Method that takes an input image and returns a resized one.
	 * @param imageName Image to resize
	 * @param width Width to resize image to
	 * @param height Height to resize image to
	 * @return Changed image
	 * @throws Exception IO realted to acessing picture
	 */
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

	MouseListener mouseOverListener = new MouseListener()
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			buffInfo = new JFrame();
			buffInfo.setSize(200, 17*(((JPanel)e.getSource()).getName().split("\n").length)+10);
			buffInfo.setUndecorated(true);
			JPanel buffInfoPanel = new JPanel();
			String fin = "<html>";
			for(String s : ((JPanel)e.getSource()).getName().split("\n"))
			{
				fin+=s+"</br><br>";
			}
			buffInfoPanel.add(new JLabel(fin + "</html>"));
			
			buffInfo.add(buffInfoPanel);
										
			buffInfo.setLocation(getX()+((JPanel)e.getSource()).getX() + ((JPanel)e.getSource()).getWidth() ,
					getY()+((JPanel)e.getSource()).getY());
			buffInfo.setVisible(true);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			buffInfo.dispose();
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

	};
}
