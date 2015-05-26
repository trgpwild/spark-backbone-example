package org.trgp.spark.backbone.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.trgp.spark.backbone.model.Contato;
import org.trgp.spark.backbone.service.ContatoService;
import spark.Request;
import static spark.Spark.*;

public class ContatoController {

    private Gson gson;
    
    public ContatoController(Gson gson) {
        
        this.gson = gson;
        
        ContatoService contatoService = new ContatoService();

        get("/contato", (req, res) -> {
            return contatoService.listContato();
        }, gson::toJson);

        get("/contato/:id", (req, res) -> {
            return contatoService.getContatoById(getIdFromRequest(req));
        }, gson::toJson);

        delete("/contato/:id", (req, res) -> {
            return contatoService.deleteContatoById(getIdFromRequest(req));
        }, gson::toJson);

        post("/contato", (req, res) -> {
            return contatoService.createContato(getContatoFromRequest(req));
        }, gson::toJson);
        
        put("/contato/:id", (req, res) -> {
            return contatoService.updateContato(getContatoFromRequest(req));
        }, gson::toJson);
        
    }

    private static int getIdFromRequest(Request req) throws NumberFormatException {
        return Integer.parseInt(req.params("id"));
    }

    private Contato getContatoFromRequest(Request req) throws JsonSyntaxException {
        return gson.fromJson(req.body(), Contato.class);
    }
    
}
