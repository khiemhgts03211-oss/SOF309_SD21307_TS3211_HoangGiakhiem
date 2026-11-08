package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TicketDetail {

    private int detailId;
    private int ticketId;
    private int serviceId;
    private int quantity;
    private BigDecimal amount;
    private Timestamp createdAt;

    private Ticket ticket;
    private Service service;

    public TicketDetail() {
    }

    public TicketDetail(int detailId, int ticketId, int serviceId,
                        int quantity, BigDecimal amount, Timestamp createdAt) {
        this.detailId = detailId;
        this.ticketId = ticketId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getDetailId() {
        return detailId;
    }
    
    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Service getService() {
        return service;
    }
    
    public void setService(Service service) {
        this.service = service;
    }
}