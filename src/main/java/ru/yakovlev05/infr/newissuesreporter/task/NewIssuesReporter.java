package ru.yakovlev05.infr.newissuesreporter.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.dao.RepositoryDao;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;
import ru.yakovlev05.infr.newissuesreporter.service.NewIssuesReporterService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
public class NewIssuesReporter {

    private final NewIssuesReporterService service;
    private final RepositoryDao repositoryDao;
    private final Supplier<Long> envNewIssuesReporterTaskDelaySupplier;

    private ScheduledExecutorService scheduler;


    public NewIssuesReporter(
            NewIssuesReporterService service,
            RepositoryDao repositoryDao,
            @Qualifier("envNewIssuesReporterTaskDelaySupplier") Supplier<Long> envNewIssuesReporterTaskDelaySupplier
    ) {
        this.service = service;
        this.repositoryDao = repositoryDao;
        this.envNewIssuesReporterTaskDelaySupplier = envNewIssuesReporterTaskDelaySupplier;
        init();
    }

    private void init() {
        List<Repository> toProcess = repositoryDao.findAllActiveRepositories();
        this.scheduler = Executors.newScheduledThreadPool(toProcess.size());

        toProcess.forEach(repository -> runInThread(runWithHandleException(repository)));

        log.info("Started processing {} repositories", toProcess.size());
    }

    private void runInThread(Runnable runnable) {
        scheduler.scheduleWithFixedDelay(
                runnable,
                1,
                envNewIssuesReporterTaskDelaySupplier.get(),
                TimeUnit.SECONDS
        );
    }

    private Runnable runWithHandleException(Repository repository) {
        return () -> {
            try {
                service.reportNewIssues(repository);
            } catch (Exception e) {
                log.error("Root error in new issues reporter", e);
            }
        };
    }

}
