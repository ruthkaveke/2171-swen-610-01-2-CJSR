package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * The Web Controller for the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class HomeController implements TemplateViewRoute {

  static final String TITLE_ATTR_MSG = "Welcome!";

  @Override
  public ModelAndView handle(Request request, Response response) {
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", TITLE_ATTR_MSG);
    return new ModelAndView(vm , "home.ftl");
  }

}