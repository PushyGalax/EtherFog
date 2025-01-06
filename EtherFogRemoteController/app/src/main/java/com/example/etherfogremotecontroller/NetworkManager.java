package com.example.etherfogremotecontroller;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static String SERVER_IP; // Replace with your server IP
    private static int SERVER_PORT; // Replace with your server port

    public NetworkManager(String IP, int port) {
        this.SERVER_IP = IP;
        this.SERVER_PORT = port;
        System.out.println(SERVER_IP);
        System.out.println(SERVER_PORT);
    }

    public void send(NetworkCallback callback, String message) {
        final Socket[] socket = {null};
        final PrintWriter[] out = {null};
        final BufferedReader[] in = {null};
        String request = message;
        executor.execute(() -> {
            try {
                // Open socket connection
                socket[0] = new Socket();
                socket[0].connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000); // 5 sec timeout
                System.out.println("Connected to server");
                // envoie de message
                out[0] = new PrintWriter(socket[0].getOutputStream(), true);
                in[0] = new BufferedReader(new InputStreamReader(socket[0].getInputStream()));

                System.out.println("Sending request: " + request);
                out[0].println(request);
                System.out.println("Request sent");


                String rec = in[0].readLine();
                mainHandler.post(() -> callback.onSuccess(rec));
            } catch (IOException e) {
                mainHandler.post(() -> callback.onError("Connection error"));
                //Handle connection errors
            } finally {
                try {
                    if (in[0] != null) in[0].close();
                    if (out[0] != null) out[0].close();
                    if (socket[0] != null) socket[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void sendn(NetworkCallback callback, String message) {
        final Socket[] socket = {null};
        final PrintWriter[] out = {null};
        final BufferedReader[] in = {null};
        String request = message;
        executor.execute(() -> {
            try {
                // Open socket connection
                socket[0] = new Socket();
                socket[0].connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000); // 5 sec timeout
                // envoie de message
                out[0] = new PrintWriter(socket[0].getOutputStream(), true);
                in[0] = new BufferedReader(new InputStreamReader(socket[0].getInputStream()));

                System.out.println("Sending request: " + request);
                out[0].println(request);
                //String rec = in[0].readLine();

                mainHandler.post(() -> callback.onSuccess(""));
            } catch (IOException e) {
                mainHandler.post(() -> callback.onError("Connection error"));
                //Handle connection errors
            } finally {
                try {
                    if (in[0] != null) in[0].close();
                    if (out[0] != null) out[0].close();
                    if (socket[0] != null) socket[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface NetworkCallback {
        void onSuccess(String s);
        void onError(String message);
    }
}
