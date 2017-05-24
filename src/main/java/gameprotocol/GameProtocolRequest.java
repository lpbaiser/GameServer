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

    private final String station;
    private final String operation;
    private Object data;

    public GameProtocolRequest(String station, GameProcotolOperation operation, Object data) {
        this.station = station;
        this.operation = operation.name();
        this.data = data;
    }

    public String getStation() {
        return station;
    }

    public GameProcotolOperation getOperation() {
        return GameProcotolOperation.valueOf(operation);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
