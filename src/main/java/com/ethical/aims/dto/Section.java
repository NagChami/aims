package com.ethical.aims.dto;

import lombok.Data;

import java.util.List;

@Data
public class Section {
    private long id;
    private String name;
    private String description;
    private List<String> keys;

}
