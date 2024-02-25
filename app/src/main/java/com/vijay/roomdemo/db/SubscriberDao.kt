package com.vijay.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SubscriberDao {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber):Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber) :Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber):Int

    @Query(value = "DELETE FROM subscriber_data_table")
    suspend fun deleteAll():Int

    @Query(value = "SELECT * FROM subscriber_data_table")
    fun getAllSubscriber(): LiveData<List<Subscriber>>
}