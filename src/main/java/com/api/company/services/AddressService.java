package com.api.company.services;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.AddressModel;
import com.api.company.repositories.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
@Service
public class AddressService {

    final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public AddressModel save(AddressModel addressModel){
        return addressRepository.save(addressModel);
    }

    public List<AddressModel> findAll(){
        return addressRepository.findAll();
    }

    public AddressModel findById(Long id){
        Optional<AddressModel> enderecoModelOptional = addressRepository.findById(id);
        if(enderecoModelOptional.isPresent() == Boolean.FALSE) {
            throw new EntityNotFoundException(ResponseEnum.ADDRESS_NOT_FOUND_BY_ID
                    .getMessage().concat(String.valueOf(id)));
        }
        return enderecoModelOptional.get();
    }
    @Transactional
    public void delete(AddressModel addressModel){
        addressRepository.delete(addressModel);
    }
}
