package io.oskin;

import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable {

    private Storage storage;

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String GET_BY_ID = "GET_BY_ID";

    //сокет для общения
    private static Socket clientSocket;
    // серверсокет
    private static ServerSocket server;
    // поток чтения из сокета
    private static BufferedReader in;
    // поток записи в сокет
    private static PrintWriter out;

    private Gson gson;

    public Server(Storage storage) {
        this.storage = storage;
        gson = new Gson();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            server = new ServerSocket(3128, 123, inetAddress); // серверсокет прослушивает порт 4004
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Сервер запущен! Порт: " + server.getLocalPort()); // хорошо бы серверу
        while (true) {

            // accept() будет ждать пока
            //кто-нибудь не захочет подключиться
            try {
                clientSocket = server.accept();
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

                    String request = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                    handleRequest(request);

                } finally {
                    System.out.println("Close client socket.");
                    clientSocket.close();
                    // потоки тоже хорошо бы закрыть
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest(String request) {
        Note note = new Note();
        String[] strings = request.split(" ", 2);
        String command = strings[0].toUpperCase();
        String jsonObject = strings[1];
        switch (command) {
            case POST:
                note = gson.fromJson(jsonObject, Note.class);
                storage.insertNote(note);
                out.println(jsonObject);
                System.out.println(POST + jsonObject);
                break;
            case PUT:
                note = gson.fromJson(jsonObject, Note.class);
                storage.updateNote(note);
                out.println(jsonObject);
                System.out.println(PUT + jsonObject);
                break;
            case GET:
                String jsonList = storage.getNotesJson();
                out.println(jsonList);
                System.out.println(GET + jsonList);
                break;
            case GET_BY_ID:
                String id = jsonObject;
                String jsonNote = storage.getNotesJsonByID(id);
                out.println(jsonNote);
                System.out.println(GET_BY_ID + jsonNote);
                break;
            case DELETE:
                note = gson.fromJson(jsonObject, Note.class);
                storage.deleteNote(note);
                out.println(jsonObject);
                System.out.println(DELETE + jsonObject);
                break;
            default:
                break;

        }
    }
}
