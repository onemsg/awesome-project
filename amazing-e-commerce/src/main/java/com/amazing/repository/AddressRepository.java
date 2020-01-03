package com.amazing.repository;

import com.amazing.model.Address;

/**
 * AddressRepository
 */
interface AddressRepository {

    void add(Address address);

    Address getByAccount(String account);

    Address update(Address address);

    void remove(Address address);
}