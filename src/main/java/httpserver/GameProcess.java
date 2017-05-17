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
import gameprotocol.GCPCode;
import gameprotocol.GCPOperation;
import static gameprotocol.GCPOperation.ADD_PLAYER;
import static gameprotocol.GCPOperation.ADD_TROPHY;
import static gameprotocol.GCPOperation.CLEAR_TROPHY;
import static gameprotocol.GCPOperation.LIST_TROPHY;
import gameprotocol.GCPRequest;
import gameprotocol.GCPResponse;

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

    protected GCPResponse getGameResource(String path) {
        Object data = "";
        GCPCode code = GCPCode.OK;
        if (path.startsWith("/game/profile")) {
            String[] url = path.split("/");
            data = player.getTrophyList();
            code = GCPCode.OK;
        } else if (path.startsWith(
                "/game")) {
            data = game;
            code = GCPCode.OK;
        }
        GCPResponse gcpResponse = new GCPResponse(code, data);
        return gcpResponse;
    }

    protected GCPResponse postGameResource(Request request, String path) {
        Object data = "";
        GCPCode code = GCPCode.OK;
        GameDAO gameController = new GameDAO();
        Gson gson = new Gson();
        GCPRequest gcpRequest = gson.fromJson(request.getValue(), GCPRequest.class
        );
        String station = gcpRequest.getStation();
        GCPOperation operation = gcpRequest.getOperation();
        switch (operation) {
            case ADD_TROPHY:
                LinkedTreeMap objectTrophy = (LinkedTreeMap) gcpRequest.getData();
//                Passava um path no Trophy
                Trophy trophy = new Trophy();
                player.setATrophy(trophy);
                code = GCPCode.OK;
                data = "";
                break;
            case LIST_TROPHY:
                ArrayList<Trophy> trophies = (ArrayList)player.getTrophyList();
                code = GCPCode.OK;
                data = gson.toJson(trophies);
                break;
            case CLEAR_TROPHY:
                break;
            case ADD_PLAYER:
                code = GCPCode.OK;
                data = "";
                break;
            default:
                throw new AssertionError(operation.name());
        }
        playerDAO.update(player);

        gameController.update(game);
        code = GCPCode.OK;
        data = "";
        GCPResponse gcpResponse = new GCPResponse(code, data);
        return gcpResponse;
    }
}
