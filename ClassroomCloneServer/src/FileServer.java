import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private ServerSocket fileServer;
    private Socket fileClient;

    private DataInputStream dis;
    private FileOutputStream fos;

    private DataOutputStream dos;
    private FileInputStream fis;

    public static String receivedFilePath = "C:\\Users\\Bhavesh\\Desktop\\LMS\\Files\\";
    private int fileSize;
    private String fileName;

   FileServer() {
        try {
            fileServer = new ServerSocket(4243);
            System.out.println("File Server Started.\n" + fileServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
   }

   public String receiveFile() {
       try {
           fileClient = fileServer.accept();
           System.out.println(fileClient);

           dis = new DataInputStream(fileClient.getInputStream());
           fileName = dis.readUTF();
           if (fileName.equals("null")) {
               closeStreams();
               return "null";
           }
           fileSize = Integer.parseInt(dis.readUTF());

           receivedFilePath += fileName;
           System.out.println(receivedFilePath);
           fos = new FileOutputStream(receivedFilePath);
           new Thread(this::saveFile).start();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return receivedFilePath;
   }

    private void saveFile() {
        byte[] buffer = new byte[4096];
        int read;
        int remaining = fileSize;

        try {
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                remaining -= read;
                fos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams();
        }
    }

    public void sendFile (File fileToSend) {
        try {
            fileClient = fileServer.accept();
            System.out.println(fileClient);
            initConnections(fileToSend);
            byte[] buffer = new byte[4096];

            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }
            System.out.println("File Sent Successfully!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams();
        }
    }

    private void initConnections(File fileToSend) {
        try {
            dos = new DataOutputStream(fileClient.getOutputStream());
            fis = new FileInputStream(fileToSend);

            dos.writeUTF(fileToSend.getName());
            dos.writeUTF(String.valueOf(fileToSend.length()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStreams() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (fileClient != null) {
                fileClient.close();
            }
            if (fileServer != null) {
                fileServer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
