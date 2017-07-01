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
 */
public class GameProcess {

    private Game game;
    private final GameDAO gameDAO;
    private final PlayerController playerController;
    private final PlayerDAO playerDAO;
    private final TrophyDAO trophyDAO;
    private LevelDAO levelDAO;
    private Player player;
    private Player jPlayer;
    private Level level;

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

    protected GameProtocolResponse postGameResource(Request request) throws Exception {

        Object data;
        int code = 500;
        String password;
        List<Trophy> trophyList;
        Trophy trophy;
        Gson gson = new Gson();
        LinkedTreeMap jData = null;
        GameProcotolOperation operation = null;
        GameProtocolRequest gcpRequest = gson.fromJson(request.getValue(), GameProtocolRequest.class);
        String idPlayer = gcpRequest.getId();
        if (idPlayer != null) {
            jPlayer = playerController.getPlayerById(idPlayer);
            player = playerDAO.obter(idPlayer);
            operation = gcpRequest.getOperation();
            jData = (LinkedTreeMap) gcpRequest.getData();
            switch (operation) {
                case ADD_SCORE:
                    LinkedTreeMap jScore = (LinkedTreeMap) gcpRequest.getData();
                    double score = (Double) jScore.get("score");
                    double newScore = playerController.updateScore(score, idPlayer);
                    code = 200;
                    data = "Pontuação Adicionada: " + newScore;
                    break;
                case ADD_TROPHY:
                    trophy = new Trophy((String) jData.get("name"), (double) jData.get("xp"), (String) jData.get("title"), (String) jData.get("description"));
                    trophyList = player.getTrophyList();
                    trophyList.add(trophy);
                    player.setTrophyList(trophyList);
                    playerDAO.update(player);
                    code = 200;
                    data = "ok";
                    break;
                case LIST_TROPHY:
                    trophyList = player.getTrophyList();
                    code = 200;
                    data = gson.toJson(trophyList);
                    break;
                case GET_TROPHY:
                    trophyList = player.getTrophyList();
                    String trophyName;
                    trophyName = (String) jData.get("");
                    for (Trophy trophy1 : trophyList) {
                        if (trophy1.getNameTrophy().equals(trophyName)) {
                            data = gson.toJson(trophy1);
                            code = 200;
                            break;
                        }
                    }
                    code = 200;
                    data = "Não encontrado";
                    break;

                case CLEAR_TROPHY:
                    trophyList = player.getTrophyList();
                    trophyList.clear();
                    player.setTrophyList(trophyList);
                    playerDAO.update(player);
                    code = 200;
                    data = "ok";
                    break;
                case ADD_PLAYER:
                    code = 200;
                    data = "ok";
                    break;
                case ADD_PROFILE:
                    if (player == null) {
                        password = (String) jData.get("password");
                        player = new Player(idPlayer, password, 0, 1);
                        playerDAO.insert(player);
                        code = 200;
                        data = "ok";
                    } else {
                        code = 200;
                        data = "Id do usuário já existe em nossos servidores";

                    }
                    break;
                case QUERY_PROFILE:
                    password = (String) jData.get("password");
                    if (player.getSenha().equals(password)) {
                        List<Level> levelList = player.getLevelList();
                        if (levelList.isEmpty()) {
                            level = new Level(0, 0, 0, 0, 0, 0);
                        } else {
                            level = levelList.get(player.getIdLevelAtual());
                        }
                        ArrayList<Object> objectList = new ArrayList<>();
                        objectList.add(level);
                        objectList.add(jPlayer);
                        trophyList = player.getTrophyList();
                        objectList.add(trophyList);
                        code = 200;
                        data = gson.toJson(objectList);
                    } else {
                        code = 401;
                        data = "Usuário ou senha inválidos.";
                    }
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
                    data = "ok";
                    break;
                default:
                    code = 200;
                    data = "Erro, não foi possível encontrar uma solução para requisição";
                    throw new Exception("Operação não conhecida");
            }
        } else {
            code = 200;
            data = "Erro, não foi possível encontrar uma solução para requisição";
        }

//        playerDAO.update(player);
//        gameController.update(game);
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        return gcpResponse;
    }
}
