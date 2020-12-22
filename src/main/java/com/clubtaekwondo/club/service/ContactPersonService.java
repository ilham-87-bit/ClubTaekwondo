package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.ContactPerson;

import java.util.List;

public interface ContactPersonService {

    ContactPerson save(ContactPerson contactPerson);

    void delete(ContactPerson contactPerson);

    List<ContactPerson> getAllContactPerson();

    ContactPerson findById(Long id);
}
