package com.webcheckers.ui;

import com.webcheckers.appl.CheckersCenter;
import spark.*;
import java.util.*;

/**
 *
 * The {@code POST /name} route handler.
 *
 */
public class PostNameRoute implements TemplateViewRoute {

    //
    // Attributes
    //

    private static final String ERROR_ATTR_MSG = "Username already taken. Please enter another.";
    private final CheckersCenter checkersCenter;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /name} route handler.
     *
     * @param checkersCenter
     *    The {@link CheckersCenter} for the application.
     *
     * @throws NullPointerException
     *    when the {@code gameCenter} parameter is null
     */
    PostNameRoute(final CheckersCenter checkersCenter) {
        // validation
        Objects.requireNonNull(checkersCenter, "checkersCenter must not be null");
        this.checkersCenter = checkersCenter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelAndView handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        String name = request.queryParams("playerName");
        return storeName(vm, name, response);
    }

    /**
     *
     * @param playerNames
     * @param name
     *
     * @return
     *      returns whether or not an entered name is available
     */
    public boolean nameAvailable(List<String> playerNames, String name)
    {
        return playerNames.contains(name);
    }

    /**
     *
     * @param vm
     * @param name
     * @param response
     *
     * @return
     *      returns a ModelAndView dependent on whether or not the name is available
     */
    public  ModelAndView storeName(Map<String, Object> vm, String name, Response response){
        if(this.checkersCenter.isPlayerListEmpty())
        {
            checkersCenter.add(name);
            return successfulAdd(vm, name, response);
        }
        else
        {
            boolean nameAvailable = nameAvailable(this.checkersCenter.getAllPlayers(),name);
            if(!nameAvailable)
            {
                checkersCenter.add(name);
                return successfulAdd(vm, name, response);
            }
            else
            {
                return error();
            }
        }
    }

    /**
     *
     * @return
     *      return signin page because name is not available, with error message displayed
     */
    public synchronized ModelAndView error()
    {
        final Map<String, Object> vm = new HashMap<>();

        vm.put("title", HomeController.TITLE_ATTR_MSG);
        vm.put("loginMessage", GetSigninRoute.LOGIN_ATTR_MSG);
        vm.put("errorMessage", ERROR_ATTR_MSG);
        return new ModelAndView(vm, "signin.ftl");
    }

    /**
     *
     * gets called when a name is stored successfully, redirects user to "/game-menu"
     *
     * playerName and errorPath stored as URL parameters
     */
    public static ModelAndView successfulAdd(Map<String, Object> vm, String name, Response response)
    {
        response.redirect(String.format("/game-menu?playerName=%s&errorPath=false",name));
        return null;
    }


}
