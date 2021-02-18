package server;

import callback.CallBack;

import java.rmi.*;

public interface Get15Interface extends Remote {
    /** Connect to the Get15 server and return the token.
     * If the returned token is 3, the client is not connected to the server
     **/
    public int connect(CallBack client) throws RemoteException;

    /** A client invokes this method to notify the server of its move and update their numbers */
    public void myMove(int number, int playerID) throws RemoteException;
}