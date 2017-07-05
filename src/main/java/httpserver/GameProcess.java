/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import controller.LevelController;
import controller.PlayerController;
import dao.GameDAO;
import dao.ImageDAO;
import dao.LevelDAO;
import dao.PlayerDAO;
import dao.TrophyDAO;
import game.Game;
import game.Images;
import game.Level;
import game.Player;
import game.Profile;
import game.Ranking;
import game.Trophy;
import gameprotocol.GameProcotolOperation;
import static gameprotocol.GameProcotolOperation.ADD_PROFILE;
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
    private final LevelController levelController;
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
        this.levelController = new LevelController();

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

        System.out.println("-----------------------------");
        System.out.println(request.getValue());
        System.out.println("-----------------------------");
        Object data = null;
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
            operation = gcpRequest.getOperation();
            player = playerDAO.obter(idPlayer);
            //se eu nao tiver o profile requisitado
            if (operation != ADD_PROFILE && player == null && !gcpRequest.isIsServer()) {

                HttpServer.serverComunication.sendRequest();
                gcpRequest.setIsServer(true);
                HttpServer.serverComunication.setGameProtocolRequest(gcpRequest);
                HttpServer.serverComunication.getGameProtocolResponses().clear();
                HttpServer.serverComunication.setTimeout(System.currentTimeMillis() + 30000);
                HttpServer.serverComunication.setEstouPerguntando(true);
                while (System.currentTimeMillis() < HttpServer.serverComunication.getTimeout()) {
                }
                HttpServer.serverComunication.setEstouPerguntando(false);
                ArrayList<GameProtocolResponse> gameProtocolResponses = HttpServer.serverComunication.getGameProtocolResponses();
                //verificar se alguma resposta é o profile requerido
                for (GameProtocolResponse gameProtocolResponse : gameProtocolResponses) {
                    return gameProtocolResponse;
                }
                code = 401;
                data = "Usuário ou senha inválidos.";
//        Retornar erro protocol 
//se o server perguntar e eu nao tiver
            } else if (operation == ADD_PROFILE) {
                if (player == null) {
                    jData = (LinkedTreeMap) gcpRequest.getData();
                    password = (String) jData.get("password");
                    player = new Player(idPlayer, password, 0, 1);
                    playerDAO.insert(player);
                    code = 200;
                    data = "ok";
                } else {
                    code = 200;
                    data = "Id do usuário já existe em nossos servidores";

                }

            } else {
                jPlayer = playerController.getPlayerById(idPlayer);
                if (!(gcpRequest.getData() instanceof String)) {
                    jData = (LinkedTreeMap) gcpRequest.getData();
                }
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
                        playerController.updateTrophyList(player, trophy);
                        playerDAO.update(player);
                        code = 200;
                        data = "ok";
                        break;
                    case LIST_TROPHY:
                        trophyList = playerController.getTrophyList(player);
                        code = 200;
                        data = trophyList;
                        break;
                    case GET_TROPHY:
                        trophyList = player.getTrophyList();
                        String trophyName;
                        trophyName = (String) jData.get("");
                        for (Trophy trophy1 : trophyList) {
                            if (trophy1.getNameTrophy().equals(trophyName)) {
                                data = trophy1;
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
                    case QUERY_PROFILE:
                        password = (String) jData.get("password");
                        if (player.getSenha().equals(password)) {
                            List<Level> levelList = player.getLevelList();
                            if (levelList.isEmpty()) {
                                level = new Level(0, 0, 0, 0, 0, 0);
                            } else {
                                level = levelController.getLevelAtual(levelList, player.getIdLevelAtual());
                            }
//                            ArrayList<Object> objectList = new ArrayList<>();
//                            objectList.add(level);
//                            objectList.add(jPlayer);
//                        trophyList = player.getTrophyList();
//                        objectList.add(trophyList);
                            Profile profile = new Profile(level, player);
                            code = 200;
                            data = profile;
                            data = level;
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
                        playerController.updateLevel(level, player);
                        code = 200;
                        data = "ok";
                        break;
                    case GET_RANKING:
                        List<Ranking> teenBetter = levelController.getTeenBetter();
                        data = teenBetter;
                        code = 200;
                        break;
                    case LIST_MEDIA:
                        data = playerController.listMedia(player);
                        code = 200;
                        break;
                    case SAVE_MEDIA:
                        String img = (String) jData.get("src");
                        Images image = new Images(img, player);
                        ImageDAO imageDAO = new ImageDAO();
                        imageDAO.insert(image);
                        break;
                    default:
                        code = 200;
                        data = "Erro, não foi possível encontrar uma solução para requisição";
                        throw new Exception("Operação não conhecida");
                }
            }
        } else {
            code = 200;
            data = "Erro, não foi possível encontrar uma solução para requisição";
        }

//        playerDAO.update(player);
//        gameController.update(game);
        GameProtocolResponse gcpResponse = new GameProtocolResponse(code, data);
        gcpRequest.setIsServer(false);
        return gcpResponse;
    }
}
