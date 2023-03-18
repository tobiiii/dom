package com.itexc.dom.sevice;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.DTO.AppointmentDto;
import com.itexc.dom.domain.projection.AppointmentView;

import java.util.List;

public interface AppointmentService {

    AppointmentView create(AppointmentDto appointment) throws Throwable;

    AppointmentView update(Long appointmentId, AppointmentDto appointment) throws Throwable;

    List<AppointmentView> findAll();

    List<AppointmentView> findAllByPatient(Long patientId) throws Throwable;

    AppointmentView getDetails(Long id) throws Throwable;

    void cancel(Long appointmentId) throws Throwable;

    void end(Long appointmentId) throws Throwable;

    Appointment findById(Long appointmentId) throws Throwable;

}
