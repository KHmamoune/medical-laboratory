import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private DatabaseConnector databaseConnector;
    private boolean isLoggedIn = false;
    private String type;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            databaseConnector = DatabaseConnector.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!isLoggedIn) {
                    String receivedMessage = dis.readUTF();
                    String[] parts = receivedMessage.split(":", 2);
                    String username = parts[0];
                    String password = parts[1];

                    if (databaseConnector.authenticate(username, password)) {
                        this.type = databaseConnector.getUserType(username, password);
                        System.out.println("authentication succsfuly");

                        dos.writeUTF(type);
                        System.out.println("Write UTF of the type");
                        isLoggedIn = true;
                        // Continue handling client requests...
                    } else {
                        dos.writeUTF("Authentication failed");
                        System.out.println("authentication failed");

                    }

                } else {
                    // Client is logged in, proceed with handling client's requests
                    String receivedMessage = dis.readUTF();
                    // Implement protocol to handle client's requests
                    // handleRequest(receivedMessage);

                    switch (type) {
                        case "gerant":
                            // Handle doctor's requests
                            //  System.out.println(receivedMessage);
                            System.out.println(receivedMessage);
                            String[] parts = receivedMessage.split(":", 2);
                            String controller = parts[0];
                            String data = parts[1];

                            if (controller.equals("addClient")) {
                                addClientHandler(data);
                                dos.writeUTF("addClientSucced");
                            }
                            if (controller.equals("getAllUsers")) {
                                sendAllUsersToClient();
                            }

                            break;
                        case "doctor":
                            // Handle reception agent's requests
                            break;
                        case "agent":
                            // Handle manager's requests
                            break;
                        case "technician":
                            // Handle technician's requests
                            break;
                        default:
                            // Handle unrecognized role
                            break;
                    }
                }

            }

        } catch (IOException | SQLException e) {
            closeEverything(socket, dis, dos);
        }

    }

    public void closeEverything(Socket socket, DataInputStream dis, DataOutputStream dos) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addClient(String nom, String prenom, String username, String password, String type) throws SQLException {
        if (databaseConnector.addClient(nom, prenom, username, password, type)) {
            System.out.println("client added succefuly");
        } else {
            System.out.println("client don't added");
        }
    }

    private void addClientHandler(String data) {
        String parts[] = data.split(":", 5);
        String nom = parts[0];
        String prenom = parts[1];
        String username = parts[2];
        String password = parts[3];
        String type = parts[4];
        try {
            addClient(nom, prenom, username, password, type);
        } catch (SQLException e) {
            System.out.println("error in addClient");
        }

    }

    private void sendAllUsersToClient() {
        try {
            // Retrieve all users from the database
            List<User> userList = databaseConnector.getAllUsers();
            // Create a StringBuilder to construct the message to send
            StringBuilder messageBuilder = new StringBuilder();
            for (User user : userList) {
                messageBuilder.append(user.getNom()).append(":").append(user.getPrenom()).append(":").append(user.getUsername()).append(":").append(user.getPassword()).append(":").append(user.getType()).append("\n");

            }
            // Send the message to the client
            dos.writeUTF(messageBuilder.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
