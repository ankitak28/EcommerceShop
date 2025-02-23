package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<AddressDTO> addressList = addressRepository.findAll().stream().map(
                address -> modelMapper.map(address,AddressDTO.class)
        ).collect(Collectors.toList());
        return addressList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address  address = addressRepository.findById(addressId).orElseThrow(
                ()-> new ResourceNotFoundException("Address","addressId",addressId)
        );
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDb = addressRepository.findById(addressId).orElseThrow(
                ()-> new ResourceNotFoundException("Address","addressId",addressId)
        );
        addressFromDb.setBuildingName(addressDTO.getBuildingName());
        addressFromDb.setState(addressDTO.getState());
        addressFromDb.setCity(addressDTO.getCity());
        addressFromDb.setCountry(addressDTO.getCountry());
        addressFromDb.setPincode(addressDTO.getPincode());
        addressFromDb.setStreet(addressDTO.getStreet());

        Address savedAddress = addressRepository.save(addressFromDb);

        User user = authUtil.loggedInUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(savedAddress.getAddressId()));
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        return modelMapper.map(addressFromDb, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<Address>  addresses = user.getAddresses();
        List<AddressDTO> addressDTOs = addresses.stream().map(
                address -> modelMapper.map(address,AddressDTO.class)
        ).toList();
        return addressDTOs;
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address","addressId",addressId)
        );
        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDatabase);
        return "Address deleted successfully with addressId: " + addressId;
    }

}
