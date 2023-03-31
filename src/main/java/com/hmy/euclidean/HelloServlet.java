package com.hmy.euclidean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmy.euclidean.entity.response.Game;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/game")
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        Game.Builder builder = new Game.Builder();
        builder.setName("Pokemon Go");
        builder.setPrice(9.99);

        Game game = builder.build();
        response.getWriter().print(mapper.writeValueAsString(game));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // read game info from request body
        JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));
        String name = jsonRequest.getString("name");
        float price = jsonRequest.getFloat("price");

        // print game info to ide console
        System.out.println("Name is: " + name);
        System.out.println("Price is: " + price);

        // return status = ok as response body to the client
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "ok");
        response.getWriter().print(jsonResponse);
    }
}