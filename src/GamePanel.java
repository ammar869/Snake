import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.util.Random;

//pause
//music
//border

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;                                            // We are setting a fixed unit/grid size of 25 pixels.
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE  ; // THIS WILL ENSURE HOW MANY OBJECTS WE CAN CREATE IN IT
    static final int DELAY = 100;
                                                                                // we are now making arrays which will hold all of the coordinates for all of the body Parts
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int [GAME_UNITS];

    int bodyparts = 6 ;                                                         // initial amount of body part
    int applesEaten = 0;
    int appleX;                                                                 // it is basically the X coordinate where the apple is located ... Moreover it will be Randomly
    int appleY;
    char direction = 'R';
    boolean running = true;
    Timer timer;
    Random random ;
    boolean paused;


    GamePanel() {
        random = new Random();
                                                                                            // Set the preferred size of the panel to the specified screen width and height
                                                                                            // This suggests to the layout manager the ideal dimensions for the component
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));                  //here this is refered to the Jframe and the Jpanel
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter() );
        startGame();


    }
    public void startGame() {
        for (int i = 0; i < bodyparts; i++) {
            x[i] = 100 - i * UNIT_SIZE;
            y[i] = 100;
        }
        newApple();
        running = true;
        timer = new Timer(DELAY, this);                                                 // this is used bec we are using Action Listerner interface
        timer.start();

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g) {
// For grid line drawn
        if(running) {
            //            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            //                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            //                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            //            }
            if (paused) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Ink Free", Font.BOLD, 75));
                g.drawString("PAUSED", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2);
            } else {
                g.setColor(Color.RED);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);                                             //THIS IS HOW LARGE THE APPLE IS

                //we need to draw the head of the snake and body
                for (int i = 0; i < bodyparts; i++) {
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(45, 100, 0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }

                g.setColor(Color.RED);
                g.setFont(new Font("Bauhaus 93", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score" + applesEaten)) / 2, g.getFont().getSize());
            }
        }
        else{
                gameOver(g);
            }




    }
    public void newApple() {
            appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;               // we cast it as int so it doesnot brak this program
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;

    }

                                                                                                // We are just creating the bodypart movment
    public void move(){
        for(int i = bodyparts; i >0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
    case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
    case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
    case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
    case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
}



    }
    public void checkApple(){
        if(appleX == x[0] && appleY == y[0]){
            bodyparts++;
            applesEaten++;
            newApple();
            // Increase speed every 5 apples
            if (applesEaten % 5 == 0) {
                timer.setDelay(Math.max(DELAY - (applesEaten * 5), 50));  // Don't go below 50ms
            }
        }

    }
    public void checkCollisions(){
        // this stops working when head colloids with the body
        for(int i = bodyparts; i >0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])) {          //here x[0] is head of the snake
                    running = false;
            }
        }
        // check if head touches left border
        if(x[0]< 0){
            running = false;
        }
        // check if head touches right border
        if(x[0]>=SCREEN_WIDTH){
            running = false;
        }
        // check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        // check if head touches bottom border
        if(y[0]>=SCREEN_HEIGHT){
            running = false;
        }

    if(!running ){
        timer.stop();
    }


    }
    public void gameOver(Graphics g){
        //Display Score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score"+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score"+applesEaten))/2, g.getFont().getSize());
        // GameOver text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

    }


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (running && !paused) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }
    public class MyKeyAdapter extends KeyAdapter {//override keyPressed to detect when a key is pressed down.
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_SPACE:
                    paused = !paused;  // Toggle pause
                    break;
            }

        }
    }

    

}
