package template_spring_boot.template.brasilapi.dto;

import template_spring_boot.template.brasilapi.entity.BrasilApiAddress;

public class BrasilApiAddressResponse {
    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String service;

    public BrasilApiAddressResponse() {
    }

    public BrasilApiAddressResponse(String cep, String state, String city, String neighborhood, String street, String service) {
        this.cep = cep;
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.service = service;
    }

    public static BrasilApiAddressResponse fromEntity(BrasilApiAddress e) {
        return new BrasilApiAddressResponse(e.getCep(), e.getState(), e.getCity(), e.getNeighborhood(), e.getStreet(), e.getService());
    }

    public String getCep() {
        return cep;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public String getService() {
        return service;
    }
}

