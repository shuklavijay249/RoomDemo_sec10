package com.vijay.roomdemo.db

class SubscriberRepository(private val Dao: SubscriberDao) {

    val subscriber = Dao.getAllSubscriber()

    suspend fun insert(subscriber: Subscriber):Long{
        return Dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber):Int{
        return Dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber):Int{
        return Dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll():Int{
        return Dao.deleteAll()
    }

}