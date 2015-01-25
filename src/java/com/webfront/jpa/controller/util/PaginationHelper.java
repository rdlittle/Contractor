package com.webfront.jpa.controller.util;

import javax.faces.model.DataModel;

public abstract class PaginationHelper {

    private final int pageSize;
    private int page;
    private String order;

    public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
    }

    public abstract int getItemsCount();

    public abstract DataModel createPageDataModel();

    public String getOrder() {
        return order;
    }

    public void setOrder(String ord) {
        order = ord;
    }
    
    public void setPage(int p) {
        page=p;
    }

    public int getPageFirstItem() {
        return page * pageSize;
    }
    
    public int getPageLastItem() {
        int i = getPageFirstItem() + pageSize - 1;
        int count = getItemsCount() - 1;
        if (i > count) {
            i = count;
        }
        if (i < 0) {
            i = 0;
        }
        return i;
    }
    
    public void gotoLastPage() {
        while(isHasNextPage()) {
            nextPage();
        }
    }

    public boolean isHasNextPage() {
        return (page + 1) * pageSize + 1 <= getItemsCount();
    }

    public void nextPage() {
        if (isHasNextPage()) {
            page++;
        }
    }

    public boolean isHasPreviousPage() {
        return page > 0;
    }

    public void previousPage() {
        if (isHasPreviousPage()) {
            page--;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void lastPage() {
        while (isHasNextPage()) {
            page++;
        }
    }
}
