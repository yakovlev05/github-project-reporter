package ru.yakovlev05.infr.newissuesreporter.service;

import org.springframework.stereotype.Service;

@Service
public class NewIssuesReporterService {

    public void reportNewIssues(){
        IO.println("parse new issues");
    }

}
