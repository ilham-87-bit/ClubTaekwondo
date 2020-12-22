package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.ContactPerson;
import com.clubtaekwondo.club.repository.ContactPersonRepository;
import com.clubtaekwondo.club.service.ContactPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactPersonImpl implements ContactPersonService {

    @Autowired
    private ContactPersonRepository contactPersonRepository;


    @Override
    public ContactPerson save(ContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public void delete(ContactPerson contactPerson) {
        contactPersonRepository.delete(contactPerson);
    }

    @Override
    public List<ContactPerson> getAllContactPerson() {
        return contactPersonRepository.findAll();
    }

    @Override
    public ContactPerson findById(Long id) {
        return contactPersonRepository.getOne(id);
    }
}
