package io.oskin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static Server server;
    private static ExecutorService mExecutorService;
    private static Storage storage;

    public static void main(String[] args) {
        mExecutorService = Executors.newCachedThreadPool();
        storage = new Storage();
        server = new Server(storage);
        mExecutorService.submit(server);
    }
}
