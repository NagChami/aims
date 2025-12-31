package com.ethical.aims.dto;

public class ContextAspectDto {
    public Long id;
    public String rowType; // "SECTION" | "ITEM"
    public String aspectName;
    public String descriptionText;
    public Long parentId;
    public Integer displayOrder;
}
