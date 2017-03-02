package RPG;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		setLayout(new GridLayout(1,2));
		describedUnit = u;
		add(Stats());
		add(Effects());

		setUndecorated(true);
		setSize(310, 270); // y value needs to depend on
												// number of buffs
		setLocation(getX() + describedUnit.occupiedSpace.getX() + 40,
				getY() + describedUnit.occupiedSpace.getY());
		setVisible(true);

	}
	private JPanel Stats()
	{
		JPanel statPanel = new JPanel();
		statPanel.setLayout(new GridLayout(0,1));

		statPanel.add(new JLabel("Level: "+describedUnit.level));
		statPanel.add(new JLabel("Max Enthusiasm: "+describedUnit.maxEnthusiasm));
		statPanel.add(new JLabel("Enthusiasm: "+describedUnit.enthusiasm));
		statPanel.add(new JLabel("State: "+describedUnit.state));
		statPanel.add(new JLabel("Toughness: "+describedUnit.toughness));
		statPanel.add(new JLabel("Max Focus: "+describedUnit.maxFocus));
		statPanel.add(new JLabel("Focus: "+describedUnit.focus));
		statPanel.add(new JLabel("Dilligence: "+describedUnit.dilligence));
		statPanel.add(new JLabel("Strength: "+describedUnit.strength));
		statPanel.add(new JLabel("Speed: "+describedUnit.speed));
		statPanel.add(new JLabel("Avoidance: "+describedUnit.avoidance));
		statPanel.add(new JLabel("Bleed Threshold: "+describedUnit.bleedThreshold));
		statPanel.add(new JLabel("Glory: "+describedUnit.glory));
		return statPanel;
	}
	
	private JPanel Effects() throws Exception
	{
		JPanel Effects = new JPanel();
		Effects.setLayout(new GridLayout(0,1));
		Effects.add(new JLabel("Buffs:"));
		for(Buff b : describedUnit.buffList)
		{
			JPanel tempPanel = new JPanel();
			JLabel tempLabel = new JLabel();
			tempPanel.setName(b+"");
			tempLabel.setIcon(new ImageIcon(resizeImage("Art\\BlackPoison",30,40)));
			tempPanel.add(tempLabel);
			tempPanel.addMouseListener(new MouseListener()
					{

						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							buffInfo = new JFrame();
							buffInfo.setSize(200, 50);
							buffInfo.setUndecorated(true);
							JPanel buffInfoPanel = new JPanel();
							buffInfoPanel.add(new JLabel(((JPanel)e.getSource()).getName()));
							buffInfo.add(buffInfoPanel);
														
							buffInfo.setLocation(e.getX() ,
									e.getY());
							buffInfo.setVisible(true);
							
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							buffInfo.dispose();
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
				
					});
			Effects.add(tempPanel);
		}
		return Effects;
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
