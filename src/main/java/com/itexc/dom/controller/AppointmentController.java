package com.itexc.dom.controller;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.DTO.AppointmentDto;
import com.itexc.dom.domain.projection.AppointmentView;
import com.itexc.dom.sevice.AppointmentService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@Validated
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PreAuthorize("hasAnyAuthority(" +
            "'appointment.list'," +
            "'appointment.add'," +
            "'appointment.update'," +
            "'appointment.detail'," +
            "'appointment.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<AppointmentView> appointment = appointmentService.findAll();
        return JsonResponse.builder().data(appointment).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('appointment.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final AppointmentDto appointment) throws Throwable {
        AppointmentView createdAppointment = appointmentService.create(appointment);
        return new JsonResponse(createdAppointment);
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'appointment.detail'," +
            "'appointment.update')")
    @GetMapping(value = "/detail/{appointmentId}")
    public JsonResponse detail(
            @NotNull @PathVariable Long appointmentId) throws Throwable {
        AppointmentView appointment = appointmentService.getDetails(appointmentId);
        return new JsonResponse(appointment);
    }

    @PreAuthorize("hasAuthority('appointment.update')")
    @PutMapping(value = "/update/{appointmentId}")
    public JsonResponse update(
            @NotNull  @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentDto appointment) throws Throwable {
        Appointment updatedAppointment = appointmentService.update(appointmentId, appointment);
        return new JsonResponse(new AppointmentView(updatedAppointment));
    }


    @PreAuthorize("hasAuthority('appointment.cancel')")
    @DeleteMapping(value = "/cancel/{appointmentId}")
    public JsonResponse cancel(
            @NotNull  @PathVariable Long appointmentId) throws Throwable {
        appointmentService.cancel(appointmentId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('appointment.end')")
    @DeleteMapping(value = "/end/{appointmentId}")
    public JsonResponse end(
            @NotNull  @PathVariable Long appointmentId) throws Throwable {
        appointmentService.end(appointmentId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

}
