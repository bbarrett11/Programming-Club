package RPG;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreen extends JFrame{
	
	public TitleScreen(){
		setSize(500,500);
		setVisible(true);
		JPanel allEncompasingPanel = new JPanel(){
			//have paintComponent use whatever image we have for background
		};
		allEncompasingPanel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel();
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		TitleScreen beginApp = new TitleScreen();
	}
	/*
	 * File f = new File("HEYO\\whatsGud");
		f.mkdirs();
		PrintWriter pw = new PrintWriter("HEYO\\whatsGud\\live.txt");
		pw.print("Still here!");
		pw.close();
		f.mkdirs();
	 */
	
}
