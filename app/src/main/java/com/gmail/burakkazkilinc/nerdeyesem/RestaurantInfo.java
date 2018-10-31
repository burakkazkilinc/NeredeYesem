package com.gmail.burakkazkilinc.nerdeyesem;

public class RestaurantInfo {

    //restoran ismi
    //restoran türü
    //restoran adresi
    //restoran ratingi
    //restoran url
    private String name,type,address,url,rating_text,rating;


    public RestaurantInfo(String name, String type, String address, String url, String rating_text, String rating) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.url = url;
        this.rating_text = rating_text;
        this.rating = rating;
    }

    public RestaurantInfo(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRating_text() {
        return rating_text;
    }

    public void setRating_text(String rating_text) {
        this.rating_text = rating_text;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String toString()
    {
        return "Restoran:"+ name+"\nTipi:"+ type+"\nAdresi:"+ address+"\nPuan: "+rating +" - "+ rating_text;
    }


}
