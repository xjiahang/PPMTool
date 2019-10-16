package io.agileintelligence.ppmtool.web;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.service.MapValidationService;
import io.agileintelligence.ppmtool.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationService mapValidationService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

        ResponseEntity<?> errorMap = mapValidationService.validate(result);
        if (errorMap != null)
            return errorMap;
        //persist into database
        projectService.saveOrUpdateProject(project);
        //response
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> findProjectByIdentifier(@PathVariable String projectIdentifier) {
        Project project = projectService.findProjectByIdentifier(projectIdentifier);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> findAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<String> deleteProjectByIdentifier(@PathVariable String projectIdentifier) {
        projectService.deleteProjectByIdentifier(projectIdentifier);
        return new ResponseEntity<String>("Project '" + projectIdentifier + "' was deleted", HttpStatus.OK);
    }
}
