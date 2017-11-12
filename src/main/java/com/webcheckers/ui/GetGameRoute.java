package com.webcheckers.ui;

import com.webcheckers.appl.CheckersCenter;
import com.webcheckers.model.Board;
import com.webcheckers.model.CheckersGame;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * The {@code GET /game} route handler.
 *
 */
public class GetGameRoute implements TemplateViewRoute {

    private final CheckersCenter checkersCenter;
    static final String VIEW_NAME = "game.ftl";

    public GetGameRoute(final CheckersCenter checkersCenter){
        // validation
        Objects.requireNonNull(checkersCenter, "checkersCenter must not be null");
        this.checkersCenter = checkersCenter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();
        vm.put(Message.TITLE_ATTR, Message.TITLE_ATTR_MSG);
        final String player = request.queryParams("playerName");
        final String opponent = request.queryParams("opponent");

        vm.put("playerName", player);
        vm.put("opponentName", opponent);

        CheckersGame game = checkersCenter.getGame(player);
        if(!checkersCenter.getInGamePlayers().contains(player) && game != null){
            removeGame(game);
            response.redirect(String.format("/game-over?playerName=%s&opponentName=%s&message=forfeit",player,opponent));
        }
        else if(!checkersCenter.getInGamePlayers().contains(player) && game == null){
            response.redirect(String.format("/game-over?playerName=%s&opponentName=%s&message=lost",player,opponent));
        }

        if(game == null){
            game = new CheckersGame(player,opponent);
            checkersCenter.getGamesList().add(game);
            vm.put("board", new Board());
        }
        else {
            vm.put("board", game.getBoard());
        }

        if(game.getPlayer().equals(player)){
            vm.put("playerColor", "RED");
            vm.put("opponentColor", "WHITE");
        }
        else
        {
            vm.put("playerColor", "WHITE");
            vm.put("opponentColor", "RED");
        }

        vm.put("isMyTurn", game.isPlayerTurn(player));

        return new ModelAndView(vm , VIEW_NAME);
    }

    private void removeGame(CheckersGame game) {
        checkersCenter.getGamesList().remove(game);
    }
}
