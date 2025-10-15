package template_spring_boot.template.adapters.controller.dto;

import template_spring_boot.template.domain.Address;

public class AddressResponse {
    private String cep;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String service;

    public AddressResponse() {
    }

    public AddressResponse(String cep, String state, String city, String neighborhood, String street, String service) {
        this.cep = cep;
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.service = service;
    }

    public static AddressResponse fromEntity(final Address e) {
        return new AddressResponse(e.getCep(), e.getState(), e.getCity(), e.getNeighborhood(), e.getStreet(), e.getService());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressResponse that = (AddressResponse) o;
        return java.util.Objects.equals(cep, that.cep) &&
                java.util.Objects.equals(state, that.state) &&
                java.util.Objects.equals(city, that.city) &&
                java.util.Objects.equals(neighborhood, that.neighborhood) &&
                java.util.Objects.equals(street, that.street) &&
                java.util.Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(cep, state, city, neighborhood, street, service);
    }
}
