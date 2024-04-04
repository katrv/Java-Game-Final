// Katrina Ravichandran
// Game.java  

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Image;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Game
{
	public Game()
	{
	}

	public static void main (String[]args)
	{
		Game game = new Game();
		game.run();
	}
	
	//Creates JFrame adds the GameHolder Panel to the JFrame.
	public void run()
	{
		JFrame frame = new JFrame("Probability Penguin");
		frame.setSize( 800, 700);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocation(50,0);
		frame.setResizable(false);
		GameHolder gh = new GameHolder();
		frame.getContentPane().add( gh );
		frame.setVisible(true);
	}
}

// creates a card layout for the main page to go to
// the other panels.
class GameHolder extends JPanel
{
	private CardLayout cards; // card layout passed to overloaded constructors
	private GamePanel gp; // used to call method in GamePanel 
	
	//creates a CardLayout, and adds all JPanels to the CardLayout. 
	public GameHolder()
	{
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		cards = new CardLayout();
		setLayout(cards);

		LogInPanel logIn = new LogInPanel(this);
		StartPanel start = new StartPanel(this);
		InstructionsPanel instructions = new InstructionsPanel(this);
		LearnProbabilityPanel learnProbability = new LearnProbabilityPanel(this);
		ScoresPanel scores = new ScoresPanel(this);
		SettingsPanel settings = new SettingsPanel(this, instructions, learnProbability, scores);
		NorthPanel np = learnProbability.returnPanel();
		gp = new GamePanel(this, settings, np, logIn);

		add(logIn, "LogInPanel");
		add(start, "StartPanel");
		add(settings, "SettingsPanel");
		add(instructions, "InstructionsPanel");
		add(learnProbability, "LearnProbabilityPanel");
		add(scores, "ScoresPanel");
		add(gp, "GamePanel");
		

	}
	
	//used to get the CardLayout from other classes. 
	public CardLayout getCards()
	{
		return cards;
	}
	
	//calls method in GamePanel to get instance of CenterGamePanel. 
	public CenterGamePanel getCGP()
	{
		CenterGamePanel cgp = gp.getCGP();
		return cgp;
	}
}



class LogInPanel extends JPanel
{
	private GameHolder gameHolder; // used for CardLayout
	private String instruction; //used for JLabel
	private String userName; //username of the account that is used to play the game
	private String backgroundName; //name for background Image
	private Image background; //image used in the background of the LogInPanel
	
	// Adds panels and button and sets the
	// layout as null for the first page.
	public LogInPanel(GameHolder ghIn)
	{
		backgroundName = "background.jpeg";
		FileIO fileIO = new FileIO();
		background = fileIO.loadImages(backgroundName);
		setLayout(null);
		userName = "";
		gameHolder = ghIn;
		instruction = new String("Welcome!");
		SignInPanel sip = new SignInPanel(this, gameHolder);
		sip.setBounds(200,200,400,150);
		add(sip);
		SignUpPanel sup = new SignUpPanel(this,gameHolder);
		sup.setBounds(200,400,400,150);
		add(sup);
	}
	
	// calls method. //
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, 800, 700, this);
		writeText(g);
	}
	
	// Draws the title and instruction JLabel. //
	public void writeText(Graphics g)
	{
		Font font = new Font("title",Font.BOLD,40);
		g.setFont(font);
		g.drawString("Probability Penguin",200,70);
		font = new Font("title",Font.BOLD,25);
		g.setFont(font);
		g.drawString(instruction,200,175);
	}
	
	// changes text in JLabel. 
	public void changeInstruction(String instructionIn)
	{
		instruction = instructionIn;
		repaint();
	}
	
	//stores username of the account that is used to play the game.
	public void setName(String nameIn)
	{
		userName = nameIn;
	}
	
	//returns the username of the account that is used to play the game.
	public String returnName()
	{
		return userName;
	}
}
// GridLayout for the sign in panel added to the LogInPanel,
// and ActionListeners for JTextFields. 
class SignInPanel extends JPanel
{
	private JTextField signInName; //For player to enter their username
	private JTextField signInPassword; //For player to enter their password
	private boolean match;//Shows if the username entered by the player matches any usernames in the file
	private boolean signInNameEntered;//Shows if a name was entered in signInName
	private String nameEntered;//The username entered by the player
	private String password;//The password for the username entered
	private LogInPanel lip;//Instance of LogInPanel, used to call method
	private GameHolder gameHolder;// contains cards in CardLayout
	private int coinAmount;
	/*adds label and two JTextFields to a GridLayout. 
	ActionListeners are added to JTextFields.*/
	public SignInPanel(LogInPanel lipIn, GameHolder ghIn)
	{
		coinAmount = 0;
		gameHolder = ghIn;
		lip = lipIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new GridLayout(3,1));
		match = false;
		nameEntered = new String("");
		password = new String("");
		JLabel signIn = new JLabel("Sign in:");
		add(signIn);
		signInName = new JTextField("Type in username and press enter.");
		add(signInName);
		SignInNameHandler sinh = new SignInNameHandler();
		signInName.addActionListener(sinh);
		signInPassword = new JTextField("Type in passsword and press enter.");
		add(signInPassword);
		SignInPasswordHandler siph = new SignInPasswordHandler();
		signInPassword.addActionListener(siph);
	}
	
class SignInNameHandler implements ActionListener
	{
		/*checks if the username entered matches any usernames in the file
		If it does, then the password for that username is saved. If it
		doesn't, then a method is called to change the JLabel in LogInPanel.*/
		public void actionPerformed(ActionEvent evt)
		{
			nameEntered = signInName.getText();
			int count = 4;
			String readFileName = new String("accountInfo.txt");
			FileIO fileIO = new FileIO();
			Scanner readIn = null;
			readIn = fileIO.readFile(readFileName);
			while(readIn.hasNext())
			{
				if(count%4 == 0)
				{
					String line = readIn.nextLine();
					if(line.equals(nameEntered))
					{
						match = true;
						password = readIn.nextLine();
						readIn.nextLine();
						String coinLine = readIn.nextLine();
						int indexOfColon = coinLine.indexOf(":");
						String temp = coinLine.substring(indexOfColon + 2);
						coinAmount = Integer.parseInt(temp);
						count+=3;
					}
				}
				else
				{
					readIn.nextLine();
				}
				count++;
			}
			if(match == false)
				lip.changeInstruction("Username doesn't exist");
			else
				lip.changeInstruction("Welcome " + nameEntered + "!");
		}
	}
	
	class SignInPasswordHandler implements ActionListener
	{
		/*if the password entered equals the password for the username
		entered, then the next panel is shown. If it doesn't, then a 
		method is called to change the JLabel in LogInPanel. */
		public void actionPerformed(ActionEvent evt)
		{
			if(match == true)
			{
				String passwordEntered = signInPassword.getText();
				if(password.equals(passwordEntered))
				{
					CardLayout cards = gameHolder.getCards();
					cards.show(gameHolder, "StartPanel");
					lip.setName(nameEntered);
					CenterGamePanel cgp = gameHolder.getCGP();
					cgp.setAccountInfo(nameEntered,password,coinAmount);
				}
				else
					lip.changeInstruction("Password is incorrect");
			}
		}
	}
}


class SignUpPanel extends JPanel
{
	private JTextField signUpName;//For player to enter the username they want
	private JTextField signUpPassword;//For player to enter the passwod they want
	private boolean match;//Shows if the username the player entered matches any usernames in the file
	private boolean signUpNameEntered;//Shows if a username was entered in signUpName
	private String nameEntered;//The username entered in signUpName
	private int coinAmount;// Number of coins the user collected
	private GameHolder gameHolder;//Contains cards in CardLayout
	private LogInPanel lip;//Instance of LogInPanel, used to call method
	/*adds label and twoJTextFields to a GridLayout. 
	ActionListeners are added to JTextFields.*/
	public SignUpPanel(LogInPanel lipIn, GameHolder ghIn)
	{
		lip = lipIn;
		coinAmount = 0;
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new GridLayout(3,1));
		match = false;
		signUpNameEntered = false;
		nameEntered = new String("");
		JLabel signUp = new JLabel("Sign up:");
		add(signUp);
		signUpName = new JTextField("Type in username and press enter.");
		add(signUpName);
		SignUpNameHandler sunh = new SignUpNameHandler();
		signUpName.addActionListener(sunh);
	    signUpPassword = new JTextField("Type in password and press enter.");
		add(signUpPassword);
		SignUpPasswordHandler suph = new SignUpPasswordHandler();
		signUpPassword.addActionListener(suph);
	}
	
	class SignUpNameHandler implements ActionListener
	{
		/*if the username entered matches any usernames in the file,
		 * then a method is called to change the JLabel in LogInPanel.*/
		public void actionPerformed(ActionEvent evt)
		{
			int count = 4;
			nameEntered = signUpName.getText();
			String readFileName = new String("accountInfo.txt");
			FileIO fileIO = new FileIO();
			Scanner readName = fileIO.readFile(readFileName);
			
			while(readName.hasNext() && match == false)
			{
				if(count%4 == 0)
				{
					String line = readName.nextLine();
					if(line.equals(nameEntered))
					{
						match = true;
						lip.changeInstruction("Username already exists");
					}
				}
				else
				{
					readName.nextLine();
				}
				count++;
			}
			signUpNameEntered = true;
			if(match == false)
				lip.changeInstruction("Welcome " + nameEntered + "!");
		}
	}
	class SignUpPasswordHandler implements ActionListener
	{
		/*if a username has been entered in signUpName that doesn't match
		 * any usernames in the file, then the password that was entered
		 * in SignUpPassword and the username are printed to the file. */
		public void actionPerformed(ActionEvent evt)
		{
			if(match == false && signUpNameEntered == true)
			{
				String password = signUpPassword.getText();
				FileIO fileIO = new FileIO();
				String writeFileName = new String("accountInfo.txt");
				PrintWriter write = fileIO.writeFile(writeFileName);

				write.println(nameEntered);
				write.println(password);
				write.println("Level: 1");
				write.println("Coins: 0");
				
				write.close();
				CenterGamePanel cgp = gameHolder.getCGP();
				cgp.setAccountInfo(nameEntered,password,0);
				CardLayout cards = gameHolder.getCards();
				cards.show(gameHolder, "StartPanel");
				lip.setName(nameEntered);
				
			}
		}
	}
}

class StartPanel extends JPanel
{
	private GameHolder gameHolder; // contains cards in CardLayout
	private String backgroundName; // name of background image
	private Image background; // image in the background of the panel
	//adds ButtonPanel to StartPanel and adds the background image //
	public StartPanel(GameHolder ghIn)
	{
		//Coded by Katrina
		backgroundName = "background.jpeg";
		FileIO fileIO = new FileIO();
		background = fileIO.loadImages(backgroundName);
		//Coded by Prita
		gameHolder = ghIn;
		setLayout(new FlowLayout(FlowLayout.CENTER,20,250));
		ButtonPanel bp = new ButtonPanel(gameHolder);
		add(bp);
	}
	
	//calls method. //
	public void paintComponent(Graphics g)//P
	{
		super.paintComponent(g);
		drawText(g);
	}
	
	//draws string. //
	public void drawText(Graphics g)//P
	{
		Font bold = new Font("title",Font.BOLD,40);
		g.setFont(bold);
		g.drawImage(background, 0, 0, 800, 700, this);
		g.setColor(Color.BLACK);
		g.drawString("Probability Penguin",200,70);
	}
}


class ButtonPanel extends JPanel
{
	private GameHolder gameHolder; // contains cards in CardLayout
	
	//Adds buttons to panel and adds ActionListeners to buttons. //
	public ButtonPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new GridLayout(6,1));
		JButton playButton = new JButton("Play");
		add(playButton);
		StartPageButtonHandler spbh = new StartPageButtonHandler();
		playButton.addActionListener(spbh);
		JButton howToPlayButton = new JButton("How To Play");
		add(howToPlayButton);
		howToPlayButton.addActionListener(spbh);
		JButton learnProbabilityButton = new JButton("Learn Probability");
		add(learnProbabilityButton);
		learnProbabilityButton.addActionListener(spbh);
		JButton highScoresButton = new JButton("High Scores");
		add(highScoresButton);
		highScoresButton.addActionListener(spbh);
		JButton settingsButton = new JButton("Settings");
		add(settingsButton);
		settingsButton.addActionListener(spbh);
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(spbh);
		add(exitButton);
	}
	
	class StartPageButtonHandler implements ActionListener
	{
		//if button is pressed, corresponding panel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			String command = evt.getActionCommand();
			if(command.equals("Play"))
			{
				cards.show(gameHolder, "GamePanel");
			}
			else if(command.equals("How To Play"))
				cards.show(gameHolder, "InstructionsPanel");
			else if(command.equals("Learn Probability"))
				cards.show(gameHolder, "LearnProbabilityPanel");
			else if(command.equals("High Scores"))
				cards.show(gameHolder, "ScoresPanel");
			else if(command.equals("Settings"))
				cards.show(gameHolder, "SettingsPanel");
			else if (command.equals("Exit"))
				System.exit(1);
		}
	}
}



class LearnProbabilityPanel extends JPanel
{
	private GameHolder gameHolder; //Contains cards in CardLayout
	private NorthPanel np; //Panel added to the north of LearnProbabilityPanel
	private int fontSize; //Font size of text in direction JTextArea
	private JTextArea directions; //JTextArea that contains information on probability
	//adds JPanel and JTextArea
	private String line; // Text that goes inside direction JTextArea
	public LearnProbabilityPanel(GameHolder ghIn)
	{
		fontSize = 15;
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new BorderLayout());
		np = new NorthPanel(gameHolder);
		add(np,BorderLayout.NORTH);
		directions = new JTextArea(" ",50,50);
		directions.setLineWrap(true);
		directions.setWrapStyleWord(true);
		directions.setEditable(false);
		directions.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane jsp = new JScrollPane(directions);
		add(jsp,BorderLayout.CENTER);
		
		FileIO fileIO = new FileIO();
		String fileName = "learnProbability.txt";
		Scanner readIn = fileIO.readFile(fileName);
		line = "";
		while(readIn.hasNext())
		{
			line += readIn.nextLine() + "\n";
		}
	}
	
//returns NorthPanel
	public NorthPanel returnPanel()
	{
		return np;
	}
	
	//sets the text and font of the directions JTextField
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Font font = new Font("SansSerif", Font.PLAIN, fontSize);
		directions.setFont(font);
		directions.setText(line);
	}
	
	//used to get the font size for the text in the direction JTextField
	public void getFontSize(int sizeIn)
	{
		fontSize = sizeIn;
		this.repaint();
	}
}

// Layout for this panel coded by Prita
class NorthPanel extends JPanel
{
	private GameHolder gameHolder; //contains cards in CardLayout
	private boolean previous; //used to check if StartPanel was the previous panel shown
	
	//adds JButton, JLabel, and ActionListener. //
	public NorthPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		previous = false;
		JButton startPage = new JButton("Go back");
		PreviousPage pp = new PreviousPage();
		startPage.addActionListener(pp);
		add(startPage);
		JLabel name = new JLabel("Learn Probability");
		add(name);
	}
	
	class PreviousPage implements ActionListener
	{
		/*if button is pressed, then the previous panel
		 *is shown using the CardLayout.*/
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			if (evt.getActionCommand().equals("Go back"))
			{
				if(previous == false)
				{
					cards.show(gameHolder, "StartPanel");
				}
				else
				{
					cards.show(gameHolder, "GamePanel");
				}
			}
				
		}
	}
	
	//sets previous to true if StartPanel was the previous panel shown
	public void setPreviousPanel(boolean previousIn)
	{
		previous = previousIn;
	}
}
	
class SettingsPanel extends JPanel
{
	private GameHolder gameHolder; //Contains cards in CardLayout. //
	private String penguinColor; //shows what color the penguin currently is
	private InstructionsPanel ip; //Used to call a method to change the font size in InstructionsPanel
	private LearnProbabilityPanel lpp; //Used to call a method to change the font size in LearnProbabilityPanel
	private ScoresPanel sp;//Used to call a method to change the font size in ScoresPanel
	private SettingsCenterPanel scp; // used to get Mode from ModeButtonPanel
	private ModeButtonPanel mbp; // used to be returned to GamePanel for the mode
	public SettingsPanel(GameHolder ghIn, InstructionsPanel ipIn, LearnProbabilityPanel lppIn, ScoresPanel spIn)
	{
		penguinColor = "black";
		gameHolder = ghIn;
		ip = ipIn;
		lpp = lppIn;
		sp = spIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		//Coded by Prita
		setLayout(new BorderLayout());
		HomeButtonPanel hbp = new HomeButtonPanel(gameHolder);
		add(hbp, BorderLayout.NORTH);
		scp = new SettingsCenterPanel(this, ip, lpp, sp);
		add(scp, BorderLayout.CENTER);
		
		mbp = scp.returnMBP();
		
	}
	
	//sets penguinColor string to show the color of the penguin everytime the color changes
	public void setPenguinColor(String colorIn)
	{
		penguinColor = colorIn;
	}
	
	//returns penguinColor string
	public String getPenguinColor()
	{
		return penguinColor;
	}
	
	//returns ModeButtonPanel instance used in ButtonsAreaPanel
	public ModeButtonPanel returnMBP()
	{
		return mbp;
	}
}

class HomeButtonPanel extends JPanel
{	
	private GameHolder gameHolder;//Contains cards in CardLayout
	//adds JButton, JLabel, and ActionListener. 
	public HomeButtonPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		JButton startPage = new JButton("Home");
		StartPageButtonHandler spbh = new StartPageButtonHandler();
		startPage.addActionListener(spbh);
		add(startPage);
		JLabel name = new JLabel("Settings");
		add(name);
	}
	
	class StartPageButtonHandler implements ActionListener
	{
		//if button is pressed, StartPanel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			cards.show(gameHolder, "StartPanel");
		}
	}
}
	
class SettingsCenterPanel extends JPanel
{
	private SettingsPanel sp;//Used to call method to change penguinColor
	private InstructionsPanel ip; //Used to call a method to change the font size in InstructionsPanel
	private LearnProbabilityPanel lpp; //Used to call a method to change the font size in LearnProbabilityPanel
	private ScoresPanel scores;//Used to call a method to change the font size in ScoresPanel
	private ModePanel mp; // Used to be returned to SettingsPanel
	private Image background; // Used for background image
	private String backgroundName; // Used to load background image
	public SettingsCenterPanel(SettingsPanel spIn, InstructionsPanel ipIn, LearnProbabilityPanel lppIn, ScoresPanel scoresIn)
	{
		sp = spIn;
		ip = ipIn;
		lpp = lppIn;
		scores = scoresIn;
		setLayout(new FlowLayout(FlowLayout.CENTER,350,50));
		FontSizePanel fsp = new FontSizePanel(ip, lpp, scores);
		add(fsp);
		PenguinColorPanel pcp = new PenguinColorPanel(sp);
		add(pcp);
		
		// Coded by Katrina
		mp = new ModePanel();
		add(mp);
		FileIO fileIO = new FileIO();
		backgroundName = "background.jpeg";
		background = fileIO.loadImages(backgroundName);
	}
	
	//This method returns ModeButtonPanel instance, used in SettingsPanel
	public ModeButtonPanel returnMBP()
	{
		ModeButtonPanel mbp = mp.returnMBP();
		return mbp;
	}
	
	// paintComponent draws the background image
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, 800, 700, this);
	}
}

class FontSizePanel extends JPanel
{
	private JSlider fontSlider; //Slider used to change font size
	private InstructionsPanel ip; //Used to call a method to change the font size in InstructionsPanel
	private LearnProbabilityPanel lpp; //Used to call a method to change the font size in LearnProbabilityPanel
	private ScoresPanel sp;//Used to call a method to change the font size in ScoresPanel
	// adds JLabel, JSlider, and ChangeListener //
	public FontSizePanel(InstructionsPanel ipIn, LearnProbabilityPanel lppIn, ScoresPanel spIn)
	{
		ip = ipIn;
		lpp = lppIn;
		sp = spIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new GridLayout(2,1));
		JLabel fontSize = new JLabel("Font Size");
		add(fontSize);
		fontSlider = new JSlider(JSlider.HORIZONTAL,15,50,25);
		FontSizeHandler fsh = new FontSizeHandler();
		fontSlider.addChangeListener(fsh);
		add(fontSlider);
	}
	
	class FontSizeHandler implements ChangeListener
	{
		//changes font size in different panels based on the value of the slider
		public void stateChanged(ChangeEvent evt)
		{
			int sliderValue = fontSlider.getValue();
			ip.getFontSize(sliderValue);
			lpp.getFontSize(sliderValue);
			sp.getFontSize(sliderValue);
		}
	}
}

class PenguinColorPanel extends JPanel
{
	private Color bgColor; // Used for the background color of the panel
	private SettingsPanel sp; // Used to send instance to ColorBUttonpanel
	//adds JLabel and JPanl
	public PenguinColorPanel(SettingsPanel spIn)
	{
		sp = spIn;
		setLayout(new GridLayout(2,1));
		bgColor = Color.WHITE;
		setBackground(bgColor);
		JLabel penguinColor = new JLabel("Penguin Color");
		add(penguinColor);
		ColorButtonPanel cbp = new ColorButtonPanel(this, sp);
		add(cbp);
	}
	
	// paints background color
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setBackground(bgColor);
	}
	
	// sets the background color when called from other classes and repaints 
	public void setBGColor(Color colorIn)
	{
		bgColor = colorIn;
		this.repaint();
	}
}

class ColorButtonPanel extends JPanel
{
	private PenguinColorPanel pcp; // Used to set bg color after button clicked
	private SettingsPanel sp; // Used to set the penguin color to be used in
								// other classes
	//adds JButtons and ActionListener
	public ColorButtonPanel(PenguinColorPanel pcpIn, SettingsPanel spIn)
	{
		pcp = pcpIn;
		sp = spIn;
		ColorButtonHandler cbh = new ColorButtonHandler();
		JButton blue = new JButton("Blue");
		add(blue);
		blue.addActionListener(cbh);
		JButton cyan = new JButton("Cyan");
		add(cyan);
		cyan.addActionListener(cbh);
		JButton black = new JButton("Black");
		add(black);
		black.addActionListener(cbh);
		JButton pink = new JButton("Pink");
		add(pink);
		pink.addActionListener(cbh);
	}
			
	// button handler classes that set the variables of the bg color and 
	// penguin color in other classes and repaints
	class ColorButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			Color bgColor = Color.BLACK;
			String penguinColor = "black";
			String command = evt.getActionCommand();
			if(command.equals("Blue"))
			{
				bgColor = Color.BLUE;
				sp.setPenguinColor("blue");
			}
			else if(command.equals("Cyan"))
			{
				bgColor = Color.CYAN;
				sp.setPenguinColor("cyan");
			}
			else if(command.equals("Black"))
			{
				bgColor = Color.BLACK;
				sp.setPenguinColor("black");
			}
			else if(command.equals("Pink"))
			{
				bgColor = Color.PINK;
				sp.setPenguinColor("pink");
			}
			
			pcp.setBGColor(bgColor);
			pcp.repaint();
		}
	}
}

//mode choosing panel
class ModePanel extends JPanel
{
	private ModeButtonPanel mbp; // used to return instance to other classes
	public ModePanel()
	{
		setLayout(new GridLayout(2,1));
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		JLabel mode = new JLabel("Mode");
		add(mode);
		mbp = new ModeButtonPanel();
		add(mbp);
	}
	
	// returns ModeButtonPanel instance for SettingsCenterPanel
	public ModeButtonPanel returnMBP()
	{
		return mbp;
	}
}

// mode choosing buttons
class ModeButtonPanel extends JPanel
{
	private String mode; // used to return the mode user chose to other classes
	public ModeButtonPanel()
	{
		ModeButtonHandler mbh = new ModeButtonHandler();
		JButton normal = new JButton("Normal");
		normal.addActionListener(mbh);
		add(normal);
		JButton timer = new JButton("Timer");
		timer.addActionListener(mbh);
		add(timer);
		mode = new String("Normal");
	}
	
	// This method returns the mode to other classes
	public String returnMode()
	{
		return mode;
	}
	
	//This is the button handler method that sets the mode
	// depending on the button the user clicks
	class ModeButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if (evt.getActionCommand().equals("Normal"))
				mode = "Normal";
			else if (evt.getActionCommand().equals("Timer"))
				mode = "Timer";
				
		}
	}
}
	
class InstructionsPanel extends JPanel
{
	private GameHolder gameHolder; //Contains cards in CardLayout. //
	private JTextArea gameInstruction; //Contains instructions on how to play game
	private int fontSize; //Font size of text in JTextAreas
	private JTextArea quizInstruction;
	//adds JPanel and two JTextAreas. //
	public InstructionsPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		fontSize = 15;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new GridLayout(2,2,0,10));
		gameInstruction = new JTextArea(" ",20,20);
		gameInstruction.setLineWrap(true);
		gameInstruction.setWrapStyleWord(true);
		gameInstruction.setEditable(false);
		gameInstruction.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane jsp1 = new JScrollPane(gameInstruction);
		add(jsp1);
		InstructionTopRightPanel trp = new InstructionTopRightPanel(gameHolder);
		add(trp);
		quizInstruction = new JTextArea(" ",20,20);
		quizInstruction.setLineWrap(true);
		quizInstruction.setWrapStyleWord(true);
		quizInstruction.setEditable(false);
		quizInstruction.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane jsp2 = new JScrollPane(quizInstruction);
		add(jsp2);
		InstructionBottomLeftPanel iblp = new InstructionBottomLeftPanel();
		add(iblp);
	}
	
	//sets font and text of JTextAreas
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Font font = new Font("SansSerif", Font.PLAIN, fontSize);
		gameInstruction.setFont(font);
		String howToPlay = new String("Dodge obstacles by using the left and right arrow keys and attempt to"+
		" hit the fish and coins. The goal of the game is to collect a certain"+
		" number of fish to pass the level. "+
		"Every time you hit a fish, you will be asked a probability question"+
		", and if it is answered correctly, then you earn one fish. Coins can"+
		" be used to purchase hints, and everytime you hit an obstacle you will"
		+" lose one fish. If you hit an obstacle while you have zero fish, then you lose the level"+
		" and have to restart.");
		gameInstruction.setText(howToPlay);
		
		quizInstruction.setFont(font);
		String howToAnswer = new String("Click on one of the multiple choice answers shown to answer"+
		" the question displayed at the top of the screen. If you are confused, you can purchase" +
		" a hint with 5 coins.");
		quizInstruction.setText(howToAnswer);
		
		
	}
	
	//recieves font size and repaints
	public void getFontSize(int sizeIn)
	{
		fontSize = sizeIn;
		this.repaint();
	}
}

class InstructionBottomLeftPanel extends JPanel
{
	//draws image of QuestionPanel
	public void paintComponent(Graphics g)
	{
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		super.paintComponent(g);
		FileIO file = new FileIO();
		String gameImageName = new String("questionPanelImage.jpg");
		Image gameImage = file.loadImages(gameImageName);
		g.drawImage(gameImage,50,50,294,248,this);
	}
}

class InstructionTopRightPanel extends JPanel
{
	private GameHolder gameHolder;//Contains cards in CardLayout
	//adds JButton and JLabel. //
	public InstructionTopRightPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		JButton startPage = new JButton("Home");
		StartPageButtonHandler spbh = new StartPageButtonHandler();
		startPage.addActionListener(spbh);
		add(startPage);
	}
	
	//draws image of GamePanel
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		FileIO file = new FileIO();
		String questionImageName = new String("gamePanelImage.jpg");
		Image questionImage = file.loadImages(questionImageName);
		g.drawImage(questionImage,50,50,293,245,this);
	}
		
	//Switching the panels using CardLayout
	class StartPageButtonHandler implements ActionListener
	{
		// If button is pressed, StartPanel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			cards.show(gameHolder, "StartPanel");
		}
	}
}

class ScoresPanel extends JPanel
{
	private GameHolder gameHolder; //Contains cards in CardLayout. //
	private JTextArea scores;//Contains high scores
	private int fontSize;// font size for scores JTextArea
	//adds JPanel, JTextArea, and JScrollPane
	public ScoresPanel(GameHolder ghIn)
	{
		fontSize = 15;
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		setLayout(new BorderLayout());
		SettingsNorthPanel snp = new SettingsNorthPanel(gameHolder);
		add(snp,BorderLayout.NORTH);
		scores = new JTextArea(" ",50,50);
		scores.setLineWrap(true);
		scores.setWrapStyleWord(true);
		scores.setEditable(false);
		scores.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane jsp = new JScrollPane(scores);
		add(jsp,BorderLayout.CENTER);
	}

	// writes high scores to the scores JTextArea
	public void writeHighScores()
	{
		FileIO fileIO = new FileIO();
		String fileName = "accountInfo.txt";
		Scanner readFile = fileIO.readFile(fileName);
		int count = 0;
		String nameLine = "";
		String levelLine = "";
		String saved = "";
		String[] userInfo = new String[200];
		int peopleCounter = 0;
		int[][] levelArray;
		int level;
		int indexOfColon;
		String tempStr;
		String printedInfo;
		
		while(readFile.hasNext())
		{
			nameLine = readFile.nextLine();
			readFile.nextLine();
			levelLine = readFile.nextLine();
			readFile.nextLine();
			count++;
			saved = "Name: " + nameLine + "; " + levelLine;
			userInfo[count-1] = saved;
		}
		
		for(int i = 0; i < userInfo.length; i++)
		{
			if(userInfo[i] == null)
				i = userInfo.length;
			else
				peopleCounter++;
		}
		
		levelArray = new int[peopleCounter][2];
		for(int i = 0; i < peopleCounter; i++)
		{
			indexOfColon = userInfo[i].lastIndexOf(":");
			tempStr = userInfo[i].substring(indexOfColon + 2);
			level = Integer.parseInt(tempStr);
			levelArray[i][0] = level;
			levelArray[i][1] = i;
		}
		
		for(int i = 0; i < levelArray.length; i++)
		{
			for(int j = i+1; j < levelArray.length; j++)
			{
				int tempOne = 0;
				int tempTwo = 0;
				if(levelArray[i][0] < levelArray[j][0])
				{
					tempOne = levelArray[i][0];
					levelArray[i][0] = levelArray[j][0];
					levelArray[j][0] = tempOne;
					
					tempTwo = levelArray[i][1];
					levelArray[i][1] = levelArray[j][1];
					levelArray[j][1] = tempTwo;
				}
			}
		}
		
		printedInfo = new String("");
		
		for(int i = 0; i < peopleCounter; i++)
		{
			int tempCount = levelArray[i][1];
			printedInfo += userInfo[tempCount] + "\n";
		}
		scores.setText(printedInfo);
	}
	
	// changes font size of the scores JTextArea
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Font font = new Font("SansSerif", Font.PLAIN, fontSize);
		scores.setFont(font);
		writeHighScores();
	}
	
	// recieves font size and repaints
	public void getFontSize(int sizeIn)
	{
		fontSize = sizeIn;
		this.repaint();
	}

	class StartPageButtonHandler implements ActionListener
	{
		// if button is pressed, StartPanel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			cards.show(gameHolder, "StartPanel");
		}
	}
}

class SettingsNorthPanel extends JPanel
{
	private GameHolder gameHolder; //contains cards in CardLayout
	
	// Adds JButton and JLabel. //
	public SettingsNorthPanel(GameHolder ghIn)
	{
		gameHolder = ghIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		JButton startPage = new JButton("Go to start page");
		StartPageButtonHandler spbh = new StartPageButtonHandler();
		startPage.addActionListener(spbh);
		add(startPage);
		JLabel name = new JLabel("Scores");
		add(name);
	}
	
	// The button will go back to the start panel
	// using the CardLayout
	class StartPageButtonHandler implements ActionListener
	{
		// If button is pressed, StartPanel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			cards.show(gameHolder, "StartPanel");	
		}
	}
}

class GamePanel extends JPanel
{
	private GameHolder gameHolder; //Contains cards in CardLayout. //
	private CenterPanelsHolder cph; //Used to call method to get instance of CenterGamePanel
	// Adds JButton and JLabel. //
	private SettingsPanel sp;//Passed to CenterPanelsHolder
	private NorthPanel np;//Calls method in NorthPanel class
	private JTextArea showCoinNum;//Shows amount of coins user collected
	private JTextArea showFishNum;//Shows amount of fish user collected
	private int fishNeeded;//The number of fish needed to pass the level
	private LogInPanel lip;//Used to call method in LogInPanel to get the username 
	private JTextArea showLevel;//Shows the level that the user is on
	private int level;//The level that the user is on
	private boolean notChanged;//Used so that the method that gets the level is only called once
	private int fishAmount;//The amount of fish that the user collected
	
	//adds JPanel, JButton, and JTextAreas
	public GamePanel(GameHolder ghIn, SettingsPanel spIn, NorthPanel npIn, LogInPanel lipIn)
	{
		setLayout(new BorderLayout());
		gameHolder = ghIn;
		sp = spIn;
		np = npIn;
		lip = lipIn;
		notChanged = true;
		fishAmount = 0;
		
		level = getLevel();
		JPanel north = new JPanel();
		showCoinNum = new JTextArea("Number of coins: 0");
		showCoinNum.setEditable(false);
		showCoinNum.setMargin(new Insets(5, 5, 5, 5));
		north.add(showCoinNum);
		StartPageButtonHandler spbh = new StartPageButtonHandler();
		JButton startPage = new JButton("Home");
		startPage.addActionListener(spbh);
		north.add(startPage);
		JButton exitButton = new JButton("Exit Game");
		exitButton.addActionListener(spbh);
		north.add(exitButton);
		fishNeeded = level + 2;
		showFishNum = new JTextArea("Number of fish: 0" + "/" + fishNeeded);
		showFishNum.setEditable(false);
		showFishNum.setMargin(new Insets(5, 5, 5, 5));
		north.add(showFishNum);
		showLevel = new JTextArea("Level: 1");
		showLevel.setMargin(new Insets(5, 5, 5, 5));
		showLevel.setEditable(false);
		north.add(showLevel);
		
		add(north, BorderLayout.NORTH);
		
		cph = new CenterPanelsHolder(sp, gameHolder, np, this);
		add(cph, BorderLayout.CENTER);
	}
	
	//calls method in CenterPanelsHolder to get instance of CenterGamePanel
	public CenterGamePanel getCGP()
	{
		CenterGamePanel cgp = cph.returnCGP();
		return cgp;
	}
	
	//Changes the JTextArea every time the user gains or loses coins
	public void changeCoinAmount(int coinAmountIn)
	{
		showCoinNum.setText("Number of coins: " + coinAmountIn);
		displayText();
	}
	
	/*gets the amount of fish the user collected, sets
	 * the amount of fish needed based on the level, and calls method.*/
	public void changeFishAmount(int fishAmountIn)
	{
		fishNeeded = level + 2;
		fishAmount = fishAmountIn;
		displayText();
	}
	
	//Changes level every time the user passes a level
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(notChanged)
		{
			level = getLevel();
			notChanged = false;
		}
		else
			level = returnLevel();
		fishNeeded = level + 2;
		showLevel.setText("Level: " + level);
		showFishNum.setText("Number of fish: " +  fishAmount + "/" + fishNeeded);
	}
	
	//changes to JTextArea every time the user gains or loses a fish
	public void displayText()
	{
		showFishNum.setText("Number of fish: " +  fishAmount + "/" + fishNeeded);
	}
	
	/*gets the level of the user by reading the information
	 * from accountInfo.txt.*/
	public int getLevel()
	{
		String userName = lip.returnName();
		FileIO fileIO = new FileIO();
		String fileName = "accountInfo.txt";
		Scanner readFile = fileIO.readFile(fileName);
		String line = new String("");
		int returned = 0;
		while(readFile.hasNext())
		{
			line = readFile.nextLine();
			if(line.equals(userName))
			{
				readFile.nextLine();
				String levelLine = readFile.nextLine();
				int indexOfColon = levelLine.indexOf(":");
				String temp = levelLine.substring(indexOfColon + 2);
				returned = Integer.parseInt(temp);
			}
		}
		
		return returned;
	}

	//returns level
	public int returnLevel()
	{
		return level;
	}
	
	//recieves level
	public void setLevel(int levelIn)
	{
		level = levelIn;
	}

	class StartPageButtonHandler implements ActionListener
	{
		// if button is pressed, StartPanel is shown. //
		public void actionPerformed (ActionEvent evt)
		{
			CardLayout cards = gameHolder.getCards();
			String command = evt.getActionCommand();
			if (command.equals("Home"))
			{
				cards.show(gameHolder, "StartPanel");
				np.setPreviousPanel(false);
			}
			else if (command.equals("Exit Game"))
			{
				System.exit(1);
			}
		}
	}
}

// Cardlayout for the game panel which consists of the
// CenterGamePanel holder for the animations and the QuestionsPanel that
// will appear when the user hits a fish.
class CenterPanelsHolder extends JPanel
{
	private CardLayout cards; //CardLayout for JPanel
	private SettingsPanel sp; //Passed to CenterGamePanel
	private GameHolder gh; //Passed to QuestionsPanel
	private NorthPanel np; //Passed to QuestionsPanel
	private GamePanel gp; //Passed to QuestionsPanel and CenterGamePanel
	private CenterGamePanel cgp; //Instance of CenterGamePanel
	
	//adds JPanels to CardLayout
	public CenterPanelsHolder(SettingsPanel spIn, GameHolder ghIn, NorthPanel npIn, GamePanel gpIn)
	{
		sp = spIn;
		gh = ghIn;
		np = npIn;
		gp = gpIn;
		cards = new CardLayout();
		setLayout(cards);

		QuestionsPanel qp = new QuestionsPanel(this, gh, np, gp, sp);
		cgp = new CenterGamePanel(this, sp, qp, gp);
		qp.getCenterGamePanel(cgp);

		add(cgp, "Center Game Panel");
		add(qp, "Questions Panel");
	}
	
	// Used to get the CardLayout from other classes. //
	public CardLayout getCards()
	{
		return cards;
	}
	
	//returns instance of CenterGamePanel
	public CenterGamePanel returnCGP()
	{
		return cgp;
	}
}

class CenterGamePanel extends JPanel implements KeyListener, MouseListener
{
	private String fishName; // fish image file name for FileIO
	private String penguinName; // penguin image file name for FileIO
	private String coinName; // coin image file name for FileIO
	private String collisionName; // collision img file name for FileIO
	private Image fish; // fish image to be loaded in
	private Image penguin; // penguin Image to be loaded in
	private Image coin; // coin image to be loaded in
	private Image collision; // collision image to be loaded in
	private int penguinXCord; // x coordinate of penguin image//
	private SettingsPanel sp;//Used to get the color of the penguin
	private QuestionsPanel qp; // used to display when user hits a fish
	private boolean wait; // waits for user to resume after they answer a question
	private boolean resume; // checks if user clicks the resume rectangle
	private AnswersAreaPanel aap; // used to return number of fish
	private int level; // gets level to check how fast the obstacles should be drawn
	private int fishNeeded; // number of fish the user needs to pass the level
	private int speed; // calculated based on the user's level and changes
						// the speed obstacles are drawn in
	private ButtonsAreaPanel bap; // returns number of coins to check
									// if user has enough to get a hint
	private boolean passLevel;//Shows if user passed the lever or not
	
	private Timer timer; // timer used to draw animations
	private int count; //Number of times paintComponents was called//
	private int obstacleCounter; //Number of new obstacles drawn
	private int fishCounter; //Number of new fish drawn
	private int coinCounter; //Number of new coins drawn
	private int [][] xCoordinates; //x coordinates of obstacles
	private int [][] yCoordinates; // y coordinates of obstacles
	private int [] fishYCoords; //y coordinates of fish image
	private int [] coinXCoords; //x coordinates of coin image
	private int [] coinYCoords; //y coordinates of coin image
	private boolean start; // checks if user has clicked the start rectangle
	private boolean lose;//Shows if user lost
	private boolean win;//Shows if user won the game
	private boolean fishDrawn; //Shows if first fish has been drawn
	private boolean obstacleDrawn; //Shows if first obstacle has been drawn
	private boolean coinDrawn; //Shows if first coin has been drawn
	private int [] fishXCoords;//x coordinates of fish image
	private int coinAmount; //Number of coins the player collected
	private int fishAmount;//Amount of fish the user collected
	private String password;//Password for account
	private String name; //Name for account
	private CenterPanelsHolder cph; //used to add this panel to the CardLayout
	private GamePanel gp;//Used to change JTextAreas in GamePanel
	
	//Sets variables
	public CenterGamePanel(CenterPanelsHolder cphIn, SettingsPanel spIn, QuestionsPanel qpIn, GamePanel gpIn)
	{
		// Vars for CardLayout, listeners, and loading images - coded by Katrina.
		sp = spIn;
		cph = cphIn;
		qp = qpIn;
		aap = qp.returnAAP();
		bap = qp.returnBAP();
		gp = gpIn;
		level = 1;
		fishNeeded = 1;
		addKeyListener(this);
		addMouseListener(this);
		penguinName = new String("penguin.jpg");
		fishName = new String("fish.jpg");
		coinName = new String("coin.jpg");
		collisionName = new String("collision.jpg");
		fish = loadImages(fishName);
		penguin = loadImages(penguinName);
		coin = loadImages(coinName);
		collision = loadImages(collisionName);
		penguinXCord = 58;
		wait = false;
		resume = false;
		speed = 1;
		passLevel = false;
		
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		
		// Variables for Timer and obstacle coordinates - coded by Prita. 
		win = false;
		lose = false;
		TimerHandler th = new TimerHandler();
		timer = new Timer(10,th);
		timer.start();
		count = 0;
		obstacleCounter = 0;
		fishCounter = 0;
		coinCounter = 0;
		xCoordinates = new int[201][5];
		yCoordinates = new int[201][5];
		fishYCoords = new int[200];
		fishXCoords = new int[200];
		coinXCoords = new int[200];
		coinYCoords = new int[200];
		fishAmount = 0;
		start = false;
		password = new String("");
		name = new String("");
	}
	
	// This method loads in the images using ImageIO and 
	// returns the image back to the constructor.
	public Image loadImages(String nameIn)
	{
		Image image = null;
		File imgFile = new File(nameIn);
		try
		{
			image = ImageIO.read(imgFile);
			
		}
		catch(IOException e)
		{
			System.err.println("The image " + nameIn + " cannot be loaded.");
		}
		return image;
	}
	
	// Draws columns, penguin, rectangle, and string. Sets speed based on level and 
	//calls method that draws the obstacles. //
	public void paintComponent(Graphics g)
	{
		level = gp.returnLevel();
		fishNeeded = level + 2;
		if(level == 1 || level == 2 || level == 3)
		{
			speed = 1;
		}
		else if (level == 4 || level == 5 || level == 6)
		{
			speed = 2;
		}
		else if (level == 7 || level == 8 || level == 9)
		{
			speed = 3;
		}
		else if (level == 10)
		{
			speed = 4;
		}
		
		super.paintComponent(g);
		setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 200, 700);
		g.drawRect(200, 0, 200, 700);
		g.drawRect(400, 0, 200, 700);
		g.drawRect(600, 0, 200, 700);
		
		String penguinColor = sp.getPenguinColor();
		if (penguinColor.equals("blue"))
			g.drawImage(penguin, penguinXCord, 525,penguinXCord +82 , 609, 55, 25, 315, 293, this);
		else if(penguinColor.equals("cyan"))
			g.drawImage(penguin, penguinXCord, 525,penguinXCord +82 , 609, 405, 25, 665, 293, this);
		else if(penguinColor.equals("black"))
			g.drawImage(penguin, penguinXCord, 525,penguinXCord +82 , 609, 70, 380, 330, 648, this); 
		else if (penguinColor.equals("pink"))
			g.drawImage(penguin, penguinXCord, 525,penguinXCord +82 , 609, 420, 380, 680, 648, this); 

		g.setColor(Color.CYAN);
		count++;
		if(level > 10)
		{
			win = true;
			start = true;
			passLevel = false;
		}
		if (resume == true)
		{
			g.fillRect(200,200,400,170);
			Font font = new Font("plain", Font.PLAIN, 30);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("Click here to resume",225,300);
		}
		
		if(start == false)
		{
			g.fillRect(200,200,400,170);
			Font font = new Font("plain", Font.PLAIN, 40);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("Click here to start",225,300);
		}
		else if(lose == true)
		{
			g.fillRect(200,200,400,170);
			Font font = new Font("plain", Font.PLAIN, 20);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("You lost. Click here to try again.",225,300);
		}
		else if (passLevel == true)
		{
			if(level<=10)
			{
				g.fillRect(200,200,400,170);
				Font font = new Font("plain", Font.PLAIN, 18);
				g.setFont(font);
				g.setColor(Color.BLACK);
				g.drawString("You won the level! Click here for the next level.",205,300);
			}
		}
		else if(win == true)
		{
			g.fillRect(50,200,700,170);
			Font font = new Font("plain", Font.PLAIN, 18);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("Congratulations! You won the game! Exit and create a new account to start over.",60,300);
		}
		else if(start == true && resume == false && lose == false && passLevel == false)
		{
			drawObstacleAndFish(g);
		}
	}
	
	/*Creates coordinates for a new obstacle or fish in a 
	 * randomized column of the panel every 100 times the method is called.
	 * Each polygon and image is animated to move down the screen. Each time
	 * polygons or images are redrawn for animation, the coordinates of the polygon
	 * and image are compared to the coordinates of the penguin, to see if
	 * they are overlapping. If they are, then the timer stops.*/
	public void drawObstacleAndFish(Graphics g)
	{
		int mod = 66;
		if(speed == 1)
			mod = 66;
		else if (speed == 2)
			mod = 50;
		else if (speed == 3)
			mod = 33;
		else if (speed == 4)
			mod = 25;
		g.setColor(Color.CYAN);
		
		if((count%mod)==0)
		{
			// Draws new obstacles and fish at top of panel and saves coordinates to arrays.
			g.setColor(Color.CYAN);
			int randNum = (int)(Math.random()*12+1);
			if(randNum == 1)
			{
				int randNumber = (int)(Math.random()*4+0);
				fishXCoords[fishCounter] = 20 + (200*randNumber);
				fishYCoords[fishCounter] = -50;
				g.drawImage(fish,fishXCoords[fishCounter], fishYCoords[fishCounter], 161, 66, this);
				fishCounter++;
				fishDrawn = true;
			}
			else if(randNum == 2 || randNum == 3 || randNum == 4)
			{
				int randomInt = (int)(Math.random()*4+0);
				coinXCoords[coinCounter] = 55 + (200*randomInt);
				coinYCoords[coinCounter] = -50;
				g.drawImage(coin,coinXCoords[coinCounter],coinYCoords[coinCounter],84,84,this);
				coinCounter++;
				coinDrawn = true;
			}
			else
			{
				int rand = (int)(Math.random()*4+0);
				int [] xArray = new int[]{32+(rand*200),69+(rand*200),94+(rand*200),131+(rand*200),167+(rand*200)};
				int [] yArray = new int[] {-50,-100,-70,-125,-50};
				xCoordinates[obstacleCounter] = xArray;
				yCoordinates[obstacleCounter] = yArray;
				g.fillPolygon(xCoordinates[obstacleCounter], yCoordinates[obstacleCounter],5);
				obstacleCounter++;
				g.setColor(Color.CYAN);
				obstacleDrawn = true;
			}
		}
		/*After first obstacle was drawn, obstacles are animated to move down the
		page. If penguin hits obstacle, then timer stops. If penguin hits fish,
		* then QuestionsPanel is shown. If penguin hits coin, then the user gains
		* one coin.*/
		int largestPenguinX = penguinXCord + 82;
		for (int k=0; k<fishCounter; k++)
		{
			int largestFishX = fishXCoords[k] + 161;
			int largestFishY = fishYCoords[k] + 66;
			if(fishDrawn == true)
			{
				fishYCoords[k] +=(speed + 2);
				g.drawImage(fish, fishXCoords[k], fishYCoords[k], 161, 66, this);
				if(((penguinXCord>fishXCoords[k]) && (penguinXCord<largestFishX)) || ((largestPenguinX>fishXCoords[k]) && (largestPenguinX<largestFishX)))
				{
					if((525>fishYCoords[k] && 525<largestFishY)||(609>fishYCoords[k] && 609<largestFishY))
					{
						timer.stop();
						if(wait == true)
						{
							if(fishAmount == fishNeeded)
							{
								level++;
								gp.setLevel(level);
								gp.changeFishAmount(0);
								gp.repaint();
								aap.getFishAmount(0);
								passLevel = true;
								writeToFile();
							}
							else
							{
								resume = true;
							}
							resetPage();
							wait = false;
							timer.start();
						}
						else if (wait == false)
						{
							CardLayout cards = cph.getCards();
							cards.show(cph, "Questions Panel");
							qp.reset();
						}
					}
				}
			}
		}
		for(int b=0; b<=obstacleCounter; b++)
		{
			if(obstacleDrawn == true)
			{
				for (int i=0; i<5; i++)
				{
					yCoordinates[b][i] +=(speed + 2);
				}
				g.fillPolygon(xCoordinates[b],yCoordinates[b],5);
				if((penguinXCord>xCoordinates[b][0] && penguinXCord<xCoordinates[b][4])||(largestPenguinX>xCoordinates[b][0] && largestPenguinX<xCoordinates[b][4]))
				{
						
					if((525>yCoordinates[b][3] && 525<yCoordinates[b][0])||(609>yCoordinates[b][3] && 609<yCoordinates[b][0]))
					{
						timer.stop();
						int width = xCoordinates[b][4] - xCoordinates[b][0];
						int height = yCoordinates[b][0] - yCoordinates[b][3];
						g.drawImage(collision, xCoordinates[b][0], yCoordinates[b][3], width, height, this);
						fishAmount--;
						if(fishAmount < 0)
						{
							fishAmount = 0;
							lose = true;
						}
						else
							resume = true;
						gp.changeFishAmount(fishAmount);
						aap.getFishAmount(fishAmount);
						resetPage();
						timer.start();
					}
				}
			}
		}
		for (int j=0; j<coinCounter; j++)
		{
			if(coinDrawn == true)
			{
				coinYCoords[j] +=(speed + 2);
				g.drawImage(coin, coinXCoords[j], coinYCoords[j], 84, 84, this);
				if((penguinXCord>coinXCoords[j] && penguinXCord<(coinXCoords[j]+161)||(largestPenguinX>coinXCoords[j] && largestPenguinX<(coinXCoords[j]+161))))
				{
					if((525>coinYCoords[j] && 525<(coinYCoords[j]+84))||(609>coinYCoords[j] && 609<(coinYCoords[j]+84)))
					{
						coinAmount++;
						gp.changeCoinAmount(coinAmount);
						bap.getCoinAmount(coinAmount);
						for(int t=0; t<(coinCounter-j); t++)
						{
							coinXCoords[j] = coinXCoords[j+1];
							coinYCoords[j] = coinYCoords[j+1];
							coinCounter--;
						}
						
					}
				}
			}
		}
		if(obstacleCounter == 200)
		{
			for(int n=0; n<199; n++)
			{
				xCoordinates[n] = xCoordinates[n+1];
				yCoordinates[n] = yCoordinates[n+1];
				obstacleCounter = 0;
			}
		}
		if(fishCounter == 200)
		{
			for(int p = 0; p<=199; p++)
			{
				fishXCoords[p] = fishXCoords[p+1];
				fishYCoords[p] = fishYCoords[p+1];
				fishCounter = 0;
			}
		}
	}
	
	//after the user completes a level, their new information is written 
	//to accountInfo.txt.
	public void writeToFile()
	{
		FileIO fileIO = new FileIO();
		String writeFileName = new String("accountInfo.txt");
		PrintWriter write = fileIO.writeFile(writeFileName);
		write.println(name);
		write.println(password);
		write.println("Level: " + level);
		write.println("Coins: " + coinAmount);
		write.close();
	}
	
	//recieves account info
	public void setAccountInfo(String nameIn, String passwordIn, int coinIn)
	{
		name = nameIn;
		password = passwordIn;
		coinAmount = coinIn;
		gp.changeCoinAmount(coinAmount);
		bap.getCoinAmount(coinAmount);
	}
	
	//recieves the number of fish collected
	public void getFishAmount(int fishAmountIn)
	{
		fishAmount = fishAmountIn;
	}
	
	//recieves the number of coins collected
	public void getCoinAmount(int coinAmountIn)
	{
		coinAmount = coinAmountIn;
	}
	
	//sets wait boolean when the user returns to CenterGamePanel
	public void setWaitBoolean(boolean waitIn)
	{
		wait = waitIn;
	}
	
	//returns the number of fish needed to pass the level
	public int returnFishNeeded()
	{
		return fishNeeded;
	}
	
	//sets passLevel based on whether the user collected enough fish
	public void setNextLevel(boolean passLevelIn)
	{
		passLevel = passLevelIn;
	}
	
	//resets page so user can restart
	public void resetPage()
	{
		count = 0;
		obstacleCounter = 0;
		fishCounter = 0;
		xCoordinates = new int[201][5];
		yCoordinates = new int[201][5];
		fishYCoords = new int[200];
		fishXCoords = new int[200];
		
	}
	
	//This method makes the penguin move to a corresponding 
	//location based on if the user clicks the right or left button. To avoid 
	// going off the screen, if-else statements are used to make sure
	// the penguin stays within the boundaries.
	public void keyPressed(KeyEvent evt)
	{
		requestFocusInWindow();
		
		int keyCode = evt.getKeyCode();
		if(keyCode == KeyEvent.VK_RIGHT)
			penguinXCord += 200;
		else if(keyCode == KeyEvent.VK_LEFT)
			penguinXCord -= 200;
		
		if (penguinXCord > 658)
			penguinXCord = 658;
		else if (penguinXCord < 58)
			penguinXCord = 58;
	}

	public void keyReleased(KeyEvent evt) {}
    public void keyTyped(KeyEvent evt) {}
    
	public void mouseReleased(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseExited(MouseEvent evt){}
	public void mouseClicked(MouseEvent evt){}
	
	// The timer starts when the user clicks on the start rectangle that 
	// is within the coordinates used in this method. 
	public void mousePressed(MouseEvent evt)
	{
		requestFocusInWindow();
		if(start == false)
		{
			if(evt.getX() > 200 && evt.getX() < 600)
			{
				if(evt.getY() > 200 && evt.getY() < 370)
					start = true;
			}

		}
		else if (resume == true)
		{
			if(evt.getX() > 200 && evt.getX() < 600)
			{
				if(evt.getY() > 200 && evt.getY() < 370)
					resume = false;
			}
		}
		else if(lose == true)
		{
			if(evt.getX() > 200 && evt.getX() < 600)
			{
				if(evt.getY() > 200 && evt.getY() < 370)
					lose = false;
			}
		}
		else if (passLevel == true)
		{
				if(evt.getX() > 200 && evt.getX() < 600)
				{
					if(evt.getY() > 200 && evt.getY() < 370)
					{
						passLevel = false;
						timer.start();
					}
				}
		}
	}
	
	//returns instance of CenterGamePanel
	public CenterGamePanel returnClass()
	{
		return this;
	}
	
	class TimerHandler implements ActionListener
	{
		// calls paintComponent. //
		public void actionPerformed(ActionEvent evt)
		{
			repaint();
		}
	}
}

//This class holds the questions and answers from
// the other classes in a border layout
class QuestionsPanel extends JPanel
{
	private CenterPanelsHolder cph; // Sent to AnswersAreaPanel
	private GameHolder gh; // Sent to ButtonsAreaPanel
	private NorthPanel np; // Sent to ButtonsAreaPanel
	private QuestionsAreaPanel qap; // Sent to AnswersAreaPanel
	private AnswersAreaPanel aap; // Used to reset in reset method
	private CenterGamePanel cgp;//Returned to other classes
	private GamePanel gp;//Passed to QuestionsAreaPanel, ButtonsAreaPanel, and AnswersAreaPanel
	private ButtonsAreaPanel bap;//Returned to other classes
	private SettingsPanel sp; // Returned to other classes
	
	// Adds JButton and JLabel. //
	public QuestionsPanel(CenterPanelsHolder cphIn, GameHolder ghIn, NorthPanel npIn, GamePanel gpIn, SettingsPanel spIn)
	{
		cph = cphIn;
		gh = ghIn;
		np = npIn;
		cgp = null;
		gp = gpIn;
		sp = spIn;
		setLayout(new BorderLayout());
		
		qap = new QuestionsAreaPanel(gp,this);
		add(qap, BorderLayout.NORTH);
		bap = new ButtonsAreaPanel(gh, np, qap, this, gp, sp, cph);
		add(bap, BorderLayout.CENTER);
		aap = new AnswersAreaPanel(cph, qap, this, gp);
		add(aap, BorderLayout.SOUTH);
	}
	
	// the reset methods of the JPanels added to this class
	// so that this method can be called at the same time the timer
	// is stopped after hitting an obstacle or fish
	public void reset()
	{
		qap.resetQuestion();
		aap.resetAnswers();
		bap.timerInitialValues();
	}
	
	//calls method to get CenterGamePanel instance
	public void getCenterGamePanel(CenterGamePanel cgpIn)
	{
		cgp = cgpIn;
		aap.getCGP();
	}
	
	//returns AnswersAreaPanel instance
	public AnswersAreaPanel returnAAP()
	{
		return aap;
	}
	
	//returns CenterGamePanel instance
	public CenterGamePanel returnCGP()
	{
		return cgp;
	}
	
	//returns QuestionsAreaPanel instance
	public QuestionsAreaPanel returnQAP()
	{
		return qap;
	}
	
	//returns ButtonsAreaPanel instance
	public ButtonsAreaPanel returnBAP()
	{
		return bap;
	}
}

class QuestionsAreaPanel extends JPanel
{
	private int problemNum; // randomly generated and sent to AnswersAreaPanel class
	private JTextArea questionArea; // text area where question is displayed
	private JTextArea directionTextArea;//text area that shows directions
	private JTextArea hintTextArea;//text area that shows hints
	private boolean change; //Makes sure the text in a drawString only changes after the hint button is pressed
	private GamePanel gp;//Used to call method that returns level
	private QuestionsPanel qp;//Used to call method to get AnswersAreaPanel instance
	
	//Adds JTextAreas
	public QuestionsAreaPanel(GamePanel gpIn, QuestionsPanel qpIn)
	{
		setLayout(new FlowLayout(FlowLayout.CENTER,10,20));
		setPreferredSize(new Dimension(800, 300));
		problemNum = 1;
		questionArea = new JTextArea();
		questionArea.setEditable(false);
		questionArea.setWrapStyleWord(true);
		questionArea.setLineWrap(true);
		questionArea.setMargin(new Insets(5, 5, 5, 5));
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		qp = qpIn;
		gp = gpIn;
		directionTextArea = new JTextArea("If you are confused, click on the hint button!");
		directionTextArea.setEditable(false);
		directionTextArea.setWrapStyleWord(true);
		directionTextArea.setLineWrap(true);
		directionTextArea.setMargin(new Insets(5, 5, 5, 5));
		hintTextArea = new JTextArea("Each hint costs 5 coins", 2, 0);
		hintTextArea.setEditable(false);
		hintTextArea.setWrapStyleWord(true);
		hintTextArea.setLineWrap(true);
		hintTextArea.setMargin(new Insets(5, 5, 5, 5));
		add(directionTextArea);
		add(questionArea);
		add(hintTextArea);
	}
	
	// resets to a new question using problemNum which is randomly generated.
	// It reads from the questions.txt file and checks for where the question
	// is in the file and sets it to the text area
	public void resetQuestion()
	{
		AnswersAreaPanel aap = qp.returnAAP();
		problemNum = (int)(Math.random()*20 + 1);
		int level = gp.returnLevel();
		aap.getLevel(level);
		Scanner readIn = null;
		FileIO fileIO = new FileIO();
		String fileName = "SpeedOneQuestions.txt";
		//if statements that set fileName to different string based on level
		if(level == 1 || level == 2 || level == 3)
		{
			fileName = "SpeedOneQuestions.txt";
		}
		else if (level == 4 || level == 5 || level == 6)
		{
			fileName = "SpeedTwoQuestions.txt";
		}
		else if (level == 7 || level == 8 || level == 9)
		{
			fileName = "SpeedThreeQuestions.txt";
		}
		else if (level == 10)
		{
			fileName = "SpeedFourQuestions.txt";
		}
		readIn = fileIO.readFile(fileName);
		String line;
		String question;
		int num;
		
		while(readIn.hasNext())
		{
			line = readIn.nextLine();
			question = line.substring(0, 8);
			if(question.equals("Question"))
			{
				int spaceIndex = line.indexOf(" ");
				int colonIndex = line.indexOf(":");
				num = Integer.parseInt(line.substring(spaceIndex + 1, colonIndex));
				
				if (num == problemNum)
				{
					questionArea.setText(line.substring(11));
				}
			}
		}
		
	}

	// problemNum used in AnswersAreaPanel
	public int returnQuestionNumber()
	{
		return problemNum;
	}
	
	// questionArea used in AnswersAreaPanel
	public JTextArea returnQuestionArea()
	{
		return questionArea;
	}
	
	//changes text in direction JTextArea
	public void changeDirection(String directionIn)
	{
		directionTextArea.setText(directionIn);
	}
	
	//changes text in hint JTextArea
	public void changeHint(String hintIn)
	{
		hintTextArea.setText(hintIn);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
}

// This class has buttons to give the user extra help and
// also give them a hint when they need it.
class ButtonsAreaPanel extends JPanel implements ActionListener
{
private GameHolder gh; // used for card layout
	private NorthPanel np; // sets boolean inside NorthPanel class
	private QuestionsAreaPanel qap; // used to repaint QuestionsAreaPanel
	private GamePanel gp;//Used to change JTextAreas in GamePanel
	private QuestionsPanel qp;//Used to call methods in QuestionsPanel that return instances of calsses
	private CenterPanelsHolder cph;//Used for CardLayout
	private int coinAmount;//Number of coins that user collected
	private String hint;//Hint for the question
	private boolean incorrect;//Shows if user got the question incorrect
	private SettingsPanel sp; // used to get the String mode
	private String mode; // returned from SettingsPanel for the timer
	private Timer timer; // Timer used for Timer Mode
	private Timer stopwatch; // Timer used for normal mode
	private int tenthSec; // used to calculate time
	private int elapsedMinutes; // used to get how many minutes has passed for the timer
	private double secondsDecimal, secondsDisplay; // numbers displayed on the timer
	private boolean running; // checks if timer is currently running
	private int time; // number of seconds for the timer
	private int elapsedSeconds; // how many seconds have already passed 
	private ModeButtonPanel mbp; // used to return the mode
	private AnswersAreaPanel aap; // used to set boolean values
	public ButtonsAreaPanel(GameHolder ghIn, NorthPanel npIn, QuestionsAreaPanel qapIn, QuestionsPanel qpIn, GamePanel gpIn, SettingsPanel spIn, CenterPanelsHolder cphIn)
	{
		gh = ghIn;
		np = npIn;
		qap = qapIn;
		Color bg = new Color(202, 255, 255);
		setBackground(bg);
		JButton help = new JButton("Extra Help");
		help.addActionListener(this);
		add(help);
		sp = spIn;
		mode = "Normal";
		TimeHandler th = new TimeHandler();
		timer = new Timer ( 100, th );
		stopwatch = new Timer (100, th);
		mbp = sp.returnMBP();
		timerInitialValues();
		
		cph = cphIn;
		qp = qpIn;
		gp = gpIn;
		incorrect = false;
		coinAmount = 0;
		JButton returnButton = new JButton("Return");
		returnButton.addActionListener(this);
		add(returnButton);
		JButton hintButton = new JButton("Hint");
		hintButton.addActionListener(this);
		add(hintButton);
	}
	
	// If the Extra Help or Return button are pressed, then the corresponding
	// panel is shown using the CardLayout. If the Hint button is pressed,
	// then the hint for the question is shown in a JTextArea in QuestionsAreaPanel.
	public void actionPerformed(ActionEvent evt)
	{
		AnswersAreaPanel aap = qp.returnAAP();
		CenterGamePanel cgp = qp.returnCGP();
		String command = evt.getActionCommand();
		if(command.equals("Extra Help"))
		{
			np.setPreviousPanel(true);
			CardLayout cards = gh.getCards();
			cards.show(gh, "LearnProbabilityPanel");
			aap.setAnswered(false);
		}
		else if(command.equals("Hint"))
		{
			if(coinAmount>=5)
			{
				qap.changeHint(hint);
				coinAmount -= 5;
				gp.changeCoinAmount(coinAmount);
				cgp.getCoinAmount(coinAmount);
			}
			else
			{
				qap.changeHint("You do not have enough coins to purchase a hint");
			}
		}
		else if(command.equals("Return"))
		{
			if(incorrect == true)
			{
				cgp.setWaitBoolean(true);
				cgp.repaint();
				CardLayout cards = cph.getCards();
				cards.show(cph, "Center Game Panel");
				incorrect = false;
				aap.setAnswered(false);
			}
		}
	}
	
	//sets incorrect if user selectes the wrong answer
	public void setIncorrect(boolean incorrectIn)
	{
		incorrect = incorrectIn;
	}
	
	//recieves number of coins user collected
	public void getCoinAmount(int coinAmountIn)
	{
		coinAmount = coinAmountIn;
	}
	
	//recieves hint from AnswersAreaPanel
	public void getHint(String hintIn)
	{
		hint = hintIn;
	}
	
	//receives mode from ModeButtonPanel and does the calculations
	// for the timers and displays the time based on which mode the user
	// is playing with.
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		mode = mbp.returnMode();
		
		if(mode.equals("Normal"))
		{
			secondsDecimal = tenthSec / 10.0;
			secondsDisplay = secondsDecimal % 60; 
			elapsedMinutes = (int)secondsDecimal / 60;
			g.drawString ( "Running:  " + elapsedMinutes + " minutes " + 
			String.format("%.1f", secondsDisplay) + " seconds" , 20, 50 );
		}
		
		else if(mode.equals("Timer"))
		{
			secondsDecimal = time - tenthSec / 10.0;
			secondsDisplay = secondsDecimal % 60; 	
			elapsedMinutes = (int)secondsDecimal / 60;
			g.drawString ( "Running:  " + elapsedMinutes + " minutes " + 
			String.format("%.1f", secondsDisplay) + " seconds" , 20, 50 );
		}
	}
	
	//This method gets the method and resets all the values
	// The method is called each time the QuestionsPanel is displayed so that
	// the timer resets every time the user gets a new question
	public void timerInitialValues()
	{
		mode = mbp.returnMode();
		if(mode.equals("Normal"))
		{
			tenthSec = elapsedMinutes = 0;
			secondsDecimal = secondsDisplay = 0.0;
			stopwatch.start();
			running = true;
		}
		else if(mode.equals("Timer"))
		{
			time = 120;
			tenthSec = elapsedSeconds = elapsedMinutes = 0;
			secondsDecimal = 0.0;
			timer.start();
			running = true;
		}
	}
	
	// this method checks if the user runs out of time
	// on Timer mode and marks the question incorrect. In both normal
	// and timer mode, it will just increment tenthSec for the calculating
	// of the timer and calls repaint()
	class TimeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt) 
		{
			mode = mbp.returnMode();
			if(mode.equals("Timer"))
			{
				if (secondsDisplay == 0 && elapsedMinutes == 0)
				{
					timer.stop();
					running = false;
					incorrect = true;
					//aap.setAnswered(false);
				}
			}
			if ( running )
				tenthSec++;
			repaint();
		}
	}
}

// This class has JRadioButtons with the possible answers
// and goes to the CenterGamePanel class in the card layout if the user 
// answers the question correctly
class AnswersAreaPanel extends JPanel
{
	private CenterPanelsHolder cph; // used in card layout
	private QuestionsAreaPanel qap; // used to get question number
	private JRadioButton answer1; // used in handler class
	private JRadioButton answer2; // used in handler class
	private JRadioButton answer3; // used in handler class
	private JRadioButton answer4; // used in handler class
	private String correctAnswer; // string with the correct answer from
								// the file
	private int fishAmount;//number of fish user collected
	private int level;//level that user is on
	private String hintLine; // string with the hint from the file
	private boolean answered;//if the user selected an answer
	private CenterGamePanel cgp; // used for setting boolean value to object
	private QuestionsPanel qp;//Used to call methods that return instances of classes
	private GamePanel gp;//Used to call methods that return instances of classes and a method that changes 
						//the JTextArea showing the number of fish
	private ButtonsAreaPanel bap;//Used to send the hint to ButtonsAreaPanel
	
	//adds 4 JRadioButtons and ActionListener
	public AnswersAreaPanel(CenterPanelsHolder cphIn, QuestionsAreaPanel qapIn, QuestionsPanel qpIn, GamePanel gpIn)
	{
		cph = cphIn;
		qap = qapIn;
		qp = qpIn;
		gp = gpIn;
		bap = null;
		setLayout(new GridLayout(2, 2));
		Color background= new Color(202, 255, 255);
		setBackground(background);
		setPreferredSize(new Dimension(800, 200));
		fishAmount = 0;
		level = 0;
		answered = false;
		ButtonGroup bg = new ButtonGroup();
		CheckAnswer ca = new CheckAnswer();
		answer1 = new JRadioButton();
		answer1.addActionListener(ca);
		answer2 = new JRadioButton();
		answer2.addActionListener(ca);
		answer3 = new JRadioButton();
		answer3.addActionListener(ca);
		answer4 = new JRadioButton();
		answer4.addActionListener(ca);
		bg.add(answer1);
		bg.add(answer2);
		bg.add(answer3);
		bg.add(answer4);
		
		add(answer1);
		add(answer2);
		add(answer3);
		add(answer4);
	}
	
	// Resets the answers corresponding to the right question number 
	// received from QuestionsAreaPanel and saves the correct answer and hint
	public void resetAnswers()
	{
		int probNum = qap.returnQuestionNumber();
		Scanner readIn = null;
		FileIO fileIO = new FileIO();
		String fileName = "SpeedOneQuestions.txt";
		//if statements that set fileName to different string based on level
		if(level == 1 || level == 2 || level == 3)
		{
			fileName = "SpeedOneQuestions.txt";
		}
		else if (level == 4 || level == 5 || level == 6)
		{
			fileName = "SpeedTwoQuestions.txt";
		}
		else if (level == 7 || level == 8 || level == 9)
		{
			fileName = "SpeedThreeQuestions.txt";
		}
		else if (level == 10)
		{
			fileName = "SpeedFourQuestions.txt";
		}
		readIn = fileIO.readFile(fileName);
		String line;
		int num;
		String question;
		qap.changeDirection("If you are confused, click on the hint button!");
		qap.changeHint("Each hint costs 5 coins");
		while(readIn.hasNext())
		{
			line = readIn.nextLine();
			question = line.substring(0, 8);
			if(question.equals("Question"))
			{
				int spaceIndex = line.indexOf(" ");
				int colonIndex = line.indexOf(":");
				num = Integer.parseInt(line.substring(spaceIndex + 1, colonIndex));
				
				if (num == probNum)
				{
					int randomNum = (int)(Math.random()*4+1);
					if(randomNum == 1)
					{
						answer1.setText(readIn.nextLine());
						answer2.setText(readIn.nextLine());
						answer3.setText(readIn.nextLine());
						answer4.setText(readIn.nextLine());
						correctAnswer = answer1.getText();
					}
					if(randomNum == 2)
					{
						answer2.setText(readIn.nextLine());
						answer3.setText(readIn.nextLine());
						answer4.setText(readIn.nextLine());
						answer1.setText(readIn.nextLine());
						correctAnswer = answer2.getText();
					}
					if(randomNum == 3)
					{
						answer3.setText(readIn.nextLine());
						answer4.setText(readIn.nextLine());
						answer1.setText(readIn.nextLine());
						answer2.setText(readIn.nextLine());
						correctAnswer = answer3.getText();
					}
					if(randomNum == 4)
					{
						answer4.setText(readIn.nextLine());
						answer1.setText(readIn.nextLine());
						answer2.setText(readIn.nextLine());
						answer3.setText(readIn.nextLine());
						correctAnswer = answer4.getText();
					}
					hintLine = readIn.nextLine();
					bap = qp.returnBAP();
					bap.getHint(hintLine);
				}
			}
		}
	
	}
	
	//recieves level
	public void getLevel(int levelIn)
	{
		level = levelIn;
	}
	
	//calls method to get instance of CenterGamePanel
	public void getCGP()
	{
		cgp = qp.returnCGP();
	}
	
	//recieves number of fish collected by user
	public void getFishAmount(int fishAmountIn)
	{
		fishAmount = fishAmountIn;
	}
	
	//sets answered when the user clicks on one of the JRadioButtons
	public void setAnswered(boolean answeredIn)
	{
		answered = answeredIn;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	class CheckAnswer implements ActionListener
	{
		// Uses card layout to go to back to the CenterGamePanel class
		public void actionPerformed(ActionEvent evt)
		{
			if(answered == false)
			{
				//Coded by Katrina
				CardLayout cards = cph.getCards();
				String command = evt.getActionCommand();
				if(command.equals(correctAnswer))
				{
					int fishNeeded = cgp.returnFishNeeded();
					fishAmount++;
					gp.changeFishAmount(fishAmount);
					cgp.getFishAmount(fishAmount);
					cgp.setWaitBoolean(true);
					cgp.repaint();
					cards.show(cph, "Center Game Panel");
				}
				//Coded by Prita
				else
				{
					qap.changeDirection("Answer is incorrect. Click on the extra help button to learn about probability, or click on"+
					" the resume button to continue playing.");
					bap.setIncorrect(true);
					answered = true;
					
				}
			}
		}
	}
}

class FileIO
{
	public FileIO()
	{
	}
	
	//creates a Scanner
	public Scanner readFile(String inFileName)
	{
		File inFile = new File(inFileName);
		Scanner readFile = null;
		try
		{
			readFile = new Scanner(inFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Cannot find " + inFileName + "file.");
		
			System.exit(1);
		}
		
		return readFile;
	}
	
	//creates a PrintWriter
	public PrintWriter writeFile(String outFileName)
	{
		PrintWriter write = null;
		File outFile = new File(outFileName);
		try
		{
			write = new PrintWriter(new FileWriter(outFile,true));
		}
		catch(IOException e)
		{
			System.err.println("Cannot append to " + outFileName + "file.");
			System.exit(1);		
		}
		
		return write;
	}
	
	//loads the image
	public Image loadImages(String nameIn)
	{
		Image image = null;
		File imgFile = new File(nameIn);
		try
		{
			image = ImageIO.read(imgFile);
			
		}
		catch(IOException e)
		{
			System.err.println("The image " + nameIn + " cannot be loaded.");
		}
		return image;
	}
}
