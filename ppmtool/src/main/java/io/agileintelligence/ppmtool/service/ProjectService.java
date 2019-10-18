package io.agileintelligence.ppmtool.service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;
    //persist object into database
    public Project saveOrUpdateProject(Project project) {
        if (project == null)
            return null;
        try {
            Backlog backlog;
            if (project.getId() == null) {
                backlog = new Backlog();
                backlog.setProjectIdentifier(project.getProjectIdentifier());
                backlog.setProject(project);

                project.setBacklog(backlog);
            } else {
                backlog = backlogRepository.findByProjectIdentifier(project.getProjectIdentifier());
                project.setBacklog(backlog);
            }
            return projectRepository.save(project);
        } catch (Exception e) {
           throw new ProjectIdentifierException("Project ID '" + project.getProjectIdentifier() + "already exists!");
        }
    }

    public Project findProjectByIdentifier(String identifier) {
        Project project = projectRepository.findByProjectIdentifier(identifier);
        if (project == null) {
            throw new ProjectIdentifierException("Project Id '" + identifier + "' does not exist!");
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier (String identifier) {
        Project project = findProjectByIdentifier(identifier);
        if (project == null) {
            throw new ProjectIdentifierException("Project Id '" + identifier + "' does not exist!");
        }

        projectRepository.delete(project);
    }
}
