package org.trgp.spark.backbone.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.trgp.spark.backbone.model.Contato;

public class ContatoService {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1);

    Map<Integer, Contato> contatos = new HashMap<>();

    public Collection<Contato> listContato() {
        return contatos.values();
    }

    public Contato getContatoById(Integer contatoId) {
        return contatos.get(contatoId);
    }

    public Contato deleteContatoById(Integer contatoId) {
        if (!contatos.containsKey(contatoId)) {
            throw new RuntimeException("Id [" + contatoId + "] doesn't exists");
        }
        Contato contato = getContatoById(contatoId);
        contatos.remove(contatoId);
        return contato;
    }

    public Contato createContato(Contato contato) {
        if (contatos.containsKey(contato.getId())) {
            throw new RuntimeException("Id [" + contato.getId() + "] already exists");
        }
        contato.setId(getNextId());
        contatos.put(contato.getId(), contato);
        return contato;
    }

    public Contato updateContato(Contato contato) {
        if (!contatos.containsKey(contato.getId())) {
            throw new RuntimeException("Id [" + contato.getId() + "] doesn't exists");
        }
        contatos.put(contato.getId(), contato);
        return contato;
    }
    
    private Integer getNextId() {
        return ATOMIC_INTEGER.getAndIncrement();
    }

}
