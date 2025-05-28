package com.aioveu.controller;

import com.aioveu.entity.GradeClassroom;
import com.aioveu.form.ActiveVenueFieldForm;
import com.aioveu.service.GradeClassroomService;
import com.aioveu.vo.AvailableIdNameVO;
import com.aioveu.vo.FieldDayPlanVO;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/grade-classroom")
public class GradeClassroomController {

    @Autowired
    private GradeClassroomService gradeClassroomService;

    @GetMapping("/list/{storeId}")
    public List<IdNameVO> list(@PathVariable Long storeId) {
        return gradeClassroomService.getByStoreId(storeId);
    }

    @GetMapping("/{id}")
    public GradeClassroom detail(@PathVariable Long id) {
        return gradeClassroomService.getById(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody GradeClassroom gradeClassroom) {
        return gradeClassroomService.updateById(gradeClassroom);
    }

    @PostMapping("")
    public boolean create(@RequestBody GradeClassroom gradeClassroom) {
        return gradeClassroomService.save(gradeClassroom);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return gradeClassroomService.removeById(id);
    }

    @GetMapping("/plan/{venueId}")
    public FieldDayPlanVO fieldPlayByVenueId(@PathVariable Long venueId, @RequestParam String day) {
        return gradeClassroomService.fieldPlayByVenueId(venueId, day);
    }

    @GetMapping("/venue/{venueId}")
    public List<IdNameVO> getByVenueId(@PathVariable Long venueId) {
        return gradeClassroomService.getByVenueId(venueId);
    }

    @PostMapping("/active")
    public List<AvailableIdNameVO> getActiveClassroomList(@Valid @RequestBody ActiveVenueFieldForm form) {
        return gradeClassroomService.getActiveClassroomList(form);
    }



}
