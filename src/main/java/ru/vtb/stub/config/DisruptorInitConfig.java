package ru.vtb.stub.config;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import io.micronaut.context.annotation.Bean;
import ru.vtb.stub.config.disruptor.DefaultDisruptorConfig;
import ru.vtb.stub.config.disruptor.EventHandlerChain;
import ru.vtb.stub.config.disruptor.WaitStrategyType;
import ru.vtb.stub.data.EventWrapper;
import ru.vtb.stub.handler.DisruptorHandler;

//@Configuration
public class DisruptorInitConfig {

    private static final String THREAD_NAME = "namo";

    @Bean
    DefaultDisruptorConfig disruptorInit(EventHandlerChain eventHandlerChain) {
        var disruptorConfig = new DefaultDisruptorConfig();
        disruptorConfig.setThreadName(THREAD_NAME);
        disruptorConfig.setEventFactory(new SampleEventFactory());
        disruptorConfig.setWaitStrategyType(WaitStrategyType.SLEEPING_WAIT);
        disruptorConfig.setRingBufferSize(256);
        disruptorConfig.setEventHandlerChain(new EventHandlerChain[]{eventHandlerChain});
        disruptorConfig.init();
        return disruptorConfig;
    }

    @Bean
    EventHandlerChain eventHandlerChain(DisruptorHandler disruptorHandler) {
        return new EventHandlerChain(new EventHandler[]{disruptorHandler});
    }

    private class SampleEventFactory implements EventFactory<EventWrapper> {

        @Override
        public EventWrapper newInstance() {
            return new EventWrapper();
        }

    }

}
