package org.trgp.spark.backbone;

import com.google.gson.Gson;
import org.trgp.spark.backbone.controller.ContatoController;
import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        port(8080);

        Gson gson = new Gson();

        staticFileLocation("/webapp");

        new ContatoController(gson);

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });

    }

}
