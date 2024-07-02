package ru.vtb.stub.handler;

import com.lmax.disruptor.EventHandler;
import io.micronaut.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.data.EventWrapper;
import ru.vtb.stub.service.RequestService;

@Bean
@RequiredArgsConstructor
public class DisruptorHandler implements EventHandler<EventWrapper> {

    private final RequestService service;

    @Override
    public void onEvent(EventWrapper event, long sequence, boolean endOfBatch) {
        if (event.getEvent().getPath() == null) {
            return;
        }
        service.putData(event.getEvent());
    }

}
