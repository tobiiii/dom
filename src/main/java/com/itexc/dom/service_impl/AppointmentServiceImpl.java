package com.itexc.dom.service_impl;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.AppointmentView;
import com.itexc.dom.domain.projection.DocDayView;
import com.itexc.dom.domain.projection.DocSessionView;
import com.itexc.dom.repository.AppointmentRepository;
import com.itexc.dom.repository.DocSessionRepository;
import com.itexc.dom.domain.DTO.AppointmentDto;
import com.itexc.dom.domain.DocSession;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.enums.AppointmentStatusE;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.sevice.AppointmentService;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.PatientService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DocSessionRepository docSessionRepository;

    @Override
    public AppointmentView create(AppointmentDto appointment) throws Throwable {
        Appointment newAppointment = new Appointment();
        Doctor doctor = doctorService.findById(appointment.getDoctor());
        Patient patient = patientService.findById(appointment.getPatient());
        DocSession session = docSessionRepository.findById(appointment.getSession()).orElse(null);
        if (isSessionReserved(session,appointment.getDate(),doctor)) throw new ValidationException(ERROR_CODE.INVALID_SESSION);
            newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);
        newAppointment.setSession(session);
        newAppointment.setReason(appointment.getReason());
        newAppointment.setDate(appointment.getDate());
        newAppointment.setStatus(AppointmentStatusE.PROGRAMED.toString());
        return new AppointmentView((Appointment) appointmentRepository.save(newAppointment));
    }

    @Override
    public AppointmentView update(Long appointmentId, AppointmentDto appointment) throws Throwable {
        Appointment appointmentToUpdate = findById(appointmentId);
        appointmentToUpdate.setReason(appointment.getReason());
        appointmentToUpdate.setDate(appointment.getDate());
        return new AppointmentView((Appointment) appointmentRepository.save(appointmentToUpdate));
    }

    @Override
    public List<AppointmentView> findAll() {
        return appointmentRepository.findAllOrderByIdDesc();
    }

    @Override
    public List<AppointmentView> findAllByPatient(Long patientId) throws Throwable {
        Patient patient = patientService.findById(patientId);
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

    @Override
    public List<DocDayView> getDocSchedule(long doc) throws Throwable {
        List<DocDayView> docSchedule = new ArrayList<>();
        Doctor doctor = doctorService.findById(doc);
        Appointment lastAppointment = (Appointment) appointmentRepository.findFirstByOrderByDateDesc().orElse(null);
        if (lastAppointment == null) return docSchedule;
        List<Date> dates = getDaysBetweenDates(new Date(), lastAppointment.getDate());
        docSchedule = dates.stream()
                .map(date -> new DocDayView(date, sessionsListByDayAndDoc(date, doctor)))
                .collect(Collectors.toList());
        return docSchedule;
    }

    private List<DocSessionView> sessionsListByDayAndDoc(Date date, Doctor doctor) {
        List<DocSessionView> result = new ArrayList<>();
        List<DocSession> sessions = docSessionRepository.findAll();
        for (DocSession docSess : sessions) {
            result.add(new DocSessionView(docSess, isSessionReserved(docSess, date, doctor)));
        }

        return result;
    }

    public boolean isSessionReserved(DocSession session, Date date, Doctor doctor) {
        Appointment appointment = (Appointment) appointmentRepository.findBySessionAndDateAndDoctor(session, date, doctor)
                .orElse(null);
        return appointment != null;
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(enddate);
        return dates;
    }


}
