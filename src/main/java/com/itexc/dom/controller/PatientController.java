package com.itexc.dom.controller;

import com.itexc.dom.domain.DTO.PatientDto;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.PatientView;
import com.itexc.dom.sevice.PatientService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@Validated
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PreAuthorize("hasAnyAuthority(" +
            "'patient.list'," +
            "'patient.add'," +
            "'patient.update'," +
            "'patient.detail'," +
            "'patient.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<PatientView> patient = patientService.findAll();
        return JsonResponse.builder().data(patient).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('patient.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final PatientDto patient) throws Throwable {
        PatientView createdPatient = patientService.create(patient);
        return new JsonResponse(createdPatient);
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'patient.detail'," +
            "'patient.update')")
    @GetMapping(value = "/detail/{patientId}")
    public JsonResponse detail(
            @NotNull  @PathVariable Long patientId) throws Throwable {
        PatientView patient = patientService.getDetails(patientId);
        return new JsonResponse(patient);
    }

    @PreAuthorize("hasAuthority('patient.update')")
    @PutMapping(value = "/update/{patientId}")
    public JsonResponse update(
            @NotNull @PathVariable Long patientId,
            @Valid @RequestBody PatientDto patient) throws Throwable {
        Patient updatedPatient = patientService.update(patientId, patient);
        return new JsonResponse(new PatientView(updatedPatient));
    }


    @PreAuthorize("hasAuthority('patient.delete')")
    @DeleteMapping(value = "/delete/{patientId}")
    public JsonResponse remove(
            @NotNull @PathVariable Long patientId) throws Throwable {
        patientService.delete(patientId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

}