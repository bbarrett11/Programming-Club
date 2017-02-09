package RPG;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreen extends JFrame{
	
	public final String GAME_NAME = "GAME NAME: Subtitle";
	
	JPanel allEncompasingPanel;
	JLabel titleLabel;
	JPanel buttonPanel;
	JButton newGameButton;
	JButton continueGameButton;
	JButton settingsButton;
	JButton exitGameButton;
	
	public TitleScreen(){
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buildMenu();
		
		add(allEncompasingPanel);
		setVisible(true);

	}
	
	private void buildMenu(){
		allEncompasingPanel = new JPanel(){
			//have paintComponent use whatever image we have for background 
		};
		allEncompasingPanel.setLayout(new BorderLayout());
		titleLabel = new JLabel(GAME_NAME);
		allEncompasingPanel.add(titleLabel, BorderLayout.NORTH);
		
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new MenuButtonListener());
		buttonPanel.add(newGameButton);
		
		continueGameButton = new JButton("Continue");
		continueGameButton.addActionListener(new MenuButtonListener());
		buttonPanel.add(continueGameButton);
		
		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(new MenuButtonListener());
		buttonPanel.add(settingsButton);
		
		exitGameButton = new JButton("Exit");
		exitGameButton.addActionListener(new MenuButtonListener());
		buttonPanel.add(exitGameButton);
		
		allEncompasingPanel.add(buttonPanel, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		TitleScreen beginApp = new TitleScreen();
	}
	
	
	JPanel createFilePanel;
	JButton returnButton;
	JButton createFileButton;
	JTextField fileNameInput;
	
	private class MenuButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String text = ((JButton) e.getSource()).getText();
			System.out.println(text);
			returnButton = new JButton("Return");
			returnButton.addActionListener(new MenuButtonListener());
			
			if(text.equals("New Game")){
				fileNameInput = new JTextField(15);
				createFilePanel = new JPanel(){
					//paintComponent set to whatever background we want
				};
				createFilePanel.setLayout(new BoxLayout(createFilePanel, BoxLayout.Y_AXIS));
				JLabel fileNamePrompt = new JLabel("File Name:");
				createFileButton = new JButton("Create File");
				createFileButton.addActionListener(new MenuButtonListener());
				
				createFilePanel.add(fileNamePrompt);
				createFilePanel.add(fileNameInput);
				createFilePanel.add(createFileButton);
				createFilePanel.add(returnButton);
				
				setVisible(false);
				remove(allEncompasingPanel);
				add(createFilePanel);
				setVisible(true);
			}
			else if(text.equals("Continue")){
				
			}
			else if(text.equals("Settings")){
				
			}
			else if(text.equals("Exit")){
				
			}
			else if(text.equals("Return")){
				setVisible(false);
				getContentPane().removeAll();
				buildMenu();
				add(allEncompasingPanel);
				setVisible(true);
			}
			
			else if(text.equals("Create File")){
				String txt = fileNameInput.getText();
				System.out.println("Start" + txt);
				
				File f = new File("Saves\\" + fileNameInput.getText());
				System.out.println("1" + fileNameInput.getText());

				f.mkdirs();
				System.out.println("2");

				f = new File("Saves\\" + fileNameInput.getText() + "\\Roster");
				System.out.println("3");

				f.mkdirs();
				System.out.println("4");


				PrintWriter pw;
				try {
					pw = new PrintWriter("Saves\\" + fileNameInput.getText() + "\\AvailableLevels.txt");
					pw.println("Test");
					pw.println("Tutorial");
					pw.close();
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				System.out.println("5");

				toMap();
				System.out.println("6");

			}
		}
	
		private class Test implements MouseListener{

			@Override
			public void mouseClicked(MouseEvent e) {
				JTextField temp = (JTextField) e.getSource();
				System.out.println(temp.getText());
				temp.setText("Changed");
				
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
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
	}
	
	private void toMap(){
		// makes map panel, replaces it, etc.
		getContentPane().removeAll();
	}
	
}
