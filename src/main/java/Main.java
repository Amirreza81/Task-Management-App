public class Main {
    public static void main(String[] args) {
        runServer();
    }

    private static void runServer() {
        Server server = Server.getInstance();
        server.run();
    }
}
