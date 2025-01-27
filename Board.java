//(THE MOTHERLOAD OF) IMPORTS
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.io.*;
import sun.audio.*;



/*THE GAME BOARD*/
public class Board extends JPanel implements ActionListener {

    //CONSTANTS
    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 700;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    //GLOBAL VARIABLES
    private int snake;
    private int oreo_x;
    private int oreo_y;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private boolean playAgain = true;

    //GLOBAL  OBJECTS
    private Timer timer;
    private Image ball;
    private Image oreo;
    private Image head;

    /*CONSTRUCTOR*/
    public Board() {
        initBoard();
    }

    /*GUI SPECS*/
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();

    }

    /*THE SNAKE PIECES' IMAGES*/
    private void loadImages() {
        ImageIcon iid = new ImageIcon("face.png"); //I would change to Adrian's face XD
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("oreo.png");
        oreo = iia.getImage();

        ImageIcon iih = new ImageIcon("face.png");
        head = iih.getImage();

    }

    /*MOVING THE SNAKE*/
    private void initGame() {
        snake = 4;

        for (int z = 0; z < snake; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateoreo();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g)  {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        if (inGame) {
            score(g);
            g.drawImage(oreo, oreo_x, oreo_y, this);
            for (int z = 0; z < snake; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else { /*exe game overs screen*/
            gameOver(g);
            score(g);
        }
    }

    /*DRAW NEW OREO*/
    private void locateoreo() {
        int r = (int) (Math.random() * RAND_POS);
        oreo_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        oreo_y = ((r * DOT_SIZE));

    }

    /*MOVE NEW OREO*/
    private void move(){

        for (int z = snake; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }

        if (right) {
            x[0] += DOT_SIZE;
        }


        if (up) {
            y[0] -= DOT_SIZE;
        }


        if (down) {
          y[0] += DOT_SIZE;
        }
    }

    /*CHECKS IF GAME OVER*/
    private void checkoreo() {
        if ((x[0]<=(oreo_x +20) && x[0] >= (oreo_x - 20)) && (y[0]<=(oreo_y +20) && y[0] >= (oreo_y -20))) {
            snake++;
            locateoreo();
        }
    }




/*CHECK IF INTO WALL*/
    private void checkCollision() {
        for (int z = snake; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= B_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    /*MUSIC FILES
    public void playChomp() throws Exception{
        String file = "pacman_chomp.wav";
        InputStream in = new FileInputStream(file);
        AudioStream audioStream = new AudioStream(in);
        AudioPlayer.player.start(audioStream);

    }
    public void playDeath() throws Exception{
        String file = "pacman_death.wav";
        InputStream in = new FileInputStream(file);
        AudioStream audioStream = new AudioStream(in);
        AudioPlayer.player.start(audioStream);

    }
    */
    /*GAME OVER SCREEN*/
    private void gameOver(Graphics g){
        String msg = "You Suck";
        Font small = new Font("Helvetica", Font.BOLD, 25);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);

    }
   private void score(Graphics g) {
        String msg = "Score: " + (snake-3);
        Font small = new Font("Helvetica", Font.BOLD, 25);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)-10), B_HEIGHT - 10);
    }


/*GAME RUNNER*/
    public void actionPerformed(ActionEvent e){

        if (inGame) {
            checkoreo();
            checkCollision();
            move();
        }
        repaint();

    }

/*KEYBOARD JUNK*/
    private class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;

            }

            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
