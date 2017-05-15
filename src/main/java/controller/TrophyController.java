
package controller;

import dao.TrophyDAO;
import game.Trophy;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class TrophyController {

    TrophyDAO trophyDAO;

    public TrophyController() {
        trophyDAO = new TrophyDAO();
    }

    public List<Trophy> listAllTrophy() {
        return null;
    }

}
