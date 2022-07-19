package com.api.company.controllers;

import com.api.company.dtos.addressDTOs.AddressInsertDTO;
import com.api.company.dtos.addressDTOs.AddressUpdateDTO;
import com.api.company.dtos.addressDTOs.AddressResponseDTO;
import com.api.company.models.AddressModel;
import com.api.company.services.AddressService;
import com.api.company.utils.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final ObjectMapperUtils objectMapperUtils;

    public AddressController(AddressService addressService, ObjectMapperUtils objectMapperUtils) {
        this.addressService = addressService;
        this.objectMapperUtils = objectMapperUtils;
    }

    @PostMapping("/insert")
    public ResponseEntity<AddressResponseDTO> save(@RequestBody @Valid AddressInsertDTO addressInsertDto){
        AddressModel addressModel = objectMapperUtils.map(addressInsertDto, AddressModel.class);
        AddressModel addressModelSaved = addressService.save(addressModel);
        AddressResponseDTO addressResponseDTO = objectMapperUtils.map(addressModelSaved, AddressResponseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AddressResponseDTO>> getAll(){
        List<AddressResponseDTO> addressResponseDTOS = objectMapperUtils
                .mapAll(addressService.findAll(),AddressResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponseDTOS);
    }

    @GetMapping("/getOneById/{id}")
    public ResponseEntity<AddressResponseDTO> getOneById(@PathVariable(value = "id") Long id){
        AddressModel addressModel = addressService.findById(id);
        AddressResponseDTO addressResponseDTO = objectMapperUtils.map(addressModel, AddressResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "id") Long id){
        AddressModel addressModel = addressService.findById(id);
        addressService.delete(addressModel);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update")
    public ResponseEntity<AddressResponseDTO> update(@RequestBody @Valid AddressUpdateDTO addressUpdateDto){
        addressService.findById(addressUpdateDto.getId());
        AddressModel addressModel = objectMapperUtils.map(addressUpdateDto, AddressModel.class);
        AddressModel addressModelSaved = addressService.save(addressModel);
        AddressResponseDTO addressResponseDTO = objectMapperUtils.map(addressModelSaved, AddressResponseDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponseDTO);
    }
}
