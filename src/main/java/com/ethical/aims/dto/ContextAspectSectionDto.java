package com.ethical.aims.dto;

import java.util.ArrayList;
import java.util.List;

public class ContextAspectSectionDto {
    public Long id;
    public String aspectName;
    public Integer displayOrder;
    public List<ContextAspectDto> children = new ArrayList<>();
}
