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
public class GameProtocolResponse {

    private final int code;
    private final Object data;

    public GameProtocolResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

}
