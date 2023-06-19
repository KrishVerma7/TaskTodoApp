package com.example.tasktodoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert()
    suspend fun insertTask(todoModel: TodoModel):Long

    //to get all the task that are not finished yet
     @Query("Select * From TodoModel where isFinished == 0")
     fun getTask(): LiveData<List<TodoModel>>

     //to update task on right swipe
     @Query("Update TodoModel Set isFinished = 1 where id=:uid")
     fun finishedTask(uid:Long)

    //to delete task on left swipe
    @Query("Delete from TodoModel where id=:uid")
    fun deleteTask(uid:Long)
}