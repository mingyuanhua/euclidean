package com.hmy.euclidean.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Use @Controller to mark a class its role as a web component,
// so the spring mvc will register the methods which annotated the @RequestMapping.

// Use the @RequestMapping annotation to define REST API, such as HTTP URL, method, etc.
@Controller
public class GameController {
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public void getGame(@RequestParam(value = "game_name", required = false) String gameName, HttpServletResponse response) throws IOException {

    }
}
