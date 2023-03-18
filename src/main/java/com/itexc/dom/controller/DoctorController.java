package com.itexc.dom.controller;

import com.itexc.dom.domain.DTO.DoctorDto;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.projection.DoctorView;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@Validated
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasAnyAuthority(" +
            "'doctor.list'," +
            "'doctor.add'," +
            "'doctor.update'," +
            "'doctor.detail'," +
            "'doctor.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<DoctorView> doctors = doctorService.findAll();
        return JsonResponse.builder().data(doctors).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('doctor.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final DoctorDto doctor) throws Throwable {
        DoctorView createdDoctor = doctorService.create(doctor);
        return new JsonResponse(createdDoctor);
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'doctor.detail'," +
            "'doctor.update')")
    @GetMapping(value = "/detail/{doctorId}")
    public JsonResponse detail(
            @NotNull  @PathVariable Long doctorId) throws Throwable {
        DoctorView doctor = doctorService.getDetails(doctorId);
        return new JsonResponse(doctor);
    }

    @PreAuthorize("hasAuthority('doctor.update')")
    @PutMapping(value = "/update/{doctorId}")
    public JsonResponse update(
            @NotNull  @PathVariable Long doctorId,
            @Valid @RequestBody DoctorDto doctor) throws Throwable {
        Doctor updatedDctor = doctorService.update(doctorId, doctor);
        return new JsonResponse(new DoctorView(updatedDctor));
    }


    @PreAuthorize("hasAuthority('doctor.delete')")
    @DeleteMapping(value = "/delete/{doctorId}")
    public JsonResponse remove(
            @NotNull  @PathVariable Long doctorId) throws Throwable {
        doctorService.delete(doctorId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

}
