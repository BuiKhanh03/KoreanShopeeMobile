package com.example.koreanshopee.model;
import java.io.Serializable;
import java.util.List;

public class OrderListResponse implements Serializable {
    private List<OrderHistory> value;
    private Paging paging;
    private Object errors;

    public List<OrderHistory> getValue() { return value; }
    public void setValue(List<OrderHistory> value) { this.value = value; }
    public Paging getPaging() { return paging; }
    public void setPaging(Paging paging) { this.paging = paging; }
    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }

    public static class Paging implements Serializable {
        private int currentPage;
        private int totalPages;
        private int pageSize;
        private int totalCount;
        private boolean hasPrevious;
        private boolean hasNext;
        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        public boolean isHasPrevious() { return hasPrevious; }
        public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
    }
} 