package io.agileintelligence.ppmtool.service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private  ProjectService projectService;
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
        if (projectTask == null)
            return null;

        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);
        if (project== null) {
            throw new ProjectNotFoundException("Project " + projectIdentifier + " not found!");
        }
        projectTask.setProjectIdentifier(projectIdentifier);
        Backlog backlog = project.getBacklog();
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

    public Iterable<ProjectTask> findBacklogById(String id, String username) {
        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String sequence, String username) {
        projectService.findProjectByIdentifier(backlog_id, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("ProjectTask " + sequence + " not found!");
        }

        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("ProjectTask " + sequence + " not exist in Project " + backlog_id + "!");
        }

        return projectTask;
    }

    public ProjectTask updateProjectTask(ProjectTask newProject, String backlog_id, String pt_sequence, String username) {
        ProjectTask exist = findProjectTaskByProjectSequence(backlog_id, pt_sequence, username);
        /*
        * ProjectTask exist = newProject;
        * projectTaskRepository.save(exist);
        * */
        return projectTaskRepository.save(newProject);
    }
    public void deleteProjectTask(String backlog_id, String sequence, String username) {
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, sequence, username);
        projectTaskRepository.delete(projectTask);
    }
}
