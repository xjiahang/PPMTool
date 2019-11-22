package io.agileintelligence.ppmtool.exceptions;

public class ProjectNotFoundExceptionResponse {
    private String ProjectNotFound;

    public ProjectNotFoundExceptionResponse (String projectNotFound) {
        //System.out.println("ProjectNotFoundExceptionResponse " + projectNotFound);
        this.ProjectNotFound = projectNotFound;
    }

    public String getProjectNotFound() {
        return ProjectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        ProjectNotFound = projectNotFound;
    }
}
