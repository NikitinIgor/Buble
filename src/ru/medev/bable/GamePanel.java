package ru.medev.bable;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import java.util.*;                         // ��������� ����� ArrayList

public class GamePanel extends JPanel implements Runnable {

    //Field   ����
    public static int WIDTH = 800;
    public static int HEIGHT = 800;

    public static int mouseX;
    public static int mouseY;
    public static boolean leftMouse;

    private Thread thread;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS;
    private double millisToFps;
    private long timerFPS;
    private int sleepTime;

    public static enum STATES {
        MENUE,
        PLAY
    }

    public static STATES state = STATES.MENUE;

    public static GameBack background;
    public static Player player;
    public static ArrayList<Bullet> bullets;   //zadaem puli
    public static ArrayList<Enemy> enemies;
    public static Wave wave;
    public static Menue menue;

    //Constructor
    public GamePanel() {
        super();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(new Listeners());
        addMouseMotionListener(new Listeners());
        addMouseListener(new Listeners());
    }

    public void gameMethod() {

    }

    //Function

    public void start() {
        thread = new Thread(this);
        thread.start();

    }

    public void run() {

        FPS = 30;
        millisToFps = 1000 / FPS;
        sleepTime = 0;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);  //����� ������
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);           //hints ��� �����������

        Toolkit kit = Toolkit.getDefaultToolkit();
        BufferedImage buffered = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g3 = (Graphics2D) buffered.getGraphics();
        g3.setColor(new Color(255, 255, 255));
        g3.drawOval(0, 0, 4, 4);
        g3.drawLine(2, 0, 2, 4);
        g3.drawLine(0, 2, 4, 2);
        Cursor myCursor = kit.createCustomCursor(buffered, new Point(3, 3), "myCursor");
        g3.dispose();

        leftMouse = false;
        background = new GameBack();
        player = new Player();
        bullets = new ArrayList<Bullet>();   //puli
        enemies = new ArrayList<Enemy>();
        wave = new Wave();
        menue = new Menue();


        while (true) { //TODO States

            this.setCursor(myCursor);

            timerFPS = System.nanoTime();
            if (state.equals(STATES.MENUE)) {
                background.update();
                background.draw(g);
                menue.update();
                menue.draw(g);
                gameDraw();
            }
            if (state.equals(STATES.PLAY)) {
                gameUpdate();
                gameRender();
                gameDraw();

            }


            timerFPS = (System.nanoTime() - timerFPS) / 1000000;
            //System.out.println(timerFPS);
            if (millisToFps > timerFPS) {
                sleepTime = (int) (millisToFps - timerFPS);
            } else sleepTime = 1;
            try {
                thread.sleep(sleepTime);   //TODO FPS
                //System.out.println(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timerFPS = 0;
            sleepTime = 1;
        }

    }

    public void gameUpdate() {
        //Background update
        background.update();

        //Player update
        player.update();

        //Bullets update
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
            boolean remove = bullets.get(i).remove();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }

        //Enemies update
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }
        //Bullets-enemies collide
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                double bx = b.getX();
                double by = b.getY();
                double dx = ex - bx;   //dx differents
                double dy = ey - by;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if ((int) dist <= e.getR() + b.getR()) {
                    e.hit();
                    bullets.remove(j);
                    j--;
                    boolean remove = e.remove();
                    if (remove) {
                        enemies.remove(i);
                        i--;
                        break;
                    }

                }
            }

        }

        //Player-enemy collides
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();

            double px = player.getX();
            double py = player.getY();
            double dx = ex - px;
            double dy = ey - py;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if ((int) dist <= e.getR() + player.getR()) {
                e.hit();
                player.hit();

                boolean remove = e.remove();
                if (remove) {
                    enemies.remove(i);
                    i--;
                }
            }
        }

        //Wave update
        wave.update();

    }

    public void gameRender() {
        //Background draw
        background.draw(g);

        //Player draw
        player.draw(g);

        //Bullets draw, ������ ����
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }

        //Enimies draw
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        //Wave draw
        if (wave.showWave()) {
            wave.draw(g);
        }

    }

    //�������� �����������
    private void gameDraw() {
        Graphics g2 = this.getGraphics();   //����� �������� � ������������ � ������
        g2.drawImage(image, 0, 0, null);
        g2.dispose(); // ������� g2, � g2 ����� �������� ������� �� ��� ����������
    }
}
