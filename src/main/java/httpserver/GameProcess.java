/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import dao.GameDAO;
import dao.PlayerDAO;
import game.Game;
import game.Player;
import game.Trophy;
import gameprotocol.GameProtocolCode;
import gameprotocol.GameProcotolOperation;
import static gameprotocol.GameProcotolOperation.ADD_PLAYER;
import static gameprotocol.GameProcotolOperation.ADD_TROPHY;
import static gameprotocol.GameProcotolOperation.CLEAR_TROPHY;
import static gameprotocol.GameProcotolOperation.LIST_TROPHY;
import gameprotocol.GameProtocolRequest;
import gameprotocol.GameProtocolResponse;

import java.util.ArrayList;

/**
 *
 * @author romulo
 */
public class GameProcess {

    private Game game;
    private GameDAO gameDAO;
    private PlayerDAO playerDAO;
    private Player player;

    public GameProcess() {
//        this.gameDAO = new GameDAO();
//        this.playerDAO = new PlayerDAO();
        Game game = new Game(1);
//        game = gameDAO.obter(1);
        this.game = game;
        player = this.game.getPlayerIdPlayer();
    }

    protected GameProtocolResponse getGameResource(String path) {
        Object data = "";
        int code = 200;
        if (path.startsWith("/game/profile")) {
            String[] url = path.split("/");
            data = player.getTrophyList();
            code = 200;
        } else if (path.startsWith("/game")) {
            data = game;
            code = 200;
        }
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }

    protected GameProtocolResponse postGameResource(Request request) {
        Object data = "";
        int code = 500;
        GameDAO gameController = new GameDAO();
        Gson gson = new Gson();
//        "{status:OK,login:rmeloca,data:{asdfgretrhd}}"
        GameProtocolRequest gcpRequest = gson.fromJson(request.getValue(), GameProtocolRequest.class);
        String station = gcpRequest.getId();
        GameProcotolOperation operation = gcpRequest.getOperation();
        switch (operation) {
            case ADD_SCORE:
                code = 200;
                break;
            case ADD_TROPHY:
                LinkedTreeMap objectTrophy = (LinkedTreeMap) gcpRequest.getData();
//                Passava um path no Trophy
                Trophy trophy = new Trophy();
                player.setATrophy(trophy);
                code = 200;
                data = "";
                break;
            case LIST_TROPHY:
                ArrayList<Trophy> trophies = (ArrayList) player.getTrophyList();
                code = 200;
                data = gson.toJson(trophies);
                break;
            case CLEAR_TROPHY:
                break;
            case ADD_PLAYER:
                code = 200;
                data = "";
                break;
            default:
                code = 404;
                throw new AssertionError(operation.name());
        }
        playerDAO.update(player);

        gameController.update(game);
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }
}
