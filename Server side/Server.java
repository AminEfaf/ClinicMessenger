import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Server{
    public static void main(String[] args) throws IOException , ClassCastException, Exception{
        ServerSocket server = null;
        try {
            server = new ServerSocket(8080);
            server.setReuseAddress(true);
    
            while(true){
                // Wait for new client
                Socket socket = server.accept();
                clientHandler clientSock = new clientHandler(socket);
    
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

}
class clientHandler implements Runnable{
    static ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    static ArrayList<Patient> patients = new ArrayList<Patient>();
    static ArrayList<Message> messages = new ArrayList<Message>();
    private final Socket socket;

    // Constructor
    public clientHandler(Socket socket) throws IOException , ClassCastException, Exception{
        this.socket = socket;
    }

    @Override
    public void run(){
        // Read arraylist from file
        // Deserialize
        try {
            FileInputStream inputStream = new FileInputStream("save.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            doctors = (ArrayList<Doctor>) objectInputStream.readObject();
            patients = (ArrayList<Patient>) objectInputStream.readObject();
            messages = (ArrayList<Message>) objectInputStream.readObject();


            objectInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.getStackTrace();
        }



        try {
            ObjectInputStream messageStream = new ObjectInputStream(socket.getInputStream());

            while(true){
                // Check what client wants to do
                String role = (String) messageStream.readObject();
            

                if (role.equals("doctor sign up")) { // Doctor sign up
                    Doctor d = (Doctor) messageStream.readObject();

                    int doctorIndex = doctorUsernameAuthentication(d.getUsername());
                    int x = patientUsernameAuthentication(d.getUsername());
                    if (doctorIndex == -1 && x == -1) { // Signed up successfully
                        doctors.add(d);

                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("OK");
                        stream.flush();

                    }else{ // Signed up unsuccessfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("NO");
                        stream.flush();

                    }
                    
                }if (role.equals("patient sign up")){ // Patient sign up
                    Patient p = (Patient) messageStream.readObject();

                    int patientIndex = patientUsernameAuthentication(p.getUsername());
                    int x = doctorUsernameAuthentication(p.getUsername());
                    if (patientIndex == -1 && x == -1) {// Signed up successfully
                        patients.add(p);

                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("OK");
                        stream.flush();
                    }else{// Signed up unsuccessfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("NO");
                        stream.flush();

                    }
                
                }if (role.equals("doctor sign in")){ // Doctor sign in
                    Doctor doctor1 = (Doctor) messageStream.readObject();

                    int doctorIndex = doctorAuthentication(doctor1.getUsername(), doctor1.getPassword());

                    if (doctorIndex != -1) {// Signed up successfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("OK doctor");
                        stream.flush();
                    }else {// Signed up unsuccessfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("NO");
                        stream.flush();
                    }
                }if (role.equals("patient sign in")){ // Patient sign in
                    Patient patient1 = (Patient) messageStream.readObject();

                    int patientIndex = patientAuthentication(patient1.getUsername(), patient1.getPassword());

                    if (patientIndex != -1) {// Signed up successfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("OK patient");
                        stream.flush();

                    }else {// Signed up unsuccessfully
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("NO");
                        stream.flush();
                    }
                }if (role.equals("edit information")){ // Edit information
                    String secondRole = (String) messageStream.readObject();
                    int patientIndex = patientUsernameAuthentication(secondRole);
                    int x = doctorUsernameAuthentication(secondRole);

                    if (patientIndex != -1){ // Edit patient information
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("patient");
                        stream.flush();

                        Patient p = (Patient) messageStream.readObject();
                        Patient currentPatient;

                        currentPatient = patients.get(patientIndex);
                        currentPatient.setUsername(p.getUsername());
                        currentPatient.setName(p.getName());
                        currentPatient.setPassword(p.getPassword());

                    }if (x != -1){ // Edit doctor information
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject("doctor");
                        stream.flush();

                        Doctor d = (Doctor) messageStream.readObject();
                        Doctor currentDoctor;

                        currentDoctor = doctors.get(x);
                        currentDoctor.setUsername(d.getUsername());
                        currentDoctor.setName(d.getName());
                        currentDoctor.setPassword(d.getPassword());
                        
                    }
                }if (role.equals("Send Patient")){ // Patient send message
                    Patient patient1 = (Patient) messageStream.readObject();

                    String check = (String) messageStream.readObject();

                    int patientIndex = patientAuthentication(patient1.getUsername(), patient1.getPassword());
                    Patient currentpatient;
                    currentpatient = patients.get(patientIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentpatient);
                    stream.flush();

                    int doctorIndex = doctorUsernameAuthentication(check);

                    if (doctorIndex != -1){ // Message sent successfully
                        stream.writeObject("OK message");
                        stream.flush();
                        
                        Doctor currentDoctor;
                        currentDoctor = doctors.get(doctorIndex);
                        
                        stream.writeObject(currentDoctor);
                        stream.flush();

                        Message m = (Message) messageStream.readObject();
                        messages.add(m);

                    }else{ // Message sent unsuccessfully
                        stream.writeObject("NO message");
                        stream.flush();
                    }
                }if (role.equals("Send doc")){ // Doctor send message
                    Doctor doctor1 = (Doctor) messageStream.readObject();

                    String check = (String) messageStream.readObject();

                    int doctorIndex = doctorAuthentication(doctor1.getUsername(), doctor1.getPassword());
                    Doctor currentDoctor;
                    currentDoctor = doctors.get(doctorIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentDoctor);
                    stream.flush();

                    int patientIndex = patientUsernameAuthentication(check);

                    if (patientIndex != -1){ // Message sent successfully
                        stream.writeObject("OK message");
                        stream.flush();

                        Patient currentpatient;
                        currentpatient = patients.get(patientIndex);
                        
                        stream.writeObject(currentpatient);
                        stream.flush();



                        Message m = (Message) messageStream.readObject();
                        messages.add(m);

                    }else{ // Message sent unsuccessfully
                        stream.writeObject("NO message");
                        stream.flush();
                    }
                }if (role.equals("INBOX Patient")){ // Patient inbox
                    Patient patient1 = (Patient) messageStream.readObject();

                    int patientIndex = patientAuthentication(patient1.getUsername(), patient1.getPassword());
                    Patient currentpatient;
                    currentpatient = patients.get(patientIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentpatient);
                    stream.flush();

                    stream.writeObject(messages);
                    stream.flush();

                }if (role.equals("INBOX Doc")){ // Doctor inbox
                    Doctor d1 = (Doctor) messageStream.readObject();

                    int doctorIndex = doctorAuthentication(d1.getUsername(), d1.getPassword());
                    Doctor currentDoctor;
                    currentDoctor = doctors.get(doctorIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentDoctor);
                    stream.flush();

                    stream.writeObject(messages);
                    stream.flush();

                }if (role.equals("Sent Patient")){ // Patient outbox
                    Patient patient1 = (Patient) messageStream.readObject();

                    int patientIndex = patientAuthentication(patient1.getUsername(), patient1.getPassword());
                    Patient currentpatient;
                    currentpatient = patients.get(patientIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentpatient);
                    stream.flush();

                    stream.writeObject(messages);
                    stream.flush();

                }if (role.equals("Sent Doc")){ // Doctor outbox
                    Doctor d1 = (Doctor) messageStream.readObject();

                    int doctorIndex = doctorAuthentication(d1.getUsername(), d1.getPassword());
                    Doctor currentDoctor;
                    currentDoctor = doctors.get(doctorIndex);
                    
                    ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                    stream.writeObject(currentDoctor);
                    stream.flush();

                    stream.writeObject(messages);
                    stream.flush();

                }if (role.equals("Exit")){// Exit
                    // Save mangers to file
                    // Serialize
                    try {
                        FileOutputStream outputStream = new FileOutputStream("save.txt");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                        objectOutputStream.writeObject(doctors);
                        objectOutputStream.writeObject(patients);
                        objectOutputStream.writeObject(messages);

                        objectOutputStream.close();
                        outputStream.close();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                }

            }
        }catch (IOException| ClassNotFoundException e) {
            e.printStackTrace();
        }




    }
    // Check message index in messages arrayList
    static int messageAuthentication(String text){
        int index = -1;
        for (int i=0; i<messages.size(); i++) {
            if (messages.get(i).getTextt().equals(text)){
                index = i;
                break;
            }
        }
        return index;
    }

    // Doctor Authentication
    static int doctorAuthentication(String username, String password){
        int index = -1;
        for (int i=0; i<doctors.size(); i++) {
            if (doctors.get(i).getUsername().equals(username)) {
                if (doctors.get(i).getPassword().equals(password)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    // Check doctor index in doctors arrayList
    static int doctorUsernameAuthentication(String username){
        int index = -1;
        for (int i=0; i<doctors.size(); i++) {
            if (doctors.get(i).getUsername().equals(username)){
                index = i;
                break;
            }
        }
        return index;
    }

    // Patient Authentication
    static int patientAuthentication(String username, String password){
        int index = -1;
        for (int i=0; i<patients.size(); i++) {
            if (patients.get(i).getUsername().equals(username)) {
                if (patients.get(i).getPassword().equals(password)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    // Check patient index in patients arrayList
    static int patientUsernameAuthentication(String username){
        int index = -1;
        for (int i=0; i<patients.size(); i++) {
            if (patients.get(i).getUsername().equals(username)){
                index = i;
                break;
            }
        }
        return index;
    }
}
