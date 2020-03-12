package io.agileintelligence.ppmtool.web;

import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.service.MapValidationService;
import io.agileintelligence.ppmtool.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationService mapValidationService;

    //http://localhost:8080/api/backlog/TEST2
    @PostMapping("/{backlog_id}") //same as project id
    public ResponseEntity<?> addProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result,
                                            @PathVariable String backlog_id,
                                            Principal principal) {
        ResponseEntity<?>errorMap = mapValidationService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask persistedPT = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());
        // what if persistedPT == null
        return new ResponseEntity<ProjectTask>(persistedPT, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getBacklog(@PathVariable String backlog_id, Principal principal) {
        return projectTaskService.findBacklogById(backlog_id, principal.getName());
    }

    @GetMapping("/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,
                                            @PathVariable String project_sequence,
                                            Principal principal) {
        ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(backlog_id, project_sequence, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PostMapping("/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                               BindingResult result,
                                               @PathVariable String backlog_id,
                                               @PathVariable String pt_sequence,
                                               Principal principal) {
        ResponseEntity<?> errorMap = mapValidationService.validate(result);
        if (errorMap != null) {
            return errorMap;
        }
        ProjectTask newProjectTask = projectTaskService.updateProjectTask(projectTask, backlog_id, pt_sequence, principal.getName());
        return new ResponseEntity<ProjectTask>(newProjectTask, HttpStatus.OK);
    }
    @DeleteMapping("/{backlog_id}/{project_sequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String project_sequence, Principal principal) {
        projectTaskService.deleteProjectTask(backlog_id, project_sequence, principal.getName());
        return new ResponseEntity<String>("ProjectTask " + project_sequence + " of Project " + backlog_id + " was deleted successfully",
                HttpStatus.OK);
    }
}
