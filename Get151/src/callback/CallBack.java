package callback;

import java.awt.*;
import java.rmi.*;
import java.util.ArrayList;

public interface CallBack extends Remote {
    /** The server notifies the client for taking a turn */
    public void takeTurn(boolean turn) throws RemoteException;

    /** The server sends a message to be displayed by the client */
    public void notify(String message) throws RemoteException;

    /** The server notifies a client of the other player's move */
    public void mark(int number) throws RemoteException;

    /** The server highlights the board of the player
     *  with the corresponding color:
     *  green - notified player is winner
     *  red - notified player is looser
    **/
    public void highlightWinningNumbers(Color color) throws RemoteException;
}