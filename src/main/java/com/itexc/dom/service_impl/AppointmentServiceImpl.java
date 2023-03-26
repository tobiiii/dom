package com.itexc.dom.service_impl;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.AppointmentView;
import com.itexc.dom.domain.projection.DoctorDay;
import com.itexc.dom.domain.projection.DoctorSessionView;
import com.itexc.dom.repository.AppointmentRepository;
import com.itexc.dom.repository.DocSessionRepository;
import com.itexc.dom.domain.DTO.AppointmentDto;
import com.itexc.dom.domain.DoctorSession;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.enums.AppointmentStatusE;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.sevice.AppointmentService;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentRepository<Appointment> appointmentRepository;

    @Autowired
    DocSessionRepository docSessionRepository;

    @Override
    public AppointmentView create(AppointmentDto appointment) throws Throwable {
        Appointment newAppointment = new Appointment();
        Doctor doctor = doctorService.findById(appointment.getDoctor());
        Patient patient = patientService.findById(appointment.getPatient());
        DoctorSession session = docSessionRepository.findById(appointment.getSession()).orElse(null);
        if (isSessionReserved(session,appointment.getDate(),doctor)) throw new ValidationException(ERROR_CODE.INVALID_SESSION);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);
        newAppointment.setSession(session);
        newAppointment.setReason(appointment.getReason());
        newAppointment.setDate(appointment.getDate());
        newAppointment.setStatus(AppointmentStatusE.PROGRAMED.toString());
        return new AppointmentView( appointmentRepository.save(newAppointment));
    }

    @Override
    public AppointmentView update(Long appointmentId, AppointmentDto appointment) throws Throwable {
        Appointment appointmentToUpdate = findById(appointmentId);
        appointmentToUpdate.setReason(appointment.getReason());
        appointmentToUpdate.setDate(appointment.getDate());
        return new AppointmentView(appointmentRepository.save(appointmentToUpdate));
    }

    @Override
    public List<AppointmentView> findAll() {
        return appointmentRepository.findAllOrderByIdDesc().stream().map(AppointmentView::new).toList();

    }

    @Override
    public List<AppointmentView> findAllByPatient(Long patientId) throws Throwable {
        Patient patient = patientService.findById(patientId);
        return appointmentRepository.findAllByPatient(patient).stream().map(AppointmentView::new).toList();
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
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_APPOINTMENT));

    }

    @Override
    public List<DoctorDay> getDocSchedule(long doctorId) throws Throwable {
        List<DoctorDay> docSchedule = new ArrayList<>();
        Doctor doctor = doctorService.findById(doctorId);
        Appointment lastAppointment = appointmentRepository.findFirstByOrderByDateDesc().orElse(null);
        if (lastAppointment == null) return docSchedule;
        List<Date> days = getDaysBetweenDates(new Date(), lastAppointment.getDate());
        return days.stream()
                .map(day -> new DoctorDay(day, sessionsListByDayAndDoc(day, doctor)))
                .toList();
    }

    private List<DoctorSessionView> sessionsListByDayAndDoc(Date date, Doctor doctor) {
        List<DoctorSession> sessions = docSessionRepository.findAll();
        return sessions.stream()
                .map(session -> new DoctorSessionView(session, isSessionReserved(session, date, doctor)))
                .toList();
    }

    public boolean isSessionReserved(DoctorSession session, Date date, Doctor doctor) {
        Appointment appointment = appointmentRepository.findBySessionAndDateAndDoctor(session, date, doctor)
                .orElse(null);
        return appointment != null;
    }

    public static List<Date> getDaysBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(endDate);
        return dates;
    }


}
