package callback;

import clientRMI.Get15ClientRMI;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {
    // The client will be called by the server through callback
    private Get15ClientRMI thisClient;

    /** Constructor */
    public CallBackImpl(Object client) throws RemoteException {
        thisClient = (Get15ClientRMI)client;
    }

    /** The server notifies the client for taking a turn */
    public void takeTurn(boolean turn) throws RemoteException {
        thisClient.setMyTurn(turn);
    }

    /** The server sends a message to be displayed by the client */
    public void notify(String message) throws RemoteException {
        thisClient.setMessage(message);
    }

    /** The server notifies a client of the other player's move */
    public void mark(int number) throws RemoteException {
        thisClient.mark(number);
    }

    /** The server highlights the board of the player
     *  with the corresponding color:
     *  green - notified player is winner
     *  red - notified player is looser
    **/
    public void highlightWinningNumbers(Color color) throws RemoteException {
        thisClient.highlightNumbers(color);
    }
}
