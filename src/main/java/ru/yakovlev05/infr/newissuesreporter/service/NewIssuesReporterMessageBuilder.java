package ru.yakovlev05.infr.newissuesreporter.service;

import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.dto.IssueDto;
import ru.yakovlev05.infr.newissuesreporter.dto.UserDto;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

import java.util.stream.Collectors;

@Component
public class NewIssuesReporterMessageBuilder {

    public String build(Repository repository, IssueDto issueDto) {
        return chooseByAssignees(repository, issueDto);
    }

    public String chooseByAssignees(Repository repository, IssueDto issueDto) {
        if (issueDto.assignees().totalCount() == 1) {
            return buildSingleAssignee(repository, issueDto);
        } else {
            return buildMultipleAssignees(repository, issueDto);
        }
    }

    private String buildSingleAssignee(Repository repository, IssueDto issueDto) {
        UserDto assignee = issueDto.assignees().nodes().getFirst();
        return "*%s* создал задачу \"\\[%s\\] [\\#%d](%s) %s\" и назначил ответственного \\- *%s*"
                .formatted(
                        safeString(issueDto.author().login()),
                        safeString(repository.getName()),
                        issueDto.number(),
                        issueDto.url(),
                        safeString(issueDto.title()),
                        assignee.login()
                );
    }

    private String buildMultipleAssignees(Repository repository, IssueDto issueDto) {
        String assigneesList = issueDto.assignees().nodes().stream()
                .map(assignee -> "\\- *%s*".formatted(safeString(assignee.login())))
                .collect(Collectors.joining("\n"));

        return "*%s* создал задачу \"\\[%s\\] [\\#%d](%s) %s\" и назначил ответственных:\n%s"
                .formatted(
                        safeString(issueDto.author().login()),
                        safeString(repository.getName()),
                        issueDto.number(),
                        issueDto.url(),
                        safeString(issueDto.title()),
                        assigneesList
                );
    }

    private String safeString(String string){
        return string.replaceAll("([_*\\[\\]()~`>#+-=|{}.!])", "\\\\$1");
    }
}
