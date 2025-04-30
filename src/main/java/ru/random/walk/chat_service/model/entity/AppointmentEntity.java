package ru.random.walk.chat_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "appointments", schema = "chat")
public class AppointmentEntity {
    @Id
    @Column(name = "appointment_id", nullable = false)
    private UUID appointmentId;

    @Id
    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppointmentId implements Serializable {
        @Column(name = "appointment_id", nullable = false)
        private UUID appointmentId;

        @Column(name = "message_id", nullable = false)
        private UUID messageId;
    }
}
