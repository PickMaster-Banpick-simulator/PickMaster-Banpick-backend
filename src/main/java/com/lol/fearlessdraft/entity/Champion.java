package com.lol.fearlessdraft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champion implements Serializable {

    private String id;
    private String name;
    private String image;
}
