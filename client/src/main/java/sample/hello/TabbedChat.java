package sample.hello;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roy on 02/06/2017.
 */
public class TabbedChat extends javax.swing.JFrame{
    /*
    /**
     * Creates new form chatTabbed
     */
    ActorSystem system;
    ActorRef client=null;
    Map<String,JPanel> rooms;
    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField roomNameInput;
    private javax.swing.JButton joinRoomBtn;
    private javax.swing.JTextField userNameInput;
    // End of variables declaration


    public TabbedChat() {
        rooms=new HashMap<String,JPanel>();
        system = ActorSystem.create("HelloWorldSystem");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        joinRoomBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        userNameInput = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        roomNameInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        joinRoomBtn.setText("join");
        joinRoomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinRoomBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("user name: ");

        userNameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userNameInputActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Channel Name:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Welcome To Our MIIRC");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(171, 171, 171)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(roomNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(joinRoomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(userNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(223, 223, 223)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(212, 212, 212)
                                                .addComponent(jLabel2))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(116, 116, 116)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel3)
                                .addGap(65, 65, 65)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(userNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(roomNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(joinRoomBtn)
                                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Login", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>

    private void userNameInputActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void joinRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (userNameInput.getText().trim().isEmpty() || roomNameInput.getText().trim().isEmpty())
            return;

        String username = userNameInput.getText();
        String channel = roomNameInput.getText();
        if(client == null)
            client = system.actorOf(Props.create(Client.class, username, this), "ClientUserActor");
        ActorSelection serv = system.actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server");
        serv.tell(new Message_JoinClient(username, channel), client);
    }

    public void addTab(String name, JPanel panel) { // Takes in a name for the tab and
        // a panel for the tab to display when clicked


        jTabbedPane1.addTab(name, null, panel, name); // The first name is the name of
        // the tab and second one is what
        // is display in the little popup
        // thing when you hover the mouse
        // over the tab
    }

    public void openNewChanel(String roomName,JPanel p){
        if(rooms.containsKey(roomName))
            return;
        rooms.put(roomName,p);
        addTab(roomName,p);

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TabbedChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TabbedChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TabbedChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TabbedChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TabbedChat().setVisible(true);
            }
        });
    }




}
