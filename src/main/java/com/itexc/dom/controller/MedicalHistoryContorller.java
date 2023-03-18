package com.itexc.dom.controller;

import com.itexc.dom.domain.DTO.MedicalHistoryDto;
import com.itexc.dom.domain.MedicalHistory;
import com.itexc.dom.domain.projection.MedicalHistoryView;
import com.itexc.dom.sevice.MedicalHistoryService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/medicalHistory")
@Validated
public class MedicalHistoryContorller {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @PreAuthorize("hasAnyAuthority(" +
            "'medicalHistory.list'," +
            "'medicalHistory.add'," +
            "'medicalHistory.update'," +
            "'medicalHistory.detail'," +
            "'medicalHistory.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<MedicalHistoryView> medicalHistory = medicalHistoryService.findAll();
        return JsonResponse.builder().data(medicalHistory).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('medicalHistory.list_by_patient')")
    @GetMapping(value = "/find_all_by_patient/{patientId}")
    public JsonResponse findAllByPatient(
            @NotNull @PathVariable Long patientId) throws Throwable {
        List<MedicalHistoryView> medicalHistorys = medicalHistoryService.findAllByPatient(patientId);
        return JsonResponse.builder().data(medicalHistorys).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('medicalHistory.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final MedicalHistoryDto medicalHistory) throws Throwable {
        MedicalHistoryView createdMedicalHistory = medicalHistoryService.create(medicalHistory);
        return new JsonResponse(createdMedicalHistory);
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'medicalHistory.detail'," +
            "'medicalHistory.update')")
    @GetMapping(value = "/detail/{medicalHistoryId}")
    public JsonResponse detail(
            @NotNull @PathVariable Long medicalHistoryId) throws Throwable {
        MedicalHistoryView medicalHistory = medicalHistoryService.getDetails(medicalHistoryId);
        return new JsonResponse(medicalHistory);
    }

    @PreAuthorize("hasAuthority('medicalHistory.update')")
    @PutMapping(value = "/update/{medicalHistoryId}")
    public JsonResponse update(
            @NotNull  @PathVariable Long medicalHistoryId,
            @Valid @RequestBody MedicalHistoryDto medicalHistory) throws Throwable {
        MedicalHistory updatedMedicalHistory = medicalHistoryService.update(medicalHistoryId, medicalHistory);
        return new JsonResponse(new MedicalHistoryView(updatedMedicalHistory));
    }

    @PreAuthorize("hasAuthority('medicalHistory.delete')")
    @DeleteMapping(value = "/delete/{medicalHistoryId}")
    public JsonResponse remove(
            @NotNull  @PathVariable Long medicalHistoryId) throws Throwable {
        medicalHistoryService.delete(medicalHistoryId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }
}
