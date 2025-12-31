package ru.yakovlev05.infr.newissuesreporter.task;

import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.service.NewIssuesReporterService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class NewIssuesReporter {

    private final NewIssuesReporterService service;
    private final Supplier<Long> envNewIssuesReporterTaskDelaySupplier;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public NewIssuesReporter(
            NewIssuesReporterService service,
            Supplier<Long> envNewIssuesReporterTaskDelaySupplier
    ) {
        this.service = service;
        this.envNewIssuesReporterTaskDelaySupplier = envNewIssuesReporterTaskDelaySupplier;
        init();
    }

    private void init() {
        scheduler.scheduleWithFixedDelay(
                service::reportNewIssues,
                0,
                envNewIssuesReporterTaskDelaySupplier.get(),
                TimeUnit.SECONDS
        );
    }

}
