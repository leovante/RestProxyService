package ru.vtb.stub.velocity;

import org.apache.velocity.Template;


public interface TemplateInitializer {

    /**
     * Init template from {@link String} content
     *
     * @param content {@link String} content
     * @return Template from {@link String} content
     */
    Template init(String content);
}
