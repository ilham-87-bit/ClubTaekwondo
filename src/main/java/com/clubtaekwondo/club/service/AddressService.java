package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Address;

import java.util.List;

public interface AddressService {

    Address save(Address address);

    List<Address> getAllAddress();

    Address findById(Long id);

    void delete(Address address);
}
