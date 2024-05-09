package org.itone.trello.taskservice.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackColorful extends ForegroundCompositeConverterBase<ILoggingEvent>{
    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        switch (level.toInt()) {
            // ERROR level is red
            case Level.ERROR_INT:
                return ANSIConstants.RED_FG;
            // WARN level is yellow
            case Level.WARN_INT:
                return ANSIConstants.YELLOW_FG;
            // INFO level is blue
            case Level.INFO_INT:
                return ANSIConstants.BLUE_FG;
            // DEBUG level is green
            case Level.DEBUG_INT:
                return ANSIConstants.GREEN_FG;
            // Other is the default color
            default:
                return ANSIConstants.DEFAULT_FG;
        }
    }
}
