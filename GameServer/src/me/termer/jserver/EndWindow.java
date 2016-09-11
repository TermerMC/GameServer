package me.termer.jserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class EndWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public EndWindow() {
		super("");
		JButton btn = new JButton("End");
    	btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
    	});
    	setUndecorated(true);
    	getContentPane().add(btn);
    	setSize(100, 60);
    	setVisible(true);
	}
}
