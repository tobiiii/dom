package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.PatientDto;
import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.enums.ProfileCodeE;
import com.itexc.dom.domain.projection.PatientView;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.PatientRepository;
import com.itexc.dom.sevice.PatientService;
import com.itexc.dom.sevice.ProfileService;
import com.itexc.dom.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    PatientRepository patientRepository;
    
    @Override
    public PatientView create(PatientDto patient) throws Throwable {
        Profile patientProfile = profileService.findByCode(ProfileCodeE.PATIENT.toString(), ERROR_CODE.INEXISTANT_PATIENT_PROFILE);
        UserDto userDto = new UserDto(patient.getFirstName(), patient.getLastName(), patient.getEmailAddress(), patientProfile.getId());
        UserView userView = userService.create(userDto);
        User newUser = userService.findUserById(userView.getId());
        Patient newPatient = new Patient();
        newPatient.setAddress(patient.getAddress());
        newPatient.setDateOfBirth(patient.getDateOfBirth());
        newPatient.setUser(newUser);
        PatientView result = new PatientView((Patient) patientRepository.save(newPatient));
        result.setUser(userView);
        return result;
    }

    @Override
    public Patient update(Long patientId, PatientDto patient) throws Throwable {
        Patient patientToUpdate = findById(patientId);
        patientToUpdate.setAddress(patient.getAddress());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
        UserDto userDto = new UserDto(patient.getFirstName(), patient.getLastName(), patient.getEmailAddress(), patientToUpdate.getUser().getProfile().getId());
        patientRepository.save(patientToUpdate);
        userService.update(patientToUpdate.getUser().getId(),userDto);
        return findById(patientToUpdate.getId());

    }

    @Override
    public List<PatientView> findAll() {
        return  patientRepository.findAllOrderByIdDesc();
    }

    @Override
    public PatientView getDetails(Long id) throws Throwable {return new PatientView(findById(id));}

    @Override
    public void delete(Long patientId) throws Throwable {
        Patient patient = findById(patientId);
        patientRepository.delete(patient);

    }

    @Override
    public Patient findById(Long id) throws Throwable {
        return (Patient) patientRepository.findById(id)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_DOCTOR));

    }
}
