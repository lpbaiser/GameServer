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
        this.gameDAO = new GameDAO();
        this.playerDAO = new PlayerDAO();
        Game game = new Game(1);
        game = gameDAO.obter(game);
        this.game = game;
        player = this.game.getPlayerIdPlayer();
    }

    protected GameProtocolResponse getGameResource(String path) {
        Object data = "";
        GameProtocolCode code = GameProtocolCode.OK;
        if (path.startsWith("/game/profile")) {
            String[] url = path.split("/");
            data = player.getTrophyList();
            code = GameProtocolCode.OK;
        } else if (path.startsWith(
                "/game")) {
            data = game;
            code = GameProtocolCode.OK;
        }
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }

    protected GameProtocolResponse postGameResource(Request request, String path) {
        Object data = "";
        GameProtocolCode code = GameProtocolCode.OK;
        GameDAO gameController = new GameDAO();
        Gson gson = new Gson();
        GameProtocolRequest gcpRequest = gson.fromJson(request.getValue(), GameProtocolRequest.class
        );
        String station = gcpRequest.getStation();
        GameProcotolOperation operation = gcpRequest.getOperation();
        switch (operation) {
            case ADD_TROPHY:
                LinkedTreeMap objectTrophy = (LinkedTreeMap) gcpRequest.getData();
//                Passava um path no Trophy
                Trophy trophy = new Trophy();
                player.setATrophy(trophy);
                code = GameProtocolCode.OK;
                data = "";
                break;
            case LIST_TROPHY:
                ArrayList<Trophy> trophies = (ArrayList)player.getTrophyList();
                code = GameProtocolCode.OK;
                data = gson.toJson(trophies);
                break;
            case CLEAR_TROPHY:
                break;
            case ADD_PLAYER:
                code = GameProtocolCode.OK;
                data = "";
                break;
            default:
                throw new AssertionError(operation.name());
        }
        playerDAO.update(player);

        gameController.update(game);
        code = GameProtocolCode.OK;
        data = "";
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }
}
