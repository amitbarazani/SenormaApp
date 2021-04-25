package com.acebrico.royalcarribeanapp;

public class Reservation {
    public String Agent;
    public String AgentEmail;
    public String ArriveAt;
    public String ArriveTo;
    public String ClientEmail;
    public String departsAt;
    public String departsFrom;
    public String fullName;
    public String IDAgent;
    public String idClient;
    public String numberNights;
    public String Price;
    public String ReservationNumber;
    public String roomCategory;
    public String ship;
    public String Status;
    public String stopPlace;
    public String stopTime;

    public Reservation(String agent, String agentEmail, String arriveAt, String arriveTo, String clientEmail, String departsAt, String departsFrom, String fullName, String IDAgent, String idClient, String numberNights, String price, String reservationNumber, String roomCategory, String ship, String status, String stopPlace, String stopTime) {
        Agent = agent;
        AgentEmail = agentEmail;
        ArriveAt = arriveAt;
        ArriveTo = arriveTo;
        ClientEmail = clientEmail;
        this.departsAt = departsAt;
        this.departsFrom = departsFrom;
        this.fullName = fullName;
        this.IDAgent = IDAgent;
        this.idClient = idClient;
        this.numberNights = numberNights;
        Price = price;
        ReservationNumber = reservationNumber;
        this.roomCategory = roomCategory;
        this.ship = ship;
        Status = status;
        this.stopPlace = stopPlace;
        this.stopTime = stopTime;
    }
    public Reservation(){}

}
