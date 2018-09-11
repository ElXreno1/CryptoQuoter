package com.finplant.cryptoquoter.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "quotes", schema = "quotes")
public class QuotesEntity {
    private int id;
    private Timestamp time;
    private BigDecimal bid;
    private BigDecimal ask;
    private String exchange;
    private String name;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TIME", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "BID", nullable = false, precision = 6)
    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "ASK", nullable = false, precision = 6)
    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    @Basic
    @Column(name = "EXCHANGE", nullable = false, length = 20)
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuotesEntity that = (QuotesEntity) o;

        if (id != that.id) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (bid != null ? !bid.equals(that.bid) : that.bid != null) return false;
        if (ask != null ? !ask.equals(that.ask) : that.ask != null) return false;
        if (exchange != null ? !exchange.equals(that.exchange) : that.exchange != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (ask != null ? ask.hashCode() : 0);
        result = 31 * result + (exchange != null ? exchange.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return exchange + ": " + name + ", [" + bid + ", " + ask + "] from " + this.time;
    }
}
