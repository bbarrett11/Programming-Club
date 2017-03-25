package RPG;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class TitleScreen extends JFrame{
	
	public final String GAME_NAME = "GAME NAME: Subtitle";
	
	public ArrayList<Unit> roster = new ArrayList<Unit>();
	
	JPanel allEncompasingPanel;
	JLabel titleLabel;
	JPanel buttonPanel;
	JButton newGameButton;
	JButton continueGameButton;
	JButton settingsButton;
	JButton exitGameButton;
	
	public static void main(String[] args) throws FileNotFoundException{
		TitleScreen beginApp = new TitleScreen();
	}
	
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
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVerticalAlignment(JLabel.CENTER);
		titleLabel.setFont(new Font(titleLabel.getFont().getName(),Font.ITALIC,50));
		allEncompasingPanel.add(titleLabel, BorderLayout.NORTH);
		
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new MenuButtonListener());
		newGameButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		newGameButton.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		buttonPanel.add(newGameButton);
		
		continueGameButton = new JButton("Continue");
		continueGameButton.addActionListener(new MenuButtonListener());
		continueGameButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		continueGameButton.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		buttonPanel.add(continueGameButton);
		
		settingsButton = new JButton("Settings");
		settingsButton.addActionListener(new MenuButtonListener());
		settingsButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		settingsButton.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		buttonPanel.add(settingsButton);
		
		exitGameButton = new JButton("Exit");
		exitGameButton.addActionListener(new MenuButtonListener());
		exitGameButton.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		exitGameButton.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		buttonPanel.add(exitGameButton);
		
		//buttonPanel.setAlignmentX(JLabel.CENTER);
		//buttonPanel.setAlignmentY(JLabel.CENTER);
		
		//allEncompasingPanel.setAlignmentX(JLabel.CENTER);
		//allEncompasingPanel.setAlignmentY(JLabel.CENTER);
		
		allEncompasingPanel.add(buttonPanel, BorderLayout.CENTER);
	}
	
	final int PANELS_PER_SCROLL = 3;
	final int SCROLLBAR_SIZE = 2; 
	
	public void buildContinuePanel(ArrayList<File> saveFiles){
		saveSelectionPanels = new JPanel[saveFiles.size()];
		
		for(int n = 0;n<saveSelectionPanels.length;n++){
			saveSelectionPanels[n] = new JPanel();
			saveSelectionPanels[n].setLayout(new BoxLayout(saveSelectionPanels[n], BoxLayout.Y_AXIS));
			saveSelectionPanels[n].setBackground(Color.GRAY);
				//change paint component to whatever background we want
			saveSelectionPanels[n].add(new JLabel(saveFiles.get(n).getName()));
			try {
				File currentMissionReader = new File(saveFiles.get(n).getPath()+ "\\AvailableLevels.txt");
				Scanner fileReader = new Scanner(currentMissionReader);
				saveSelectionPanels[n].add(new JLabel("Level: " + fileReader.next()));
				fileReader.close();
				
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		continueGamePanel = new JPanel();
			//change paintComponent() to whatever we want background to be
		continueGamePanel.setLayout(new BorderLayout());
		savePanels = new JPanel();
		savePanels.setLayout(new BoxLayout(savePanels, BoxLayout.Y_AXIS));
		
		for(int n = 0;n<saveSelectionPanels.length && n<PANELS_PER_SCROLL;n++){
			savePanels.add(saveSelectionPanels[n]);
		}
		continueGamePanel.add(savePanels, BorderLayout.CENTER);
		
		if(saveSelectionPanels.length>PANELS_PER_SCROLL){
			saveSelectScrollbar = new Scrollbar(Scrollbar.VERTICAL, 0, SCROLLBAR_SIZE, 0, SCROLLBAR_SIZE
					+ saveSelectionPanels.length - PANELS_PER_SCROLL);
			
			saveSelectScrollbar.addAdjustmentListener(new ContinueScrollAdjustmentListener());
			continueGamePanel.add(saveSelectScrollbar, BorderLayout.EAST);
		}
		continueGamePanel.add(returnButton, BorderLayout.SOUTH);
		
	}
	
	JPanel[] saveSelectionPanels;
	JPanel continueGamePanel;
	JPanel savePanels;
	Scrollbar saveSelectScrollbar;

	
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
				createFileButton = new JButton("Create File");
				createFileButton.addActionListener(new MenuButtonListener());
				JLabel fileNamePrompt = new JLabel("File Name:");
				
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
				File savesFolder = new File("Saves");
				File[] listOfFiles = savesFolder.listFiles();
				ArrayList<File> saves = new ArrayList<File>();

			    for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			        System.out.println("File " + listOfFiles[i].getName());
			      } else if (listOfFiles[i].isDirectory()) {
			        System.out.println("Directory " + listOfFiles[i].getName());
			        saves.add(listOfFiles[i]);
			      }
			    }
			    
			    buildContinuePanel(saves);
			    setVisible(false);
				remove(allEncompasingPanel);
				add(continueGamePanel);
				setVisible(true);
			    
			    
			}
			else if(text.equals("Settings")){
				
			}
			else if(text.equals("Exit")){
				System.exit(0);
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
	
		
	}

	private class ContinueScrollAdjustmentListener implements AdjustmentListener{

		public void adjustmentValueChanged(AdjustmentEvent e) {
			savePanels.removeAll();
			for(int n = 0;n<PANELS_PER_SCROLL;n++){
				savePanels.add(saveSelectionPanels[e.getValue()+n]);
			}
			savePanels.updateUI();
		}
		
	}
	
	private void toMap(){
		// makes map panel, replaces it, etc.
		setVisible(false);
		getContentPane().removeAll();
		setVisible(true);
	}
	
}
