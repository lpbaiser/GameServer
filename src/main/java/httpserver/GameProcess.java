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
import dao.LevelDAO;
import dao.PlayerDAO;
import dao.TrophyDAO;
import game.Game;
import game.Level;
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
    private LevelDAO levelDAO;
    private Player player;

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
        String idPlayer = gcpRequest.getId();
        player = playerDAO.obter(idPlayer);
        GameProcotolOperation operation = gcpRequest.getOperation();
        LinkedTreeMap jData = (LinkedTreeMap) gcpRequest.getData();
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
//                ArrayList<Trophy> trophies = (ArrayList) playerController.getPlayerById(gcpRequest.getId()).getTrophyList();
//                ArrayList<Trophy> trophies = (ArrayList) playerController.getPlayerById("a");

                code = 200;
//                data = gson.toJson(trophies);
                break;
            case CLEAR_TROPHY:
                break;
            case ADD_PLAYER:
                code = 200;
                data = "";
                break;
            case QUERY_PROFILE:
                code = 200;
                data = "";
                break;
            case SAVE_POINT:
                double coins = (double) jData.get("coins");
                int life = (int) (double) jData.get("life");
                int xp = (int) (double) jData.get("xp");
                double sp_x = (double) jData.get("save_point_x");
                double sp_y = (double) jData.get("save_point_y");
                double sp_id = (double) jData.get("save_point_id");
                Level level = new Level(coins, sp_id, sp_y, sp_id, life, xp);
                level.setPlayerNomePlayer(player);
                level.setIdLevel(1);
                levelDAO = new LevelDAO();
                levelDAO.insert(level);
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
