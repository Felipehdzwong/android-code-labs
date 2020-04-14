package com.example.datastoragemanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void InsertStudent(Student student);

    @Query("SELECT * from student")
    List<Student> selectAll();
}
