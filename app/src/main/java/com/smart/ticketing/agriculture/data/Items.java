package com.smart.ticketing.agriculture.data;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by dayanand on 05/04/18.
 */

@Data
public class Items implements Serializable{


    String objectId;

    String name;

    int price;

    int quantity;

    int duration;

    String harvestedDate;

    String imageUrl;

    int calculatedPrice;

    int availableQuantity;

    String username;

    String phone;

}
