package com.hmy.euclidean;

import org.json.JSONObject;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/game")
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONObject game = new JSONObject();
        game.put("name", "Tango");
        game.put("price", "70");

        response.getWriter().print(game);
    }

    public void destroy() {
    }
}