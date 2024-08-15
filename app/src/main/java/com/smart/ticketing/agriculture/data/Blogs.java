package com.smart.ticketing.agriculture.data;

import java.io.Serializable;

import lombok.Data;

@Data
public class Blogs implements Serializable {

    String objectId;

    String name;

    String username;

    String description;

    int likeCount;

    int dislikeCount;

    String topic;

    String link;

}
