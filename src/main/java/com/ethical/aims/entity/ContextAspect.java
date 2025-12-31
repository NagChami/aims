package com.ethical.aims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CONTEXT_ASPECT")
public class ContextAspect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTEXT_ASPECT_ID")
    private Long contextAspectId;

    @Column(name = "CONTEXT_REGISTER_ID", nullable = false)
    private Long contextRegisterId;

    @Column(name = "ROW_TYPE", nullable = false)
    private String rowType; // "SECTION" or "ITEM"

    @Column(name = "ASPECT_NAME", nullable = false)
    private String aspectName;

    @Column(name = "DESCRIPTION_TEXT")
    private String descriptionText;

    @Column(name = "PARENT_CONTEXT_ASPECT_ID")
    private Long parentContextAspectId;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "VALUE_DISPLAY_TYPE")
    private String valueDisplayType;

    // getters/setters
    public Long getContextAspectId() { return contextAspectId; }
    public void setContextAspectId(Long v) { this.contextAspectId = v; }

    public Long getContextRegisterId() { return contextRegisterId; }
    public void setContextRegisterId(Long v) { this.contextRegisterId = v; }

    public String getRowType() { return rowType; }
    public void setRowType(String v) { this.rowType = v; }

    public String getAspectName() { return aspectName; }
    public void setAspectName(String v) { this.aspectName = v; }

    public String getDescriptionText() { return descriptionText; }
    public void setDescriptionText(String v) { this.descriptionText = v; }

    public Long getParentContextAspectId() { return parentContextAspectId; }
    public void setParentContextAspectId(Long v) { this.parentContextAspectId = v; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer v) { this.displayOrder = v; }

    public String getValueDisplayType() {
        return valueDisplayType;
    }

    public void setValueDisplayType(String valueDisplayType) {
        this.valueDisplayType = valueDisplayType;
    }
}
