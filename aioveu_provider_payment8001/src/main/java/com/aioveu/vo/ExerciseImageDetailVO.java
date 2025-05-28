package com.aioveu.vo;

import com.aioveu.entity.ExerciseImage;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 0001 23:00
 */
@Data
public class ExerciseImageDetailVO extends ExerciseImage {

    private String imageTypeName;

    private String exerciseName;

}
