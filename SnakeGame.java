import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int GAME_SPEED = 100;

    private int[] x, y;
    private int length;
    private int foodX, foodY;
    private char direction;

    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        x = new int[GRID_SIZE * GRID_SIZE];
        y = new int[GRID_SIZE * GRID_SIZE];
        length = 1;
        direction = 'R'; // Initially moving to the right

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(GAME_SPEED, this);
        timer.start();

        placeFood();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }

    private void placeFood() {
        Random rand = new Random();
        foodX = rand.nextInt(GRID_SIZE) * TILE_SIZE;
        foodY = rand.nextInt(GRID_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
        }

        checkCollision();
        checkFood();
    }

    private void checkCollision() {
        if (x[0] < 0 || x[0] >= getWidth() || y[0] < 0 || y[0] >= getHeight()) {
            gameOver();
        }

        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameOver();
            }
        }
    }

    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            placeFood();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, getWidth(), getHeight());

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < length; i++) {
            g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
