import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image flappybirdImg;
    Image bottomPipeImg;
    Image topPipeImg;

    //Flappy Bird
    int flappyBirdX = boardWidth/8;
    int flappyBirdY = boardHeight/2;
    int flappybirdWidth = 34;
    int flappybirdHeight = 24;
    
    class Bird{
        int x = flappyBirdX;
        int y = flappyBirdY;
        int width = flappybirdWidth;
        int height = flappybirdHeight;
        Image img;

        Bird (Image img){
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe (Image img) {
            this.img = img;
        }
    }

    //Game Logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;


    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //Load Images  
        backgroundImg = new ImageIcon(getClass().getResource("./background.png")).getImage();
        flappybirdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();

        //Bird
        bird = new Bird(flappybirdImg);
        pipes = new ArrayList<Pipe>();
        
        //placePipes Timer
        placePipesTimer = new Timer(1500,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes(); 
            }
        });
        placePipesTimer.start();

        // Game Timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = boardHeight/4;
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;  
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);                      
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw (Graphics g) {
           //Background 
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //FlappyBird
        g.drawImage (bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //Pipes
        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //Score
        g.setColor (Color.white);
        g.setFont(new Font( "Arial", Font.BOLD, 32));

        if (gameOver) {
            g.drawString("Game Over : " + String.valueOf((int) score), 10, 35);            
        }else {
            g.drawString("Score : " + String.valueOf((int) score), 10, 35);  
        }
        
    }

    public void move (){
        //FlappyBird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //Pipes
        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if (collision (bird, pipe)) {
                gameOver = true;                                                               
            }
        }

        if (bird.y > boardHeight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();        
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collision (Bird a, Pipe b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;            
        }
    }

    // Not Needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}    
}

    
 
