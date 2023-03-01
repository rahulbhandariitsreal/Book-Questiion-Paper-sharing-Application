package com.example.pdfapp.model;



public class PDF {


    private String name;
    private String pdfURI;
    private String relatedto;

    public PDF(String name, String pdfURI, String relatedto) {
        this.name = name;
        this.pdfURI = pdfURI;
        this.relatedto = relatedto;
    }

    public PDF() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfURI() {
        return pdfURI;
    }

    public void setPdfURI(String pdfURI) {
        this.pdfURI = pdfURI;
    }

    public String getRelatedto() {
        return relatedto;
    }

    public void setRelatedto(String relatedto) {
        this.relatedto = relatedto;
    }
}
