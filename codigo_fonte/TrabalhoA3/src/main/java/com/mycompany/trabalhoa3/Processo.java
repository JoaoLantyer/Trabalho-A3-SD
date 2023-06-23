package com.mycompany.trabalhoa3;

public class Processo {
    private Integer identificador;
    private String host;
    private Integer port;
    public boolean isLider = false;

    public boolean isLider() {
        return isLider;
    }

    public void setIsLider(boolean isLider) {
        this.isLider = isLider;
    }

    public Processo(Integer identificador, String host, Integer port){
        this.identificador = identificador;
        this.host = host;
        this.port = port;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
