package ru.random.walk.chat_service.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;

import java.security.Principal;
import java.util.Set;

public class StubDataUtil {
    public static final class SimpUserStub implements SimpUser {
        @Override
        public @NotNull String getName() {
            return "";
        }

        @Override
        public Principal getPrincipal() {
            return null;
        }

        @Override
        public boolean hasSessions() {
            return false;
        }

        @Override
        public SimpSession getSession(@NotNull String sessionId) {
            return null;
        }

        @Override
        public @NotNull Set<SimpSession> getSessions() {
            return Set.of();
        }
    }
}
