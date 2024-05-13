package ru.vtb.stub.velocity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.velocity.VelocityContext;

public interface ContextInitializer {

    /**
     * Initialize {@link VelocityContext} using params from input request
     *
     * @param request Request
     * @return Initialized {@link VelocityContext} using params from input request
     */
    VelocityContext init(String request) throws JsonProcessingException;
}
