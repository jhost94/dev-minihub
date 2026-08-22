package center.jhub.dev.service;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import center.jhub.data.dto.out.dev.DevRestOutDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class SSEService {

    @Value("${application.sse.max-duration:PT5M}")
    private Duration maxDuration;
    
    @Value("${application.sse.min-frequency:PT0.1S}")
    private Duration minFrequency;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final TemplateService templateService;
    private final ObjectMapper objectMapper;


    public SseEmitter createStream(DevRestInDTO dto, SSEOptions options) {
        SseEmitter emitter = new SseEmitter(options.getDuration().toMillis());
        options = satinizeOptions(options);

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                String message = objectMapper.writeValueAsString(createObject(dto));
                emitter.send(SseEmitter.event()
                                 .name("message")
                                 .data(message));
                log.info("Sending message: {}", message);
            } catch (AsyncRequestNotUsableException e) {
                log.info("Client closed pipe");
                emitter.complete();
            } catch (IOException e) {
                log.warn("Exception: ", e);
                emitter.completeWithError(e);
            }

        }, options.getDelay().toMillis(), options.getFrequency().toMillis(), TimeUnit.MILLISECONDS);

        emitter.onCompletion(() -> {
            task.cancel(true);
            log.info("Emitter Completion");
        });
        emitter.onTimeout(() -> {
            log.info("Emitter timeout");
            task.cancel(true);
            emitter.complete();
        });
        emitter.onError(error -> {
            task.cancel(true);
            log.info("Emitter ERROR");
        });

        return emitter;
    }

    private SSEOptions satinizeOptions(SSEOptions options) {
        if (Objects.isNull(options)) {
            return SSEOptions.getDefaults();
        }
        if (Objects.nonNull(options.getDuration())
                && options.getDuration().compareTo(maxDuration) > 0) {
            options.duration(maxDuration);
        }
        if (Objects.nonNull(options.getFrequency())
                && options.getFrequency().compareTo(minFrequency) < 0) {
            options.frequency(minFrequency);
        }
        return options;
    }

    private DevRestOutDTO createObject(DevRestInDTO dto) {
        DevRestOutDTO out = new DevRestOutDTO();
        dto.forEach((k, v) -> out.put(k, templateService.getExampleForType(v, k)));
        return out;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SSEOptions {
        private Duration duration;
        private Duration delay;
        private Duration frequency;
        private String eventName;
        private Boolean sendComment;
        private List<String> commentValues;
        private Duration commentFrequency;

        private static SSEOptions getDefaults() {
            return new SSEOptions(
                Duration.ofMinutes(5),
                Duration.ZERO,
                Duration.ofSeconds(3),
                "message",
                Boolean.TRUE,
                List.of("keep-alive"),
                Duration.ofSeconds(5)
            );
        }

        public SSEOptions duration(Duration duration) {
            this.duration = duration;
            return this;
        }
        public SSEOptions delay(Duration delay) {
            this.delay = delay;
            return this;
        }
        public SSEOptions frequency(Duration frequency) {
            this.frequency = frequency;
            return this;
        }
        public SSEOptions duration(String eventName) {
            this.eventName = eventName;
            return this;
        }
        public SSEOptions duration(Boolean sendComment) {
            this.sendComment = sendComment;
            return this;
        }
        public SSEOptions commentValues(List<String> commentValues) {
            this.commentValues = commentValues;
            return this;
        }
        public SSEOptions commentFrequency(Duration commentFrequency) {
            this.commentFrequency = commentFrequency;
            return this;
        }

        public static SSEOptions create() {
            return getDefaults();
        }

        public static SSEOptions create(Duration duration, Duration delay, Duration frequency, String eventName, Boolean sendComment, List<String> commentValues, Duration commentFrequency) {
            SSEOptions options = create();

            if (Objects.nonNull(duration)) options.duration = duration;
            if (Objects.nonNull(delay)) options.delay = delay;
            if (Objects.nonNull(frequency)) options.frequency = frequency;
            if (Objects.nonNull(eventName)) options.eventName = eventName;
            if (Objects.nonNull(sendComment)) options.sendComment = sendComment;
            if (Objects.nonNull(commentValues)) options.commentValues = commentValues;
            if (Objects.nonNull(commentFrequency)) options.commentFrequency = commentFrequency;

            return options;
        }

        public static SSEOptions create(
            Long duration, TimeUnit durationUnit,
            Long delay, TimeUnit delayUnit,
            Long frequency, TimeUnit frequencyUnit,
            String eventName,
            Boolean sendComment,
            List<String> commentValues,
            Long commentFrequency, TimeUnit commentFrequencyUnit) {

            SSEOptions options = create();

            if (Objects.nonNull(duration) && Objects.nonNull(durationUnit)) options.duration = Duration.of(duration, durationUnit.toChronoUnit());
            if (Objects.nonNull(delay) && Objects.nonNull(delayUnit)) options.delay = Duration.of(delay, delayUnit.toChronoUnit());
            if (Objects.nonNull(frequency) && Objects.nonNull(frequencyUnit)) options.frequency = Duration.of(frequency, frequencyUnit.toChronoUnit());
            if (Objects.nonNull(eventName)) options.eventName = eventName;
            if (Objects.nonNull(sendComment)) options.sendComment = sendComment;
            if (Objects.nonNull(commentValues)) options.commentValues = commentValues;
            if (Objects.nonNull(commentFrequency) && Objects.nonNull(commentFrequencyUnit)) options.commentFrequency = Duration.of(commentFrequency, commentFrequencyUnit.toChronoUnit());
            return options;
        }
    }
}
