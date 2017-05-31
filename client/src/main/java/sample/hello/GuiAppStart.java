package sample.hello;

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Created by Bella on 5/27/2017.
 */


public class GuiAppStart {


    public GuiAppStart() {
        JFrame guiFrame = new JFrame();

        //make sure the program exitsz when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Chat");
        guiFrame.setSize(500, 450);

        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);


        //The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("User name:");
        JTextField username = new JTextField();

        JLabel comboLb2 = new JLabel("Channel:");
        JTextField channel = new JTextField();

        JButton start = new JButton("start chating");
        comboPanel.add(comboLbl);
        comboPanel.add(username);
        comboPanel.add(comboLb2);
        comboPanel.add(channel);
        comboPanel.add(start);

        guiFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // close sockets, etc
            }
        });

        //The ActionListener class is used to handle the
        //event that happens when the user clicks the button.
        //As there is not a lot that needs to happen we can
        //define an anonymous inner class to make the code simpler.
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //When the start buttun is pressed
                //the setVisible value of the listPanel and
                //comboPanel is switched from true to
                //value or vice versa.
                String usermaneString = username.getText();
                String channelString = channel.getText();
                Message_JoinClient m= new Message_JoinClient(usermaneString,channelString);


            }
        });

        //The JFrame uses the BorderLayout layout manager.
        //Put the two JPanels and JButton in different areas.
//        guiFrame.add(comboPanel, BorderLayout.NORTH);
//        guiFrame.add(listPanel, BorderLayout.CENTER);
//        guiFrame.add(vegFruitBut,BorderLayout.SOUTH);

        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }
    private class CloseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //DO SOMETHING
            System.exit(0);
        }
    }

}

