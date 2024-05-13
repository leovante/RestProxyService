package ru.vtb.stub.velocity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;


public interface TemplateProcessor {

    /**
     * Process velocity {@link Template} by Velocity {@link Context}
     *
     * @param template         Velocity {@link Template}
     * @param contextProcessor {@link ContextProcessor}
     * @return {@link String} From processed {@link Template}
     */
    String process(Template template, ContextProcessor contextProcessor);

    /**
     * Context initializer
     */
    interface ContextProcessor {

        /**
         * Init context to merge template
         *
         * @return Context to merge template
         */
        Context context() throws JsonProcessingException;
    }
}
