import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class Three extends JFrame{
	public static void main(String[] args) throws Exception{
		Three go = new Three();
	}
	
	final int LINES_PER_PAGE = 7;
	
	JTabbedPane thing;
	JPanel firstSet;
	JPanel secondSet;
	JPanel thirdSet;
	JPanel fourthSet;
	JPanel fifthSet;
	
	JPanel numbers;
	ArrayList<JLabel> text;
	
	public Three() throws Exception{
		thing = new JTabbedPane();
		
		firstSet = new JPanel();
		JLabel animals = new JLabel("<html>Cats<BR>Dogs<BR>Mice</html>");
		firstSet.add(animals);
		
		secondSet = new JPanel();
		JLabel name = new JLabel("Cindy Lou");
		secondSet.add(name);
		
		thirdSet = new JPanel();
		numbers = new JPanel();
		numbers.setLayout(new BoxLayout(numbers, BoxLayout.Y_AXIS));
		thirdSet.setLayout(new BorderLayout());
		text = new ArrayList<JLabel>();
		for(int n  = 0;n<30;n++){
			text.add(new JLabel("Line " + (n + 1)));
		}
		for(int n = 0;n<LINES_PER_PAGE;n++){
			numbers.add(text.get(n));
		}
		thirdSet.add(numbers);
		Scrollbar mrMoveUpAndDown = new Scrollbar(Scrollbar.VERTICAL, 0, 8, 0, 8 + text.size()- LINES_PER_PAGE);
		mrMoveUpAndDown.addAdjustmentListener(new FirstScrollListener());
		thirdSet.add(mrMoveUpAndDown, BorderLayout.EAST);
		
		fourthSet = new JPanel();
		ImageIcon animation = new ImageIcon("SenorSavesTheDayAttack.gif");
		JLabel attackAnimation = new JLabel(animation);
		fourthSet.add(attackAnimation);
		
		setSize(200,200);
		Image scaledImage = createResizedImage();
		
		fifthSet = new JPanel();
		
		JPanel try1 = new JPanel(){
			
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(new ImageIcon(scaledImage).getImage(), 5,5, null);
			
			}
			
		};
		
		JPanel try2 = new JPanel();
		
		JLabel resizedFromFile = new JLabel(new ImageIcon("TestResize.png"));
		
		try2.add(resizedFromFile);
		
		//fifthSet.add(try1);
		fifthSet.add(try2);
		
		thing.addTab("Animals", firstSet);
		thing.addTab("Name", secondSet);
		thing.addTab("Numbers", null, thirdSet, "Figure this out");
		thing.addTab("GIF", fourthSet);
		thing.addTab("Resize", fifthSet);
		
		add(thing);
		setSize(200,200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public Image createResizedImage() throws Exception{
		int scaledWidth = 20;
		int scaledHeight = 70;
		
		File inputFile = new File("Art\\Maps\\Test.png");
		BufferedImage inputImage = ImageIO.read(inputFile);
		
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
		
		Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        
        ImageIO.write(outputImage, "png", new File("TestResize.png"));
        
		return outputImage;
	}
	
	private class FirstScrollListener implements AdjustmentListener{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			numbers.removeAll();
			for(int n = 0;n<LINES_PER_PAGE;n++){
				numbers.add(text.get(e.getValue() + n));
			}
			numbers.updateUI();
		}
		
	}

}
