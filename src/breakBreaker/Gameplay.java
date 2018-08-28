package breakBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private boolean win = false;
	private int score = 0;
	
	public int level=1;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private int tmpXdir, tmpYdir;
	
	private MapGenerator map;
	
	public Gameplay() {
		map = new MapGenerator(3,7,level);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
		
	}
	
	public void paint (Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 580, 30);
		
		//level
		g.setColor(Color.blue);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString("level:"+level, 620, 30);
		
		//the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		if(totalBricks <= 0) {
			play=false;
			win=true;
			ballXdir = 0;
			ballYdir = 0;
			if(level==3) {
				g.setColor(Color.red);
				g.setFont(new Font("serif", Font.BOLD, 30));
				g.drawString("You Won!", 260, 300);
				g.setFont(new Font("serif", Font.BOLD, 20));
				g.drawString("Press ENTER to Restart", 230, 350);
			}
			else {
				g.setColor(Color.red);
				g.setFont(new Font("serif", Font.BOLD, 30));
				g.drawString("You passed level "+level, 230, 300);
				g.setFont(new Font("serif", Font.BOLD, 20));
				g.drawString("Press SPACE to continue", 240, 330);
				g.drawString("Press ENTER to Restart", 241, 360);
			}
		}
		
		if(ballposY > 570) {
			play=false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Scores: "+score, 190, 300);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press ENTER to Restart", 230, 350);
		}
		
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
				ballYdir = -ballYdir;
			}
			
			A: for(int i=0; i<map.map.length;i++) {
				for(int j=0; j<map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX =j*map.brickWidth +80;
						int brickY =i*map.brickHeight +50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrcikValue(map.map[i][j]-1, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}
							else {
								ballYdir = -ballYdir;
							}
							break A;
							
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
		}
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {	}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 600)
				playerX = 600;
			else
				moveRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 10)
				playerX = 10;
			else
				moveLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_P) {
			if(ballXdir != 0) {
				tmpXdir=ballXdir;
				tmpYdir=ballYdir;
				ballXdir=0;
				ballYdir=0;
			}
			else {
				ballXdir=tmpXdir;
				ballYdir=tmpYdir;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				level=1;
				delay=8;
				map = new MapGenerator(3, 7,level);
				
				repaint();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(!play&&win) {
			play = true;
			win=false;
			ballposX = 120;
			ballposY = 350;
			ballXdir = -1;
			ballYdir = -2;
			playerX = 310;
			delay -=2;
			level++;
			if (level==2) {
				totalBricks = 31;
				map=new MapGenerator(4, 7, level);
				map.map[1][3]=2;
				map.map[3][0]=2;
				map.map[3][6]=2;
			}
			else if(level==3) {
				totalBricks = 47;
				map=new MapGenerator(5, 7, level);
				map.map[0][2]=2;
				map.map[0][3]=2;
				map.map[0][4]=2;
				map.map[1][1]=2;
				map.map[1][5]=2;
				map.map[2][0]=2;
				map.map[2][6]=2;
				map.map[3][1]=2;
				map.map[3][5]=2;
				map.map[4][2]=2;
				map.map[4][3]=2;
				map.map[4][4]=2;
			}
			}
		}
		
	}
	
	public void moveRight() {
		play=true;
		playerX+=20;
	}
	public void moveLeft() {
		play=true;
		playerX-=20;
	}

}
