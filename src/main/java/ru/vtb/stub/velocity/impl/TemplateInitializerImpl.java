package ru.vtb.stub.velocity.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeSingleton;
import ru.vtb.stub.velocity.TemplateInitializer;

import java.io.StringReader;

public class TemplateInitializerImpl implements TemplateInitializer {

    private final VelocityEngine velocityEngine;

    public TemplateInitializerImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * Init template from {@link String} content
     *
     * @param content {@link String} content
     * @return Template from {@link String} content
     */
    @Override
    public Template init(String content) {
        try {
            Template template = new Template();
            template.setRuntimeServices(RuntimeSingleton.getRuntimeServices());

            /*
             * The following line works for Velocity version up to 1.7
             * For version 2, replace "Template name" with the variable, template
             */
            template.setData(RuntimeSingleton.getRuntimeServices().parse(new StringReader(content), RandomStringUtils.random(10)));
            template.initDocument();
            return template;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
