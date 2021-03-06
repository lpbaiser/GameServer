/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameprotocol;

/**
 *
 * @author Marco
 */
public class GameProtocolRequest {

    private final String id;
    private final String op;
    private Object data;
    private boolean isServer;

    public GameProtocolRequest(String id, GameProcotolOperation op, Object data, boolean isServer) {
        this.id = id;
        this.isServer = isServer;
        this.op = op.name();
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public GameProcotolOperation getOperation() {
        return GameProcotolOperation.valueOf(op.toUpperCase().replace("-", "_"));
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isIsServer() {
        return isServer;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

}
