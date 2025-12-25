package com.ethical.aims.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private Long companyId;
    private String name;
    private String code;
    private String description;

}
