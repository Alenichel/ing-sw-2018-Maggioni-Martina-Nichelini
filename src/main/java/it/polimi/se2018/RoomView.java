package it.polimi.se2018;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class RoomView implements Observer {

    private RoomController rc;
    protected JTextField textField;

    public RoomView(RoomController rc) {
        this.rc = rc;
        System.out.println(rc.getState());
        JFrame frame = new JFrame();
        this.textField = new JTextField("ciao", 2);
        textField.setText(rc.getState().toString());
        textField.setBackground(Color.black);
        textField.setForeground(Color.white);
        textField.setBounds(10,10,400, 50);
        JButton button = new JButton("Start Game");
        button.setBounds(50,50,90, 50);
        button.addActionListener(new ExampleAction());
        frame.add(button);
        frame.add(textField);
        frame.setSize(300,200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ExampleAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            rc.setState(new RoomStartedState());
            System.out.println(rc.getState());
            textField.setText(rc.getState().toString());
        }
    }

    @Override
    public void update(Observable o, Object arg){
        ;
    }
}
