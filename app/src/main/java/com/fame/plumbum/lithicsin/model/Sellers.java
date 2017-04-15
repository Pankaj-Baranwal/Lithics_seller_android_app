package com.fame.plumbum.lithicsin.model;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class Sellers {
    private String id, seller_name, contact_person, seller_id, seller_sku;

    public Sellers(String id, String seller_name, String contact_person, String seller_id, String seller_sku){
        this.id = id;
        this.seller_name = seller_name;
        this.contact_person = contact_person;
        this.seller_id = seller_id;
        this.seller_sku = seller_sku;
    }

    public String getContact_person() {
        return contact_person;
    }

    public String getId() {
        return id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getSeller_sku() {
        return seller_sku;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public void setSeller_sku(String seller_sku) {
        this.seller_sku = seller_sku;
    }
}
