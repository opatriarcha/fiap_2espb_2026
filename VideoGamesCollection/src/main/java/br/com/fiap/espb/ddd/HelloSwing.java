package br.com.fiap.espb.ddd;

import javax.swing.*;

public class HelloSwing {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello Swing");
        System.out.println("Hello Swing");
        JLabel label = new JLabel("Hello Swing, a label");
        JButton button = new JButton("Swing Button");
        frame.add(label);
       // frame.add(button);
        frame.setSize(900, 540);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }
}
