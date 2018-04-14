package ru.medev.bable;


import java.awt.*;

public class Menue {

    //Fields
    private int buttonWidth;
    private int buttonHidth;
    private Color color1;
    private String s;
    private int transp = 0;

    //Construction
    public Menue(){
        buttonWidth = 120;
        buttonHidth = 60;

        color1 = Color.white;
        s = "Play!";
    }


    //Functions
    public void  update(){
        if(GamePanel.mouseX > GamePanel.WIDTH/2 - buttonWidth/2 &&
                GamePanel.mouseX < GamePanel.WIDTH + buttonWidth/2 &&
                GamePanel.mouseY > GamePanel.HEIGHT/2 -buttonHidth/2 &&
                GamePanel.mouseY < GamePanel.HEIGHT/2 + buttonHidth/2){
            transp =60;
            if(GamePanel.leftMouse){
                GamePanel.state = GamePanel.STATES.PLAY;
            }
        }else {
            transp = 0;
        }
    }

    public void draw(Graphics2D g){
        g.setColor(color1);
        g.setStroke(new BasicStroke(3)); //контур у кнопки
        g.drawRect(GamePanel.WIDTH/2 - buttonWidth/2,
                GamePanel.HEIGHT/2 - buttonHidth, buttonWidth, buttonHidth);
        g.setColor(new Color(255, 255, 255, transp));
        g.fillRect(GamePanel.WIDTH/2 - buttonWidth/2,
                GamePanel.HEIGHT/2 - buttonHidth, buttonWidth, buttonHidth);
        g.setStroke(new BasicStroke(1));

        g.setColor(color1);
        g.setFont(new Font("Consolas", Font.BOLD,40));
        long length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g.drawString(s, (int)(GamePanel.WIDTH/2 - length/2), (int)(GamePanel.HEIGHT/2 + buttonHidth/400));

    }

}
