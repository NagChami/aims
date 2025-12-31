package com.ethical.aims.service.impl;

import com.ethical.aims.dao.impl.ContextAspectDaoImpl;
import com.ethical.aims.dto.Section;
import com.ethical.aims.entity.ContextAspect;
import com.ethical.aims.service.AimsBasicService;
import org.springframework.stereotype.Service;

import java.util.*;

import com.google.gson.Gson;

@Service
public class AimsBasicServiceImpl implements AimsBasicService {

    private final ContextAspectDaoImpl contextAspectDao;
    private final Gson gson = new Gson();

    public AimsBasicServiceImpl(ContextAspectDaoImpl contextAspectDao /*, other deps */) {
        this.contextAspectDao = contextAspectDao;
    }

    public List<Section> getAvailableSection() {
        return null;
    }


    @Override
    public String getContextRegisterDetails(Long contextRegisterId) {

        System.out.println("In getContextRegisterDetails() - reached here --- 1");

        // 1) Load all aspects for register (ordered)
        String jpql = "SELECT c FROM ContextAspect c " +
                "WHERE c.contextRegisterId = :rid " +
                "ORDER BY c.displayOrder ASC";

        Map<String, Object> params = new HashMap<>();
        params.put("rid", contextRegisterId);

        List<ContextAspect> rows = contextAspectDao.list(jpql, params);
        System.out.println("In getContextRegisterDetails() - reached here --- 2222");

        // 2) Separate sections + items
        List<ContextAspect> sections = new ArrayList<>();
        Map<Long, List<ContextAspect>> itemsByParent = new LinkedHashMap<>();

        System.out.println("In getContextRegisterDetails() - reached here --- 33333:rows.."+rows.size());

        for (ContextAspect r : rows) {
            if ("SECTION".equalsIgnoreCase(r.getRowType())) {
                sections.add(r);
            } else if ("ITEM".equalsIgnoreCase(r.getRowType())) {
                Long parentId = r.getParentContextAspectId();
                if (parentId != null) {
                    itemsByParent.computeIfAbsent(parentId, k -> new ArrayList<>()).add(r);
                }
            }
        }

        // 3) Build JSON structure
        List<Map<String, Object>> sectionList = new ArrayList<>();

        for (ContextAspect sec : sections) {
            Map<String, Object> secJson = new LinkedHashMap<>();
            secJson.put("id", sec.getContextAspectId());
            secJson.put("rowType", "SECTION");
            secJson.put("aspectName", sec.getAspectName());
            secJson.put("displayOrder", sec.getDisplayOrder());

            List<Map<String, Object>> children = new ArrayList<>();
            List<ContextAspect> kids = itemsByParent.getOrDefault(sec.getContextAspectId(), Collections.emptyList());

            for (ContextAspect kid : kids) {
                Map<String, Object> kidJson = new LinkedHashMap<>();
                kidJson.put("id", kid.getContextAspectId());
                kidJson.put("rowType", "ITEM");
                kidJson.put("aspectName", kid.getAspectName());
                kidJson.put("descriptionText", kid.getDescriptionText());
                kidJson.put("parentId", kid.getParentContextAspectId());
                kidJson.put("displayOrder", kid.getDisplayOrder());
                kidJson.put("valueDisplayType", kid.getValueDisplayType());
                children.add(kidJson);
            }

            secJson.put("children", children);
            sectionList.add(secJson);
        }

        // 4) Wrap (future: add other tabs data too)
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("contextRegisterId", contextRegisterId);
        root.put("roleAndContext", sectionList);

        System.out.println("In the getContextRegisterDetails:::::[{}]"+root);
        return gson.toJson(root);
    }

}
