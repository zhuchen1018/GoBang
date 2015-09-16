
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GUI extends JFrame implements MouseListener, Runnable {
  Thread user = null;
  Server server = null;
  JButton b1 = null;
  JButton b2 = null;
  String s1 = "黑方";
  String s2 = "白方";
  String s3 = "黑方获胜";
  String s4 = "白方获胜";
  String s5 = "平局";
  String s6 = "禁手,黑负";
  String s = this.s1;
  BufferedImage backbuffer = null;
  Graphics2D g2d = null;
  Image back = null;
  Image black = null;
  Image white = null;
  int width;
  int height;
  int current = 1;
  int role = 1;
  int[][] position = null;
  int winner = 0;
  boolean bannedPos = false;

  PrintStream out = null;
  int nextX;
  int nextY;
  boolean next = false;

  public static void main(String[] args) throws IOException {
    GUI user = new GUI();
  }

  public GUI() throws IOException {
    //this.out = new PrintStream(new File("record.txt"));

    setTitle("五子棋");
    setSize(450, 380);
    setDefaultCloseOperation(3);
    setResizable(false);
    setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 500) / 2, 
      (Toolkit.getDefaultToolkit().getScreenSize().height - 500) / 2);
    setVisible(true);
    try {
      File parent = new File(new File("").getAbsolutePath());
      this.back = ImageIO.read(new File(parent, "images/board.jpg"));
      this.black = ImageIO.read(new File(parent, "images/black.png"));
      this.white = ImageIO.read(new File(parent, "images/white.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.width = this.back.getWidth(null);
    this.height = this.back.getHeight(null);
    this.backbuffer = new BufferedImage(this.width, this.height, 1);
    this.g2d = this.backbuffer.createGraphics();
    this.g2d.drawImage(this.back, 0, 0, this);
    for (int i = 0; i < 15; i++)
      g2d.drawString(new Integer(i).toString(), 5 + i * 23, 12);
    for (int i = 1; i < 15; i++)
      g2d.drawString(new Integer(i).toString(), 2, 18 + 23 * i);
    this.b1 = new JButton("用户先手");
    this.b1.setBounds(this.width - 5, 50, 100, 30);
    this.b2 = new JButton("电脑先手");
    this.b2.setBounds(this.width - 5, 80, 100, 30);
    Container cp = getContentPane();
    cp.setLayout(null);
    cp.add(this.b1);
    cp.add(this.b2);
    this.b1.addMouseListener(this);
    this.b2.addMouseListener(this);
    addMouseListener(this);
    this.position = new int[15][];
    for (int i = 0; i < 15; i++) {
      this.position[i] = new int[15];
    }
    this.user = new Thread(this);
    this.user.start();

    repaint();
    this.server = new Server(this, this.role);
    this.server.start();
  }

  public void initializeSet() throws IOException {
	if (! new File("initialize.txt").exists())
		return;
    Scanner scanner = new Scanner(new File("initialize.txt"));
    while (scanner.hasNext()) {
      int x = scanner.nextInt();
      int y = scanner.nextInt();
      int player = scanner.nextInt();
      this.position[x][y] = player;
      this.current = (3 - player);
      if (player == 1)
        this.g2d.drawImage(this.black, 1 + 23 * x, 1 + 23 * y, this);
      else
        this.g2d.drawImage(this.white, 1 + 23 * x, 1 + 23 * y, this);
      if ((!scanner.hasNext()) && (player == this.role))
        this.server.set(x, y, player);
      else
        this.server.initializeSet(x, y, player);
    }
  }

  public void run() {
    Thread t = Thread.currentThread();
    while (t == this.user) {
      repaint();
      try {
        Thread.sleep(500L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (this.winner != 0) {
        if (this.winner == 1)
          this.s = this.s3;
        else if (this.winner == 2)
          this.s = this.s4;
        else
          this.s = this.s5;
        if (this.bannedPos)
          this.s = this.s6;
      }
    }
  }

  public void stop()
  {
    this.user = null;
  }

  public void paint(Graphics g) {
    g.drawImage(this.backbuffer, 0, 30, this);
    BufferedImage b = new BufferedImage(100, 50, 1);
    Graphics g2d = b.createGraphics();
    g2d.fillRect(0, 0, 100, 50);
    if (this.s != null) {
      g2d.setFont(new Font("黑体", 1, 20));
      g2d.setColor(Color.black);
      g2d.drawString(this.s, 10, 30);
    }
    g.drawImage(b, this.width, 30, this);
    b = new BufferedImage(100, this.height - 110, 1);
    g2d = b.createGraphics();
    g2d.fillRect(0, 0, 100, this.height - 110);
    g.drawImage(b, this.width, 140, this);
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  @SuppressWarnings("deprecation")
  public void mousePressed(MouseEvent e)
  {
    if (e.getSource() == this.b1) {
      this.role = 1;
      this.current = 1;
      for (int i = 0; i < 15; i++)
        for (int j = 0; j < 15; j++)
          this.position[i][j] = 0;
      this.g2d.drawImage(this.back, 0, 0, this);
      for (int i = 0; i < 15; i++)
        g2d.drawString(new Integer(i).toString(), 5 + i * 23, 12);
      for (int i = 1; i < 15; i++)
        g2d.drawString(new Integer(i).toString(), 2, 18 + 23 * i);
      this.server.stop();
      try {
        this.server = new Server(this, this.role);
        this.server.start();
        this.winner = 0;
        this.bannedPos = false;
        initializeSet();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
      return;
    }
    if (e.getSource() == this.b2) {
      this.role = 2;
      this.current = 2;
      for (int i = 0; i < 15; i++)
        for (int j = 0; j < 15; j++)
          this.position[i][j] = 0;
      this.position[7][7] = 1;
      this.g2d.drawImage(this.back, 0, 0, this);
      for (int i = 0; i < 15; i++)
        g2d.drawString(new Integer(i).toString(), 5 + i * 23, 12);
      for (int i = 1; i < 15; i++)
        g2d.drawString(new Integer(i).toString(), 2, 18 + 23 * i);
      this.g2d.drawImage(this.black, 162, 162, this);
      this.server.stop();
      try {
        this.server = new Server(this, this.role);
        this.server.start();
        this.winner = 0;
        this.bannedPos = false;
        initializeSet();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
      return;
    }
    if (e.getSource() == this) {
      if (this.winner != 0)
        return;
      if (this.current != this.role)
        return;
      if ((e.getX() >= 3) && (e.getX() <= 345) && (e.getY() >= 33) && (e.getY() <= 375)) {
        int x = (e.getX()-13)%23;
        if (x <= 10)
          x = (e.getX()-13-x)/23;
        else if (x >= 13)
          x = (e.getX()-13-x)/23+1;
        else
          x = -1;
        int y = (e.getY()-43)%23;
        if (y <= 10)
          y = (e.getY()-43-y)/23;
        else if (y >= 13)
          y = (e.getY()-43-y)/23+1;
        else
          y = -1;
        if ((x != -1) && (y != -1) && (this.position[x][y] == 0)) {
          //out.println(x + "\t" + y + "\t" + this.role);
          this.server.set(x, y, this.current);
          if (this.current == 1) {
            this.g2d.drawImage(this.black, 1+23*x, 1+23*y, this);
            this.s = this.s2;
          }
          else {
            this.g2d.drawImage(this.white, 1+23*x, 1+23*y, this);
            this.s = this.s1;
          }
          repaint();
          this.position[x][y] = this.current;
          this.current = (3 - this.current);
          if (this.next) {
            this.next = false;
            computer(this.nextX, this.nextY);
          }
        }
      }
    }
  }

  public void setBanned(int player) {
    this.bannedPos = true;
    this.winner = player;
  }

  public void setWinner(int player) {
    this.winner = player;
  }

  public void computer(int x, int y) {
    //out.println(x + "\t" + y + "\t" + (3 - this.role));
    if (this.current == this.role) {
      this.next = true;
      this.nextX = x;
      this.nextY = y;
      return;
    }
    if (this.current == 1) {
      this.g2d.drawImage(this.black, 1+23*x, 1+23*y, this);
      this.s = this.s2;
    }
    else {
      this.g2d.drawImage(this.white, 1+23*x, 1+23*y, this);
      this.s = this.s1;
    }
    repaint();
    this.position[x][y] = this.current;
    this.current = (3 - this.current);
  }

  public void mouseReleased(MouseEvent e) {

  }
}
