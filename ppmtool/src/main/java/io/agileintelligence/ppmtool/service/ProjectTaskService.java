package io.agileintelligence.ppmtool.service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        if (projectTask == null)
            return null;
        if (projectRepository.findByProjectIdentifier(projectIdentifier) == null) {
            throw new ProjectNotFoundException("Project " + projectIdentifier + " not found!");
        }
        projectTask.setProjectIdentifier(projectIdentifier);
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        projectTask.setBacklog(backlog);
        Integer sequence = backlog.getPTSequence() + 1;
        backlog.setPTSequence(sequence);
        projectTask.setProjectSequence(projectIdentifier + "-" + sequence);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
            projectTask.setStatus("TODO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        if (projectRepository.findByProjectIdentifier(id) == null) {
            throw new ProjectNotFoundException("Project " + id + " not found!");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
