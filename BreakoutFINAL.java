import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Name: Samana Shrestha
 * HW 8
 *
 *  When run as a program, this class opens a window on the screen that
 *  shows a red circle that bounces off all 4 walls of the scene and a
 *  moving paddle near the bottom of the scene that follows the mouse 
 *  movements and causes the ball to change direction when its top comes
 *  in contact with the ball. If the paddle misses contacting the ball,
 *  the ball falls off the bottom of the scene.
 * 
 */
public class BreakoutFINAL extends JPanel implements ActionListener {
    
    
    //*************************
    
    
    // Window constants
    public static final int APP_WIDTH = 400;
    public static final int APP_HEIGHT = 750;
    public static final int WINDOW_X = 100;
    public static final int WINDOW_Y = 50;
    public static final Color BACK_GRND_CLR = Color.WHITE;
    public static final String WINDOW_TITLE = "Moving ball bouncing off paddle";
    
    // Brick constants
    public static final int BRICK_HEIGHT = 20;
    public static final int BRICK_WIDTH = 60;
    public static final Color BRICK_COLOR = Color.CYAN;
    
    
    
    //declaring a Brick wall 
    public static Brick[][] wall;
    
    // Ball constants
    public static final int BALL_DIAM = 50;
    public static final Color BALL_COLOR = Color.RED;
    
    // Paddle constants
    public static final Color PADDLE_COLOR = Color.BLACK;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 16;
    public static final int PADDLE_YPOS = APP_HEIGHT - 100;
    public static final int PADDLE_MIDDLE_Y = PADDLE_YPOS+PADDLE_HEIGHT/2;  
    
    // Clock constant
    public static final int CLOCK_SPEED = 20;
    public static Timer frameTimer;
    
    // Position constants
    public static final int START_POS = 200;
    public static final int MOVE_CHANGE = 3;
    
    // Program instance variables
    public int xpos = START_POS, ypos = START_POS;
    public int xChange = MOVE_CHANGE, yChange = MOVE_CHANGE;
    public int paddleX = APP_WIDTH/2 - PADDLE_WIDTH/2;
    
    // number of tries
    int nTries = 0;
    
    //*****************
    //JComponents declaration
    JLabel promptLabel;
    
    //****************
    
    /**
     * This method is called from the paintComponent method. It draws a red circle
     * on a white background.
     */
    public void drawFrame(Graphics g, int width, int height) {
        
        g.setColor(BALL_COLOR);
        
        // Check for ball collision with top, left, and right edges;
        // logically, this occurs when the right edge of the ball is at
        // the right side of the scene and moving to the right, at the left
        // side and moving to the left, at the bottom and moving down, or at
        // the top and moving up.
        if ((((xpos + BALL_DIAM) >= width) && (xChange > 0)) ||
            ((xpos <= 0) && (xChange < 0))) {
            xChange = -xChange;
        }
        if((ypos <= 0) && (yChange < 0)) {
            yChange = -yChange;
        }     
        
        //*****************
        if(((ypos + BALL_DIAM) >= height) && (yChange > 0) && (nTries < 3)) {
            promptLabel.setText("Click to try again!");
        }
        
        //********************
        
        // check for ball hitting paddle
        // the center point of paddle has x coordinate of current
        // panelX + 50
        int paddleCenterX = paddleX + PADDLE_WIDTH/2;
        int paddleY = PADDLE_MIDDLE_Y; // middle of paddle       
        // the center point of the ball has x coordinate of xpos + 25
        // and y coordinate of ypos + 25
        int ballCenterX = xpos + BALL_DIAM/2;
        int ballCenterY = ypos + BALL_DIAM/2;
        // Ball and paddle touch when difference between their centers
        // in the x direction is 75 and the difference between their
        // centers in the y direction is 8 + 25 and ball is moving
        // down. Only changes movement in y direction.
        if ((Math.abs(paddleCenterX - ballCenterX) <= PADDLE_WIDTH/2 + BALL_DIAM/2)  &&
            (Math.abs(paddleY - ballCenterY) <= PADDLE_HEIGHT/2 + BALL_DIAM/2) && 
            (yChange > 0)) {
            yChange = -yChange;
        }
        
        //********************************
        
        
        // check for ball hitting brick
        // the center point of brick has x coordinate of current
        // panelX + 50
        // int brickCenterX = paddleX + PADDLE_WIDTH/2;
        breakBricks();
        //int paddleY = PADDLE_MIDDLE_Y; // middle of paddle 
        
        // Ball and paddle touch when difference between their centers
        // in the x direction is 75 and the difference between their
        // centers in the y direction is 8 + 25 and ball is moving
        // down. Only changes movement in y direction.
        if ((Math.abs(paddleCenterX - ballCenterX) <= PADDLE_WIDTH/2 + BALL_DIAM/2)  &&
            (Math.abs(paddleY - ballCenterY) <= PADDLE_HEIGHT/2 + BALL_DIAM/2 && 
             (yChange > 0))) {
            yChange = -yChange;
        }
        
        
        
        
        
        //********************************
        
        
        // to move ball position, add the value of xChange and
        // yChange to xpos and ypos
        xpos = xpos + xChange;
        ypos = ypos + yChange;
        g.fillOval(xpos, ypos, BALL_DIAM, BALL_DIAM);
        
        // paint paddle onto scene
        g.setColor(PADDLE_COLOR);
        g.fillRect(paddleX, 
                   PADDLE_YPOS,
                   PADDLE_WIDTH,
                   PADDLE_HEIGHT);                        
    }
    
    /**
     * This method is called every time an ActionEvent is fired, in this case
     * every time the clock ticks
     */
    public void actionPerformed(ActionEvent evt) {
        // The call to repaint calls paintComponent of the JPanel
        repaint();
    } // end actionPerformed
    
    /**
     * This method is part of every JPanel class. It is called when the 
     * program starts and every time repaint() is called in the program.
     */
    protected void paintComponent(Graphics g) {
        // call paintComponent in parent class, sending in the Graphics object
        super.paintComponent(g);
        // call drawing method, passing in the Graphics object g and 3 ints
        drawFrame(g, this.getWidth(), this.getHeight());
        // call drawwall
        drawWall(g);
    }// end paintComponent
    
    
    //***************   
    public class Brick {
        
        
        // Brick variable
        public int bXPos, bYPos;
        
        public Brick (int xpos, int ypos){
            bXPos = xpos;
            bYPos = ypos;
        }
    }
    
    public void makeWall(int m, int n){
        wall = new Brick[m][n];
        
        int k = 5;
        int l = 100;
        
        for (int i = 0; i < wall.length; i++){
            for (int j = 0; j < wall[i].length; j++){
                wall[i][j] = new Brick(k, l);
                k += 62;
            }
            k = 5;
            l += 22;
        }
        //System.out.println("Done making wall!!");
    }
    
    public static void drawWall(Graphics g){
        for (int i = 0; i < wall.length; i++){
            for (int j = 0; j < wall[i].length; j++){
                g.setColor(BRICK_COLOR);
                if (wall[i][j] != null) {
                    g.fillRect(wall[i][j].bXPos, 
                               wall[i][j].bYPos,
                               BRICK_WIDTH,
                               BRICK_HEIGHT);
                }}
        }
    }
    /*
     * Execution starts here. The main method sets up the JFrame and adds
     * this JPanel to the JFrame. Also declares, instantiates and starts a 
     * Timer whose ticks will drive the animation.
     */
    public static void main(String[] args) {
        
        JFrame window = new JFrame(WINDOW_TITLE);
        
        // drawing Area is a JPanel:
        BreakoutTry drawingArea = new BreakoutTry();
        drawingArea.setBackground(BACK_GRND_CLR);
        drawingArea.makeWall(3, 6);
        
        // These statements set up JFrame
        window.setContentPane(drawingArea);
        window.pack();
        window.setLocation(WINDOW_X,WINDOW_Y);
        window.setSize(APP_WIDTH,APP_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
        
        // A Timer object will fire an ActionEvent every CLOCK_SPEED
        // milliseconds. This ActionEvent is caught by the actionPerformed
        // method in this JPanel.
        frameTimer = new Timer(CLOCK_SPEED, drawingArea);
        
        // Timer must be started, after which a new ActionEvent will be
        // generated each time clock is advanced.
        // frameTimer.start();
        
    } // end main
    
    
    
    /*
     * Constructor needed to set up MouseMotionListener on this 
     * JPanel.
     */
    public BreakoutTry() {
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        
        //*************************
        addMouseListener(new MouseAdapter() {
            public void mouseClicked (MouseEvent me){
                nextTry();
                frameTimer.start();
            }
        });
        
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved (MouseEvent mme){
                movePaddle(mme.getX());
            }
            public void mouseDragged(MouseEvent mme) {};
        });
        
        //instantiating promptLabel to prompt for filename
        promptLabel = new JLabel("Click to start game! You have 2 lives!");
        
        this.add(promptLabel);
        
        //make wall
        
        
        //*************************
    }
    
    //************************************
    //nextTry method to give user new chance (up to 3) when ball falls
    
    public void nextTry(){
        nTries++;
        if (nTries < 3){
            xpos = START_POS;
            ypos = START_POS;
            //frameTimer.start();
        }
        else{
            System.out.println("nTries = " + nTries);
            promptLabel.setText("You are out of lives, you lose!");
        }
    }
    
    //************************************
    
    /*
     * method called when MouseEvent fired by moving mouse
     */
    public void movePaddle(int x) {
        if (!((x + PADDLE_WIDTH) >= this.getWidth())) {
            paddleX = x;
            //repaint();
        }
    }
    
    
    public void breakBricks() {
        
        // the center point of the ball has x coordinate of xpos + 25
        // and y coordinate of ypos + 25
        int ballCenterX = xpos + BALL_DIAM/2;
        int ballCenterY = ypos + BALL_DIAM/2;
        // Ball and bricks touch when difference between their centers
        // in the x direction is <= half the ball diameter + half the brick width
        // and the difference between their centers in the y direction is <= half 
        // the ball diameter + half the brick height
        
        for (int i = 0; i < wall.length; i++){
            for (int j = 0; j < wall[i].length; j++){
                if (wall[i][j] != null) {
                    //System.out.println("Checking brick "+wall[i][j].bXPos+", "+wall[i][j].bYPos);
                    if (((Math.abs(wall[i][j].bXPos + BRICK_WIDTH/2 - ballCenterX)) 
                             <= (BRICK_WIDTH/2 + BALL_DIAM/2))  &&
                        ((Math.abs(wall[i][j].bYPos + BRICK_HEIGHT/2 - ballCenterY)) 
                             <= (BRICK_HEIGHT/2 + BALL_DIAM/2))) {
                        wall[i][j] = null;
                        yChange = -yChange;
                    }}}}
    }
} // end class
