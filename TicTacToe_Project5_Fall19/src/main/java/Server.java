import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server {

    int portNumber;
    String ipAddress;
    int count = 1;
    int round = 1;
    int p1Score = 0;
    int p2Score = 0;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    ServerThread server;
    TicTacToeGameLogic gameLogic = new TicTacToeGameLogic();
    private Consumer<Serializable> callback;

    Server(int portNumber) {
        this.portNumber = portNumber;
    }

    Server(String ip, int pN) {
        portNumber = pN;
        ipAddress = ip;
    }


    public void runServer() {
        server = new ServerThread();
    }

    public void setCallback(Consumer<Serializable> callback) {
        this.callback = callback;
        server.start();
    }

    public class ServerThread extends Thread {

        @Override
        public void run() {
            this.setName("ServerThread");
            try (ServerSocket mysocket = new ServerSocket(portNumber)) {

                while (true) {
                    if (clients.size() >= 0) {
                        callback.accept("Server is accepting clients...");
                    }
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count + " as player " + count);
                    count++;
                    synchronized (clients) {
                        clients.add(c);
                        clients.notify();
                    }
                    c.start(); //GAMESTART CODE HERE
                    if (clients.size() == 2) {
                        GameThread g = new GameThread();
                        g.start();
                        callback.accept("2 clients connected to the game. Begin the game...");
                    }
                }
            }//end of try
            catch (IOException e) {
                callback.accept("Server socket did not launch");
                e.printStackTrace();
            }
        }//end of while
    }

    class ClientThread extends Thread {

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.count = count;
        }

        public void updateClients(String message) {
            for (ClientThread t : clients) {
                try {
                    t.out.writeObject(new GameInfo(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void readData() throws IOException, ClassNotFoundException {
            System.out.println(in.readObject());
        }

        @Override
        public void run() {
            this.setName("ClientThread");
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);

            } catch (Exception e) {
                System.out.println("Streams not open");
            }

            updateClients("New player connected to the server: player #" + count);

            while (true) {
                try {
                    //String data = in.readObject().toString();
                    //callback.accept("Player " + count + " sent: " + data);
                    //updateClients("client #" + count + " said: " + data);

                } catch (Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    updateClients("Client #" + count + " has left the server!");
                    clients.remove(this);
                    count--;
                    break;
                }
            }
        }//end of run


    }//end of client thread

    class GameThread extends Thread {

        GameInfo gameInfo = new GameInfo();
        @Override
        public void run() {
            this.setName("GameThread");
            try {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    GameInfo gameInfo = new GameInfo();
                    gameInfo.setHaveTwoPlayers(true);
                    gameInfo.setMessage("Round " + round + " has started! Pick your move");
                    gameInfo.setGameState(GameInfo.State.PLAYING);
                    clients.get(0).out.writeObject(gameInfo);
                    clients.get(1).out.writeObject(gameInfo);

                    callback.accept("ROUND " + round + ": START");
                    GameInfo p1Played = (GameInfo) clients.get(0).in.readObject();
                    callback.accept("Player 1 chose " + p1Played.getP1play());
                    GameInfo p2Played = (GameInfo) clients.get(1).in.readObject();
                    callback.accept("Player 2 chose " + p2Played.getP1play());

                    //ArrayList<GameInfo> result = gameLogic.evalHand(p1Played.getP1play(), p2Played.getP1play());
                    ArrayList<GameInfo> result = null;
                    callback.accept("Player 1 " + result.get(0).getP1Result());
                    callback.accept("Player 2 " + result.get(1).getP1Result());
                    result.get(0).setGameState(GameInfo.State.RESULT);
                    result.get(1).setGameState(GameInfo.State.RESULT);
                    clients.get(0).out.writeObject(result.get(0));
                    clients.get(1).out.writeObject(result.get(1));

                    clients.get(0).updateClients("Player 1 chose " + p1Played.getP1play());
                    clients.get(1).updateClients("Player 2 chose " + p2Played.getP1play());

                    clients.get(0).updateClients("Player 1 score: " + result.get(0).getP1Result());
                    clients.get(1).updateClients("Player 2 score: " + result.get(1).getP1Result());

                    callback.accept("Player 1 point: " + result.get(0).getP1Points());
                    callback.accept("Player 2 point: " + result.get(1).getP1Points());
                    callback.accept("ROUND " + round + ": END");

                    clients.get(0).updateClients("ROUND " + round + ": END");

                    round++;

                    if (result.get(0).getP1Points() == 1 || result.get(1).getP1Points() == 1 ||
                        result.get(0).getP2Points() == 1 || result.get(1).getP2Points() == 1) {
                        ArrayList<GameInfo> finalResult = gameLogic.evalWinner(result.get(0).getP1Points(), result.get(1).getP1Points());
                        finalResult.get(0).setGameState(GameInfo.State.FINISHED);
                        finalResult.get(1).setGameState(GameInfo.State.FINISHED);
                        callback.accept("Game over! Player 1 " + finalResult.get(0).getTotalResult() + " Player 2 " + finalResult.get(1).getTotalResult());
                        clients.get(0).out.writeObject(finalResult.get(0));
                        clients.get(1).out.writeObject(finalResult.get(1));

                        GameInfo gameInfo2;

                        this.gameInfo = (GameInfo) clients.get(0).in.readObject();
                        gameInfo2 = (GameInfo) clients.get(1).in.readObject();

                        if (this.gameInfo.getGameState() == GameInfo.State.PLAYAGAIN) {
                            callback.accept("Player 1 wants to play again!");
                        }

                        if (gameInfo2.getGameState() == GameInfo.State.PLAYAGAIN) {
                            callback.accept("Player 2 wants to play again!");
                        }

                        if ((this.gameInfo.getGameState() == GameInfo.State.PLAYAGAIN) &&
                                (gameInfo2.getGameState() == GameInfo.State.PLAYAGAIN)) {
                            callback.accept("Both players want to play again. Restart game...");
                            this.gameInfo = new GameInfo();
                            gameInfo2 = new GameInfo();
                            gameLogic = new TicTacToeGameLogic();
                            this.gameInfo.setPlayAgain(true);
                            gameInfo2.setPlayAgain(true);
                            clients.get(0).out.writeObject(this.gameInfo);
                            clients.get(1).out.writeObject(gameInfo2);
                            round = 1;
                        }

                        if ((this.gameInfo.getGameState() == GameInfo.State.QUIT) ||
                                (gameInfo2.getGameState() == GameInfo.State.QUIT)) {
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}






