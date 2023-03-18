package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.DoctorDto;
import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.enums.ProfileCodeE;
import com.itexc.dom.domain.projection.DoctorView;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.DoctorRepository;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.ProfileService;
import com.itexc.dom.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public DoctorView create(DoctorDto doctor) throws Throwable {
        Profile docProfile = profileService.findByCode(ProfileCodeE.DOCTOR.toString(), ERROR_CODE.INEXISTANT_DOC_PROFILE);
        UserDto userDto = new UserDto(doctor.getFirstName(), doctor.getLastName(), doctor.getEmailAddress(), docProfile.getId());
        UserView userView = userService.create(userDto);
        User newUser = userService.findUserById(userView.getId());
        Doctor newDoc = new Doctor();
        newDoc.setSpecialization(doctor.getSpecialization());
        newDoc.setUser(newUser);
        DoctorView result = new DoctorView((Doctor) doctorRepository.save(newDoc));
        result.setUser(userView);
        return result;
    }

    @Override
    public Doctor update(Long doctorId, DoctorDto doctor) throws Throwable {
        Doctor doc = findById(doctorId);
        doc.setSpecialization(doctor.getSpecialization());
        UserDto userDto = new UserDto(doctor.getFirstName(), doctor.getLastName(), doctor.getEmailAddress(), doc.getUser().getProfile().getId());
        doctorRepository.save(doc);
        userService.update(doc.getUser().getId(),userDto);
        return findById(doc.getId());
    }

    @Override
    public List<DoctorView> findAll() {
        return doctorRepository.findAllOrderByIdDesc();
    }

    @Override
    public DoctorView getDetails(Long id) throws Throwable {
        return new DoctorView(findById(id));
    }

    @Override
    public void delete(Long doctorId) throws Throwable {
        Doctor doc = findById(doctorId);
        doctorRepository.delete(doc);
    }

    @Override
    public Doctor findById(Long id) throws Throwable {
        return (Doctor) doctorRepository.findById(id)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_DOCTOR));

    }
}
