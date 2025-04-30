package ru.random.walk.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.random.walk.chat_service.model.entity.AppointmentEntity;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, AppointmentEntity.AppointmentId> {
    Optional<AppointmentEntity> findByAppointmentId(UUID appointmentId);
}
