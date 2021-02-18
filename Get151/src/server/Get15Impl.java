package server;

import callback.CallBack;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Get15Impl extends UnicastRemoteObject implements Get15Interface {
    //Declare two players, used to call players back
    private CallBack player1 = null;
    private CallBack player2 = null;

    //List of numbers that each player has selected
    private ArrayList<Integer> player1Numbers = new ArrayList<>();
    private ArrayList<Integer> player2Numbers = new ArrayList<>();

    //Custom colors for the winning client view and the losing client view
    Color greenHighlight = new Color(91, 196, 35, 255);
    Color redHighlight = new Color(224, 37, 37, 255);

    /** Constructs Get15Impl object and exports it on default port */
    protected Get15Impl() throws RemoteException {
        super();
    }

    /** Constructs Get15Impl object and exports it on specified port.
     *  @param port The port for exporting
     **/
    public Get15Impl(int port) throws RemoteException {
        super(port);
    }

    /** Connect to the Get15 server and return the token.
     * If the returned token is 3, the client is not connected to the server
     **/
    public int connect(CallBack client) throws RemoteException {
        if (player1 == null) {
            // player1 (first player) registered
            player1 = client;
            player1.notify(" Wait for a second player to join");
            return 1;
        }
        else if (player2 == null) {
            // player2 (second player) registered
            player2 = client;
            player2.notify(" Wait for the first player to move");
            player2.takeTurn(false);
            player1.notify(" It is my turn to select number");
            player1.takeTurn(true);
            return 2;
        }
        else {
            // Already two players
            client.notify(" Two players are already in the game");
            return 3;
        }
    }

    /** A client invokes this method to notify the server of their move and update numbers list */
    public void myMove(int number, int playerID) throws RemoteException {
        //Notify the other player of the move and update list of numbers
        if (playerID == 1) {
            player1Numbers.add(number);
            player2.mark(number);
        }
        else if (playerID == 2) {
            player2Numbers.add(number);
            player1.mark(number);
        }

        //Check if the player with this ID wins
        if (isWinner(playerID)) {
            if (playerID == 1) {
                player1.notify("      You win!");
                player1.highlightWinningNumbers(greenHighlight);
                player2.notify("      You lose!");
                player2.highlightWinningNumbers(redHighlight);
                player1.takeTurn(false);
            }
            else if (playerID == 2) {
                player2.notify("      You win!");
                player2.highlightWinningNumbers(greenHighlight);
                player1.notify("      You lose!");
                player1.highlightWinningNumbers(redHighlight);
                player2.takeTurn(false);
            }
        }

        //If the player doesn't win continue game
        else if (playerID == 1) {
            player1.notify(" Wait for player 2 to move");
            player1.takeTurn(false);
            player2.notify(" It is my turn");
            player2.takeTurn(true);
        }
        else if (playerID == 2) {
            player2.notify(" Wait for the player 1 to move");
            player2.takeTurn(false);
            player1.notify(" It is my turn");
            player1.takeTurn(true);
        }
    }

    /** Method that checks if the current player has any three numbers that total 15 */
    public boolean isWinner(int playerID) {
        //Size of playerNumbers array could vary. We get the array accordingly - playerID.
        ArrayList<Integer> playerNumbers = (playerID == 1) ? player1Numbers : player2Numbers;
        int size = playerNumbers.size();

        //Using 3 nested loops to check if sum of any three numbers totals 15
        for (int i = 0; i < size; i++) {
            int a = playerNumbers.get(i);

            for (int j = i + 1; j < size; j++) {
                int b = playerNumbers.get(j);

                for (int k = j + 1; k < size; k++) {
                    int c = playerNumbers.get(k);

                    if (a + b + c == 15) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            Get15Interface obj = new Get15Impl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Get15Impl", obj);
            System.out.println("Server " + obj + " registered");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}