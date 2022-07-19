package com.api.company.unitTests;

import com.api.company.enums.ResponseEnum;
import com.api.company.models.AddressModel;
import com.api.company.repositories.AddressRepository;
import com.api.company.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressService addressService;

    private AddressModel dummyAddress;

    @BeforeEach
    void initAddress() {
        dummyAddress = new AddressModel();
        dummyAddress.setCountry("Brazil");
        dummyAddress.setFu("MG");
        dummyAddress.setCity("Belo Horizonte");
        dummyAddress.setStreet("Rua João Pinheiro");
        dummyAddress.setNumber(345L);
        dummyAddress.setPostalCode("876335570");
    }

    @Test
    void insertedAddressHasMandatoryFields(){
        AddressModel addressModel = dummyAddress;
        when(addressRepository.save(any(AddressModel.class))).then(returnsFirstArg());

        AddressModel savedAddress = addressService.save(addressModel);

        assertEquals(savedAddress.getCountry(),addressModel.getCountry());
        assertEquals(savedAddress.getFu(),addressModel.getFu());
        assertEquals(savedAddress.getCity(),addressModel.getCity());
        assertEquals(savedAddress.getStreet(),addressModel.getStreet());
        assertEquals(savedAddress.getNumber(),addressModel.getNumber());
        assertEquals(savedAddress.getPostalCode(),addressModel.getPostalCode());
        verify(addressRepository,times(1)).save(addressModel);
    }

    @Test
    void updateAddress(){
        AddressModel addressDBModel = dummyAddress;
        addressDBModel.setId(1L);
        AddressModel addressToUpdateModel = new AddressModel();
        addressToUpdateModel.setId(1L);
        addressToUpdateModel.setCountry("Argentina");
        addressToUpdateModel.setNumber(345L);
        addressToUpdateModel.setPostalCode("64453324");

        when(addressRepository.save(addressToUpdateModel)).thenReturn(addressToUpdateModel);

        AddressModel addressModelSaved = addressService.save(addressToUpdateModel);
        assertNotNull(addressModelSaved);
        assertEquals(addressToUpdateModel.getId(),addressModelSaved.getId());
        assertEquals(addressToUpdateModel.getCountry(),addressModelSaved.getCountry());
        assertEquals(addressToUpdateModel.getNumber(),addressModelSaved.getNumber());
        assertEquals(addressToUpdateModel.getPostalCode(),addressModelSaved.getPostalCode());
        verify(addressRepository,times(1)).save(addressToUpdateModel);
    }

    @Test
    void findAddressByIdSuccess(){
        AddressModel addressModel = dummyAddress;
        addressModel.setId(1L);
        when(addressRepository.findById(addressModel.getId())).thenReturn(Optional.of(addressModel));

        AddressModel returnedAddressModel = addressService.findById(addressModel.getId());

        assertNotNull(returnedAddressModel);
        assertEquals(addressModel.getId(),returnedAddressModel.getId());
        verify(addressRepository,times(1)).findById(addressModel.getId());
    }

    @Test
    void findAddressByIdThrowEntityNotFoundException(){
        AddressModel addressModel = dummyAddress;
        addressModel.setId(1L);
        when(addressRepository.findById(addressModel.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                EntityNotFoundException.class, () -> addressService.findById(addressModel.getId())
        );

        assertEquals(ResponseEnum.ADDRESS_NOT_FOUND_BY_ID.getMessage()
                .concat(addressModel.getId().toString()),exception.getMessage());
        verify(addressRepository,times(1)).findById(addressModel.getId());
    }

    @Test
    void deleteAddressSuccess(){
        AddressModel addressModel = dummyAddress;
        addressRepository.delete(addressModel);
        verify(addressRepository, times(1)).delete(addressModel);
    }
}
