package com.bupticet.paperadmin.model;

import java.sql.Date;

public class ExerciseTO {
	private int exerciseToId;
	private String exerciseName;
	private int belongCourseId;
	public int getBelongCourseId() {
		return belongCourseId;
	}
	public void setBelongCourseId(int belongCourseId) {
		this.belongCourseId = belongCourseId;
	}
	public String getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	public int getExerciseToId() {
		return exerciseToId;
	}
	public void setExerciseToId(int exerciseToId) {
		this.exerciseToId = exerciseToId;
	}
	
}
