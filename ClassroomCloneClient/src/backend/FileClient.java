package backend;

import UI.LMSConstants;

import java.io.*;
import java.net.Socket;

public class FileClient {
    private Socket fileSocket;

    private DataOutputStream dos;
    private FileInputStream fis;

    private DataInputStream dis;
    private FileOutputStream fos;

    private void initConnections(File fileToSend) {
        try {
            fileSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.FILE_SERVER_PORT);
            System.out.println(fileSocket);

            dos = new DataOutputStream(fileSocket.getOutputStream());
            fis = new FileInputStream(fileToSend);

            dos.writeUTF(fileToSend.getName());
            dos.writeUTF(String.valueOf(fileToSend.length()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initConnections() {
        try {
            fileSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.FILE_SERVER_PORT);
            System.out.println(fileSocket);

            dos = new DataOutputStream(fileSocket.getOutputStream());

            dos.writeUTF("null");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile (File fileToSend) {
        if (fileToSend == null) {
            initConnections();
            return;
        }
        initConnections(fileToSend);
        byte[] buffer = new byte[4096];

        try {
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

    public void receiveFile(String path) {
        try {
            fileSocket = new Socket(LMSConstants.SERVER_ADDR, LMSConstants.FILE_SERVER_PORT);
            System.out.println(fileSocket);

            dis = new DataInputStream(fileSocket.getInputStream());
            String fileName = dis.readUTF();
            System.out.println(fileName);
            int fileSize = Integer.parseInt((dis.readUTF()));

            path += fileName;
            System.out.println(path);
            fos = new FileOutputStream(path);
            new Thread(() -> saveFile(fileSize)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(int fileSize) {
        byte[] buffer = new byte[4096];
        int read;
        int remaining = fileSize;

        try {
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                remaining -= read;
                fos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams();
        }
    }

    private void closeStreams() {
        try {
            if (fileSocket != null) {
                fileSocket.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
