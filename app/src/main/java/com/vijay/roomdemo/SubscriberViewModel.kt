package com.vijay.roomdemo

import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.vijay.roomdemo.db.Subscriber
import com.vijay.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Files.delete

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    val subscribers = repository.subscriber

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateAndDelete: Subscriber

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            subscriberToUpdateAndDelete.name = inputName.value!!
            subscriberToUpdateAndDelete.email = inputEmail.value!!
            update(subscriberToUpdateAndDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))
            inputName.value = ""
            inputEmail.value = ""
        }

    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateAndDelete)
        } else {
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val rowId = repository.insert(subscriber)
        withContext(Dispatchers.Main) {
            if (rowId > -1) {
                statusMessage.value = Event("Data Inserted Successfully $rowId")
            } else {
                statusMessage.value = Event("Error Occured!")
            }
        }
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.update(subscriber)
        withContext(Dispatchers.Main) {
            if (numberOfRows > 0) {
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("$numberOfRows Updated Successfully!")
            } else {
                statusMessage.value = Event("Error Occured!")
            }

        }
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val noOfRowsDeleted = repository.delete(subscriber)
        withContext(Dispatchers.Main) {
            if (noOfRowsDeleted > 0) {
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("$noOfRowsDeleted Rows Deleted Successfully")
            } else {
                statusMessage.value = Event("Error Occured!")
            }
        }
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        val noOfRowsDeleted = repository.deleteAll()
        withContext(Dispatchers.Main) {
            if (noOfRowsDeleted > 0) {
                statusMessage.value = Event("$noOfRowsDeleted Rows Deleted Successfully")
            } else {
                statusMessage.value = Event("Error Occured!")
            }
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateAndDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"

    }
}