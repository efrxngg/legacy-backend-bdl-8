package com.bancodellitoral.demo.dto;

import java.util.Objects;

public class RSCBC {

    // Campos del DTO
    private String id;
    private String idBodega;

    public String getId() {
        return id;
    }

    public RSCBC setId(String id) {
        this.id = id;
        return this;
    }

    public String getIdBodega() {
        return idBodega;
    }

    public RSCBC setIdBodega(String idBodega) {
        this.idBodega = idBodega;
        return this;
    }
}

