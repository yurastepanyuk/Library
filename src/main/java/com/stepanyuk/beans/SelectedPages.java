package com.stepanyuk.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@RequestScoped
public class SelectedPages {

    private Map<String, Integer> qtyShowPages = new HashMap<>();

    public SelectedPages() {
        qtyShowPages.put("2",2);
        qtyShowPages.put("5",5);
        qtyShowPages.put(" 10",10);
    }

    public Map<String, Integer> getQtyShowPages() {
        return qtyShowPages;
    }
}
