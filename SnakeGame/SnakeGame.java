package Game.SnakeGame;/*
import java.util.Scanner;//类的导入
@SuppressWarnings({"all"})//抑制所有警告
@Deprecated//表示该类/方法/..过时但仍可用,升级过渡使用
@Override//校验say()是否为重写方法*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 500;
    private final int HEIGHT = 500;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;

    private final ArrayList<Integer> snakeX = new ArrayList<>();
    private final ArrayList<Integer> snakeY = new ArrayList<>();
    private int foodX;
    private int foodY;
    private int length = 3;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        // 初始化蛇的位置
        snakeX.clear();
        snakeY.clear();
        for(int i = 0; i < length; i++) {
            snakeX.add(100 - i * UNIT_SIZE);
            snakeY.add(100);
        }

        generateFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void generateFood() {
        foodX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            // 画食物
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // 画蛇
            for(int i = 0; i < snakeX.size(); i++) {
                if(i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void move() {
        // 移动身体
        for(int i = snakeX.size()-1; i > 0; i--) {
            snakeX.set(i, snakeX.get(i-1));
            snakeY.set(i, snakeY.get(i-1));
        }

        // 移动头部
        switch(direction) {
            case 'U': snakeY.set(0, snakeY.get(0) - UNIT_SIZE); break;
            case 'D': snakeY.set(0, snakeY.get(0) + UNIT_SIZE); break;
            case 'L': snakeX.set(0, snakeX.get(0) - UNIT_SIZE); break;
            case 'R': snakeX.set(0, snakeX.get(0) + UNIT_SIZE); break;
        }
    }

    public void checkFood() {
        if(snakeX.get(0) == foodX && snakeY.get(0) == foodY) {
            snakeX.add(snakeX.get(snakeX.size()-1));
            snakeY.add(snakeY.get(snakeY.size()-1));
            length++;
            generateFood();
        }
    }

    public void checkCollision() {
        // 检查是否撞到自己
        for(int i = 1; i < snakeX.size(); i++) {
            if(snakeX.get(0).equals(snakeX.get(i)) && snakeY.get(0).equals(snakeY.get(i))) {
                running = false;
                break;
            }
        }

        // 检查是否撞墙
        if(snakeX.get(0) < 0 || snakeX.get(0) >= WIDTH ||
                snakeY.get(0) < 0 || snakeY.get(0) >= HEIGHT) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over"))/2, HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if(direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if(direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 'U') direction = 'D';
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("贪吃蛇");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}