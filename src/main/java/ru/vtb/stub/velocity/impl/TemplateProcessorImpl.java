package ru.vtb.stub.velocity.impl;

import org.apache.velocity.Template;
import ru.vtb.stub.velocity.TemplateProcessor;

import java.io.StringWriter;


public class TemplateProcessorImpl implements TemplateProcessor {

    /**
     * Process velocity {@link Template} by Velocity {@link org.apache.velocity.context.Context}
     *
     * @param template         Velocity {@link Template}
     * @param contextProcessor {@link ContextProcessor}
     * @return {@link String} From processed {@link Template}
     */
    @Override
    public String process(Template template, ContextProcessor contextProcessor) {
        StringWriter writer = new StringWriter();
        try {
            template.merge(contextProcessor.context(), writer);
            return writer.toString();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
