/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameprotocol;

/**
 *
 * @author romulo
 */
public class GCPResponse {

    private final GCPCode code;
    private final Object data;

    public GCPResponse(GCPCode code, Object data) {
        this.code = code;
        this.data = data;
    }

}
