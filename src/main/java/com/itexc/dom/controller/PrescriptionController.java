package com.itexc.dom.controller;

import com.itexc.dom.domain.Prescription;
import com.itexc.dom.domain.DTO.PrescriptionDto;
import com.itexc.dom.domain.projection.PrescriptionView;
import com.itexc.dom.sevice.PrescriptionService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/prescription")
@Validated
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PreAuthorize("hasAnyAuthority(" +
            "'prescription.list'," +
            "'prescription.add'," +
            "'prescription.update'," +
            "'prescription.detail'," +
            "'prescription.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<PrescriptionView> prescription = prescriptionService.findAll();
        return JsonResponse.builder().data(prescription).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('prescription.list_by_patient')")
    @GetMapping(value = "/find_all_by_patient/{patientId}")
    public JsonResponse findAllByPatient(
            @NotNull @PathVariable Long patientId) throws Throwable {
        List<PrescriptionView> prescriptions = prescriptionService.findAllByPatient(patientId);
        return JsonResponse.builder().data(prescriptions).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('prescription.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final PrescriptionDto prescription) throws Throwable {
        PrescriptionView createdPrescription = prescriptionService.create(prescription);
        return new JsonResponse(createdPrescription);
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'prescription.detail'," +
            "'prescription.update')")
    @GetMapping(value = "/detail/{prescriptionId}")
    public JsonResponse detail(
            @NotNull @PathVariable Long prescriptionId) throws Throwable {
        PrescriptionView prescription = prescriptionService.getDetails(prescriptionId);
        return new JsonResponse(prescription);
    }

    @PreAuthorize("hasAuthority('prescription.update')")
    @PutMapping(value = "/update/{prescriptionId}")
    public JsonResponse update(
            @NotNull  @PathVariable Long prescriptionId,
            @Valid @RequestBody PrescriptionDto prescription) throws Throwable {
        Prescription updatedPrescription = prescriptionService.update(prescriptionId, prescription);
        return new JsonResponse(new PrescriptionView(updatedPrescription));
    }


    @PreAuthorize("hasAuthority('prescription.delete')")
    @DeleteMapping(value = "/cancel/{prescriptionId}")
    public JsonResponse remove(
            @NotNull  @PathVariable Long prescriptionId) throws Throwable {
        prescriptionService.delete(prescriptionId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }


}
