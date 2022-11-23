package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {

    static Connection conn;
    static List<Client> clients;

    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src/server/data.db");
        clients = new ArrayList<>();
        try (ServerSocket server = new ServerSocket(5555)) {

            while (true) {

                Socket client = server.accept();

                // TODO: make server accept login check for several clients in the same time

                DataInputStream dis = new DataInputStream(client.getInputStream());
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                String username = dis.readUTF();
                String password = dis.readUTF();

                md.update(password.getBytes());
                password = init.App.byte2hex(md.digest());

                if (checkLogin(username, password)) {
                    dos.writeUTF("success");
                    Client c = new Client(username, getFullName(username), client, dis, dos);
                    clients.add(c);

                    new Sender(c).start();
                    new Receiver(c, clients).start();
                } else {
                    dos.writeUTF("fail");
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String getFullName(String username) {
        // TODO: get user's name from database using his username
        try{ 
        String queryString = "SELECT * FROM users";
            ResultSet results = Statement.executeQuery(queryString);
        while (results.next()) {
           String username = results.getString("sname");
           if ((username.equals(username)){
               String fullname = results.getString("name");
           } }
         results.close();
 }catch (SQLException sql) {

            out.println(sql);}
        return "Sample Name";
    }

    private static boolean checkLogin(String username, String password) {
         try {
            String queryString = "SELECT sName, Pwd FROM users";
            ResultSet results = Statement.executeQuery(queryString);

            while (results.next()) {
            String username = results.getString("sname");
            String userpassword =  results.getString("SPwd");

               if ((username.equals(username)) && (password.equals(userpassword))) {

                  JOptionPane.showMessageDialog(null, "Username and Password exist");  
            }else {

             //JOptionPane.showMessageDialog(null, "Please Check Username and Password ");
            } }
            results.close();
        } catch (SQLException sql) {

            out.println(sql);}
        return true;
    
}
