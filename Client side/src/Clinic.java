import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Clinic{
    private JPanel main;
    private JPanel topPanel;
    private JPanel downPanel;
    private JLabel name;
    private JButton signUp;
    private JButton signIn;
    private JPanel afterSignUp;
    private JLabel signUpAs;
    private JRadioButton doctor;
    private JRadioButton patient;
    private JPanel doctorSignUp;
    private JTextField doctorNameField;
    private JTextField doctorUsernameField;
    private JTextField doctorPasswordField;
    private JButton doctorSignUpButton;
    private JLabel doctorName;
    private JLabel doctorUserName;
    private JLabel doctorPassword;
    private JPanel doctorStatus;
    private JTextField doctorStatusField;
    private JButton doctorSignUpExit;
    private JPanel signInPanel;
    private JTextField patientUsernameField;
    private JPasswordField patientPasswordField;
    private JButton patientSignInbutton;
    private JLabel patientUserName;
    private JLabel patientPassword;
    private JLabel signINAs;
    private JRadioButton doctorRadioButton;
    private JRadioButton patientRadioButton;
    private JPanel patientStatus;
    private JTextField patientStatusField;
    private JButton patientSignInExit;
    private JRadioButton invisible;
    private JRadioButton secondInvisible;
    private JPanel afterSignIn;
    private JButton exitSignIn;
    private JButton sendMessage;
    private JButton sent;
    private JButton accountInformation;
    private JButton inbox;
    private JPanel editInformation;
    private JLabel editName;
    private JLabel editPassword;
    private JLabel editUsername;
    private JTextField editNameField;
    private JTextField editUsernameField;
    private JTextField editPasswordField;
    private JButton save;
    private JPanel sendMesagePanel;
    private JTextArea composeArea;
    private JTextField toField;
    private JButton sendButton;
    private JLabel to;
    private JLabel compose;
    private JPanel emailStatus;
    private JButton exitEmail;
    private JPanel inboxPnl;
    private JTextArea inboxArea;
    private JButton exitSign;
    private JPanel sentPnl;
    private JTextArea outBoxArea;
    private JButton sentExit;
    private JButton exitAndSave;
    private JPanel inboxPanel;


    public Clinic() throws IOException , Exception{
        Socket socket = new Socket("Localhost",8080);
        ObjectOutputStream streamer = new ObjectOutputStream(socket.getOutputStream());

        invisible.setSelected(true);
        invisible.setVisible(false);
        secondInvisible.setSelected(true);
        secondInvisible.setVisible(false);
        afterSignIn.setVisible(false);
        doctorSignUp.setVisible(false);
        doctorStatus.setVisible(false);
        afterSignUp.setVisible(false);
        signInPanel.setVisible(false);
        patientStatus.setVisible(false);
        editInformation.setVisible(false);
        sentPnl.setVisible(false);
        sendMesagePanel.setVisible(false);
        emailStatus.setVisible(false);
        inboxPnl.setVisible(false);

        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPanel.setVisible(false);
                afterSignUp.setVisible(true);
            }
        });
        doctor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afterSignUp.setVisible(false);
                doctorSignUp.setVisible(true);

                try { // Tells servers to sign up current doctor
                    streamer.writeObject("doctor sign up");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        doctorSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctor.isSelected()){
                    // Make doctor object
                    Doctor d = new Doctor(doctorUsernameField.getText());
                    d.setName(doctorNameField.getText());
                    d.setPassword(doctorPasswordField.getText());

                    // Send object to server
                    try {
                        streamer.writeObject(d);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    doctor.setSelected(false);
                    patient.setSelected(false);
                    doctorNameField.setText("");
                    doctorUsernameField.setText("");
                    doctorPasswordField.setText("");

                }
                if (patient.isSelected()){
                    // Make patient object
                    Patient p = new Patient(doctorUsernameField.getText());
                    p.setName(doctorNameField.getText());
                    p.setPassword(doctorPasswordField.getText());

                    // Send object to server
                    try {
                        streamer.writeObject(p);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    patient.setSelected(false);
                    doctor.setSelected(false);
                    doctorNameField.setText("");
                    doctorUsernameField.setText("");
                    doctorPasswordField.setText("");

                }



                try {
                    String result = "";
                    ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                    result = (String) messageStreamer.readObject(); // Receive the result from server

                    // Check if username is duplicate
                    if (result.equals("OK")){
                        doctorSignUp.setVisible(false);
                        doctorStatus.setVisible(true);
                        doctorStatusField.setText("You have successfully signed up!");
                    }if (result.equals("NO")){
                        doctorSignUp.setVisible(false);
                        doctorStatus.setVisible(true);
                        doctorStatusField.setText("This username has been taken!");
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        doctorSignUpExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doctorStatus.setVisible(false);
                downPanel.setVisible(true);
                invisible.setSelected(true);
            }
        });
        patient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afterSignUp.setVisible(false);
                doctorSignUp.setVisible(true);

                try {// Tells servers to sign up current patient
                    streamer.writeObject("patient sign up");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPanel.setVisible(false);
                signInPanel.setVisible(true);
            }
        });
        doctorRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {// Tells servers to sign in current doctor
                    streamer.writeObject("doctor sign in");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        patientRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // Tells servers to sign up current patient
                    streamer.writeObject("patient sign in");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        patientSignInbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctorRadioButton.isSelected()){
                    // Make doctor object
                    Doctor d = new Doctor(patientUsernameField.getText());
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    d.setPassword(pass);
                    // Send object to server
                    try {
                        streamer.writeObject(d);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }if (patientRadioButton.isSelected()){
                    // Make patient object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Patient patient1 = new Patient(patientUsernameField.getText());
                    patient1.setPassword(pass);

                    // Send object to server
                    try {
                        streamer.writeObject(patient1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }

                if (!secondInvisible.isSelected()){
                    String result = "";
                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        result = (String) messageStreamer.readObject(); // Receive the result from server

                        if (result.equals("OK doctor")) {// Doctor signed in successfully
                            signInPanel.setVisible(false);
                            afterSignIn.setVisible(true);



                        }if (result.equals("OK patient")) {// Patient signed in successfully
                            signInPanel.setVisible(false);
                            afterSignIn.setVisible(true);



                        }if (result.equals("NO")){// Patient or doctor signed in unsuccessfully
                            signInPanel.setVisible(false);
                            patientStatus.setVisible(true);
                        }


                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }if (secondInvisible.isSelected()){
                    signInPanel.setVisible(false);
                    patientStatus.setVisible(true);
                }




            }
        });
        inbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (patientRadioButton.isSelected()){

                    try { // Ask server for patients inbox
                        streamer.writeObject("INBOX Patient");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    afterSignIn.setVisible(false);
                    inboxPnl.setVisible(true);

                    // Make patient object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Patient p1 = new Patient(patientUsernameField.getText());
                    p1.setPassword(pass);

                    try { // Send object to server
                        streamer.writeObject(p1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Patient p2 = (Patient) messageStreamer.readObject();// Receive current patient from server

                        // Receive messages arraylist from server
                        ArrayList<Message> messages = (ArrayList<Message>) messageStreamer.readObject();


                        inboxArea.setText("");
                        for (int i=0; i<messages.size(); i++) { // patient inbox
                            if (messages.get(i).getReceiver().equals(p2.getUsername())){
                                inboxArea.setText(inboxArea.getText()+"Dr."+messages.get(i).getSenderName()+" at "+messages.get(i).getTime()+" : "+messages.get(i).getTextt()+"\n");
                            }
                        }


                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }


                }if (doctorRadioButton.isSelected()){
                    try { // Asks server for doctor inbox
                        streamer.writeObject("INBOX Doc");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    afterSignIn.setVisible(false);
                    inboxPnl.setVisible(true);

                    // Make doctor object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Doctor d1 = new Doctor(patientUsernameField.getText());
                    d1.setPassword(pass);

                    // Send object to server
                    try {
                        streamer.writeObject(d1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Doctor d2 = (Doctor) messageStreamer.readObject(); // Receive current doctor object from server

                        // Receive messages arraylist from server
                        ArrayList<Message> messages = (ArrayList<Message>) messageStreamer.readObject();


                        inboxArea.setText("");
                        for (int i=0; i<messages.size(); i++) { // doctor inbox
                            if (messages.get(i).getReceiver().equals(d2.getUsername())){
                                inboxArea.setText(inboxArea.getText()+"Pt."+messages.get(i).getSenderName()+" at "+messages.get(i).getTime()+" : "+messages.get(i).getTextt()+"\n");
                            }
                        }


                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }


                }

            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientRadioButton.isSelected()){

                    try { // Tells server that patient wants to send message
                        streamer.writeObject("Send Patient");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Make patient object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Patient p1 = new Patient(patientUsernameField.getText());
                    p1.setPassword(pass);

                    // Send object to the server
                    try {
                        streamer.writeObject(p1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Send Receiver username to server
                    try {
                        streamer.writeObject(toField.getText());
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Patient p3 = (Patient) messageStreamer.readObject(); // Receive current patient object from server

                        String check = (String) messageStreamer.readObject();

                        // Check if username exists
                        if (check.equals("OK message") && !toField.getText().equals(p3.getUsername())){
                            Doctor d4 = (Doctor) messageStreamer.readObject(); // Receive current doctor object from server

                            Message m = new Message(p3.getUsername(), toField.getText(), composeArea.getText());
                            m.setReceiverName(d4.getName());
                            m.setSenderName(p3.getName());
                            m.setReceiver(d4.getName());
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
                            LocalDateTime now = LocalDateTime.now();
                            m.setTime(dtf.format(now));

                            try {
                                streamer.writeObject(m);
                                streamer.flush();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            afterSignIn.setVisible(true);
                            sendMesagePanel.setVisible(false);


                        }
                        if (check.equals("NO message") || toField.getText().equals(p3.getUsername())){
                            sendMesagePanel.setVisible(false);
                            emailStatus.setVisible(true);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    toField.setText("");
                    composeArea.setText("");


                }if (doctorRadioButton.isSelected()){

                    try {// Tells server that doctor wants to send message
                        streamer.writeObject("Send doc");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Make doctor object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Doctor d1 = new Doctor(patientUsernameField.getText());
                    d1.setPassword(pass);

                    // Send object to server
                    try {
                        streamer.writeObject(d1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {// Send Receiver username to server
                        streamer.writeObject(toField.getText());
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Doctor d3 = (Doctor) messageStreamer.readObject(); // Receive current doctor object from server

                        String check = (String) messageStreamer.readObject();

                        // Check if username exists
                        if (check.equals("OK message") && !toField.getText().equals(d3.getUsername())){
                            Patient p4 = (Patient) messageStreamer.readObject(); // Receive current pateint object from server

                            Message m = new Message(d3.getUsername(), toField.getText(), composeArea.getText());
                            m.setReceiverName(p4.getName());
                            m.setSenderName(d3.getName());
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
                            LocalDateTime now = LocalDateTime.now();
                            m.setTime(dtf.format(now));

                            try {
                                streamer.writeObject(m);
                                streamer.flush();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            afterSignIn.setVisible(true);
                            sendMesagePanel.setVisible(false);


                        }
                        if (check.equals("NO message") || toField.getText().equals(d3.getUsername())){
                            sendMesagePanel.setVisible(false);
                            emailStatus.setVisible(true);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    toField.setText("");
                    composeArea.setText("");


                }

            }
        });
        exitSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afterSignIn.setVisible(false);
                downPanel.setVisible(true);
                secondInvisible.setSelected(true);
                patientUsernameField.setText("");
                patientPasswordField.setText("");

            }
        });
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afterSignIn.setVisible(false);
                sendMesagePanel.setVisible(true);
            }
        });

        patientSignInExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientStatus.setVisible(false);
                downPanel.setVisible(true);
                secondInvisible.setSelected(true);
                patientUsernameField.setText("");
                patientPasswordField.setText("");
            }
        });
        accountInformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afterSignIn.setVisible(false);
                editInformation.setVisible(true);

                try { // Tells server that client wants to edit his or her information
                    streamer.writeObject("edit information");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try { // Send last username to server
                    streamer.writeObject(patientUsernameField.getText());
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    String result = "";
                    ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                    result = (String) messageStreamer.readObject();

                    // Check if that username exists to updaye the information
                    if (result.equals("patient")){
                        Patient p = new Patient(editUsernameField.getText());
                        p.setName(editNameField.getText());
                        p.setPassword(editPasswordField.getText());

                        try {
                            streamer.writeObject(p);
                            streamer.flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }if (result.equals("doctor")){
                        Doctor d = new Doctor(editUsernameField.getText());
                        d.setName(editNameField.getText());
                        d.setPassword(editPasswordField.getText());

                        try {
                            streamer.writeObject(d);
                            streamer.flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                    editInformation.setVisible(false);
                    secondInvisible.setSelected(true);
                    patientUsernameField.setText("");
                    patientPasswordField.setText("");
                    downPanel.setVisible(true);
                    editNameField.setText("");
                    editUsernameField.setText("");
                    editPasswordField.setText("");
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exitEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailStatus.setVisible(false);
                afterSignIn.setVisible(true);
            }
        });

        exitSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inboxPnl.setVisible(false);
                afterSignIn.setVisible(true);
            }
        });
        sent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientRadioButton.isSelected()){

                    try { // Tells server that patient wants to see outbox
                        streamer.writeObject("Sent Patient");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    afterSignIn.setVisible(false);
                    sentPnl.setVisible(true);

                   // Make patient object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Patient p1 = new Patient(patientUsernameField.getText());
                    p1.setPassword(pass);

                    try { // Send object to server
                        streamer.writeObject(p1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Patient p2 = (Patient) messageStreamer.readObject(); // Receive current doctor object from server

                        // Receive messages arraylist from server
                        ArrayList<Message> messages = (ArrayList<Message>) messageStreamer.readObject();


                        outBoxArea.setText("");
                        for (int i=0; i<messages.size(); i++) { // Patient outbox
                            if (messages.get(i).getSender().equals(p2.getUsername())){
                                outBoxArea.setText(outBoxArea.getText()+"To Dr."+messages.get(i).getReceiverName()+" at "+messages.get(i).getTime()+" : "+messages.get(i).getTextt()+"\n");
                            }
                        }


                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }


                }if (doctorRadioButton.isSelected()){
                    try {// Tells server that doctor wants to see outbox
                        streamer.writeObject("Sent Doc");
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    afterSignIn.setVisible(false);
                    sentPnl.setVisible(true);

                    // Make doctor object
                    String pwd = new String(patientPasswordField.getPassword());
                    String pass = pwd+"";
                    Doctor d1 = new Doctor(patientUsernameField.getText());
                    d1.setPassword(pass);

                    try { // Send object to server
                        streamer.writeObject(d1);
                        streamer.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        ObjectInputStream messageStreamer = new ObjectInputStream(socket.getInputStream());
                        Doctor d2 = (Doctor) messageStreamer.readObject();// Receive current doctor object from server

                        // Receive messages arraylist from server
                        ArrayList<Message> messages = (ArrayList<Message>) messageStreamer.readObject();


                        outBoxArea.setText("");
                        for (int i=0; i<messages.size(); i++) { // doctor outbox
                            if (messages.get(i).getSender().equals(d2.getUsername())){
                                outBoxArea.setText(outBoxArea.getText()+"To Pt."+messages.get(i).getReceiverName()+" at "+messages.get(i).getTime()+" : "+messages.get(i).getTextt()+"\n");
                            }
                        }


                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }


                }
            }
        });
        sentExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sentPnl.setVisible(false);
                afterSignIn.setVisible(true);
            }
        });
        exitAndSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // Tells server that client wants to exit
                    streamer.writeObject("Exit");
                    streamer.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Clinic");
        frame.setContentPane(new Clinic().main);
        frame.setPreferredSize(new Dimension(600, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

