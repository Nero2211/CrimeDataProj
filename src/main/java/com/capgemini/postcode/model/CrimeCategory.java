package com.capgemini.postcode.model;

public class CrimeCategory {

    private String url;
    private String name;

    public CrimeCategory(){

    }

    public CrimeCategory(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
