package ru.home_work.t1_school.props;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.home_work.t1_starter.aspect.config.LogConfig;

@Configuration
@Import(LogConfig.class)
public class ProjectProperties {
}
