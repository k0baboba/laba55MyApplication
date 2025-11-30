package com.example.laba55myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException

class PlanesViewModel : ViewModel() {

    private val _states = MutableLiveData<List<StateVector>>() // LiveData для списка самолётов
    val states: LiveData<List<StateVector>> = _states

    private val _statistics = MutableLiveData<PlanesStatistics>()
    val statistics: LiveData<PlanesStatistics> = _statistics

    // LiveData для ошибки обнуляемой
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


     // запуск загрузки и обработки данных о самолетах
    fun fetchPlanes() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = RetrofitInstance.api.getAllStates()
                if (response.isSuccessful) {
                    val allPlanes = response.body()?.states?.mapNotNull { StateVector.fromArray(it) } ?: emptyList()
                    processPlanesData(allPlanes)
                } else {
                    _error.postValue("API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                _error.postValue("Network error: ${e.message}")
            } catch (e: Exception) {
                _error.postValue("An error occurred: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


    // обрабатывает список самолетов, рассчитывает статистику и обновляет LiveData
    private fun processPlanesData(allPlanes: List<StateVector>) {
        val total = allPlanes.size
        val onGround = allPlanes.count { it.onGround }
        val inAir = total - onGround
        _statistics.postValue(PlanesStatistics(total, inAir, onGround))

        _states.postValue(allPlanes.take(100)) // первые 100 RecyclerView
    }

    // сброс ошибки
    fun onErrorShown() {
        _error.value = null
    }
}