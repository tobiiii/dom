package com.itexc.dom.service_impl;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.DTO.AppointmentDto;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.enums.AppointmentStatusE;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.AppointmentView;
import com.itexc.dom.domain.projection.PrivilegeView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.AppointmentRepository;
import com.itexc.dom.sevice.AppointmentService;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public AppointmentView create(AppointmentDto appointment) throws Throwable {
        Appointment newAppointment = new Appointment();
        Doctor doctor = doctorService.findById(appointment.getDoctor());
        Patient patient = patientService.findById(appointment.getPatient());
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);
        newAppointment.setReason(appointment.getReason());
        newAppointment.setDateAndTime(appointment.getDateAndTime());
        newAppointment.setStatus(AppointmentStatusE.PROGRAMED.toString());
        return new AppointmentView((Appointment) appointmentRepository.save(newAppointment));
    }

    @Override
    public Appointment update(Long appointmentId, AppointmentDto appointment) throws Throwable {
        Appointment appointmentToUpdate = findById(appointmentId);
        appointmentToUpdate.setReason(appointment.getReason());
        appointmentToUpdate.setDateAndTime(appointment.getDateAndTime());
        return (Appointment) appointmentRepository.save(appointmentToUpdate);
    }

    @Override
    public List<AppointmentView> findAll() {
        return appointmentRepository.findAllOrderByIdDesc();
    }

    @Override
    public List<AppointmentView> findAllByPatient(Long patientId) throws Throwable {
        Patient patient =  patientService.findById(patientId);
        List<Appointment> app = appointmentRepository.findAllByPatient(patient);
        return app.stream().map(AppointmentView::new).collect(Collectors.toList());
    }

    @Override
    public AppointmentView getDetails(Long id) throws Throwable {
        return new AppointmentView(findById(id));
    }

    @Override
    public void cancel(Long appointmentId) throws Throwable {
        Appointment appointment = findById(appointmentId);
        appointment.setStatus(AppointmentStatusE.CANCELED.toString());
        appointmentRepository.save(appointment);

    }

    @Override
    public void end(Long appointmentId) throws Throwable {
        Appointment appointment = findById(appointmentId);
        appointment.setStatus(AppointmentStatusE.ACCOMPLISHED.toString());
        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findById(Long appointmentId) throws Throwable {
        return (Appointment) appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_APPOINTMENT));

    }
}
