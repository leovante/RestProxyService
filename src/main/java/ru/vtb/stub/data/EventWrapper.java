package ru.vtb.stub.data;

import com.lmax.disruptor.EventFactory;
import ru.vtb.stub.domain.StubData;

/**
 * Wrapper class that allows events of different types to be used on the same disruptor.
 */
public class EventWrapper {
    public final static EventFactory EVENT_FACTORY = EventWrapper::new;

    private int type;
    private StubData event;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StubData getEvent() {
        return event;
    }

    public void setEvent(StubData event) {
        this.event = event;
    }
}
