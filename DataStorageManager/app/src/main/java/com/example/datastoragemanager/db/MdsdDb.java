package com.example.datastoragemanager.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.datastoragemanager.data.Student;
import com.example.datastoragemanager.data.StudentDao;

import java.util.List;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class MdsdDb extends RoomDatabase {

    public abstract StudentDao getStudentDao();
}
