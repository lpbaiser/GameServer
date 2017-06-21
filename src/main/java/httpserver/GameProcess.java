/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import controller.PlayerController;
import dao.GameDAO;
import dao.PlayerDAO;
import dao.TrophyDAO;
import game.Game;
import game.Player;
import game.Trophy;
import gameprotocol.GameProcotolOperation;
import static gameprotocol.GameProcotolOperation.ADD_PLAYER;
import static gameprotocol.GameProcotolOperation.ADD_TROPHY;
import static gameprotocol.GameProcotolOperation.CLEAR_TROPHY;
import static gameprotocol.GameProcotolOperation.LIST_TROPHY;
import gameprotocol.GameProtocolRequest;
import gameprotocol.GameProtocolResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author romulo
 */
public class GameProcess {

    private Game game;
    private final GameDAO gameDAO;
    private final PlayerController playerController;
    private final PlayerDAO playerDAO;
    private final TrophyDAO trophyDAO;

    public GameProcess(GameDAO gameDAO, PlayerDAO playerDAO, TrophyDAO trophyDAO) {
        this.gameDAO = gameDAO;
        this.playerDAO = playerDAO;
        this.playerController = new PlayerController();

        this.trophyDAO = new TrophyDAO();
    }

    protected GameProtocolResponse getGameResource(String path) {
        Object data = "";
        int code = 200;
        if (path.startsWith("/game/profile")) {
            String[] url = path.split("/");
//            data = player.getTrophyList();
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
        Gson gson = new Gson();
        GameProtocolRequest gcpRequest = gson.fromJson(request.getValue(), GameProtocolRequest.class);
        int idPlayer = gcpRequest.getId();
        GameProcotolOperation operation = gcpRequest.getOperation();
        switch (operation) {
            case ADD_SCORE:
                LinkedTreeMap jScore = (LinkedTreeMap) gcpRequest.getData();
                double score = (Double) jScore.get("score");
                double newScore = playerController.updateScore(score, idPlayer);
                code = 200;
                data = "Pontuação Adicionada: " + newScore;
                break;
            case ADD_TROPHY:
                LinkedTreeMap objectTrophy = (LinkedTreeMap) gcpRequest.getData();
//                Passava um path no Trophy
                Trophy trophy = new Trophy();
//                player.setATrophy(trophy);
                code = 200;
                data = "";
                break;
            case LIST_TROPHY:
                ArrayList<Trophy> trophies = (ArrayList) playerController.getPlayerById(gcpRequest.getId()).getTrophyList();
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
                data = "Erro, não foi possível encontrar uma solução para requisição";
                throw new AssertionError(operation.name());
        }
//        playerDAO.update(player);

//        gameController.update(game);
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }
}
