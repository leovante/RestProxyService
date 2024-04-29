package ru.vtb.stub.config;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Plugin(name = "MyAppender", category = Core.CATEGORY_NAME, elementType = LogAppender.ELEMENT_TYPE)
public class LogAppender extends AbstractAppender {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    protected LogAppender(String name, Filter filter) {
        super(name, filter, null, true, Property.EMPTY_ARRAY);
    }

    @PluginFactory
    public static LogAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new LogAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + " : Thread:[" + Thread.currentThread().getName()
                + "] : " + event.getMessage().getFormattedMessage());
    }

}
