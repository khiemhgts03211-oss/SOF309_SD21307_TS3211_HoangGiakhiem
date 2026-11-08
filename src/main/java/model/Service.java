package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Service {

    private int serviceId;
    private int categoryId;
    private String serviceName;
    private BigDecimal price;
    private String status;
    private Timestamp createdAt;

    private ServiceCategory category;
    private String categoryName;
    
    public Service() {
    }

    public Service(int serviceId, int categoryId, String serviceName,
                   BigDecimal price, String status, Timestamp createdAt) {
        this.serviceId = serviceId;
        this.categoryId = categoryId;
        this.serviceName = serviceName;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public ServiceCategory getCategory() {
        return category;
    }
    
    public void setCategory(ServiceCategory category) {
        this.category = category;
    }
    
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
}