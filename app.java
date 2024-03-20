import javax.swing.*;

public class app {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;
        
        JFrame frame = new JFrame("Flappy Bird");
       // frame.setVisible(true);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        
        frame.setVisible(true);

        
    }    
}