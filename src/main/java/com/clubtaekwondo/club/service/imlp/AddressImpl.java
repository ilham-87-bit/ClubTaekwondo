package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Address;
import com.clubtaekwondo.club.repository.AddressRepository;
import com.clubtaekwondo.club.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.getOne(id);
    }

    @Override
    public void delete(Address address) {
        addressRepository.delete(address);
    }
}
