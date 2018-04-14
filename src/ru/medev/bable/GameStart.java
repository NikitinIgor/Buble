package ru.medev.bable;

import javax.swing.JFrame;

public class GameStart {
	
	public static void main(String[] args){
		
		GamePanel panel = new GamePanel();              //для запуска потока 
		JFrame startFrame = new JFrame("Buble");
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		startFrame.setContentPane(panel);
		startFrame.pack();
		startFrame.setLocationRelativeTo(null);
		startFrame.setVisible(true);
		panel.start();                        //запуск потока
	}

}
