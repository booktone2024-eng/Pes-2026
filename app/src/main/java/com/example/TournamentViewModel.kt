package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.TournamentDatabase
import com.example.data.TournamentRepository
import com.example.data.TournamentStateEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TournamentViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database by lazy {
        Room.databaseBuilder(
            application,
            TournamentDatabase::class.java,
            "tournament_database"
        ).fallbackToDestructiveMigration().build()
    }
    
    private val repository by lazy {
        TournamentRepository(database.tournamentStateDao())
    }
    
    private val _uiState = MutableStateFlow(TournamentStateEntity())
    val uiState: StateFlow<TournamentStateEntity> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.getTournamentState().collect { savedState ->
                if (savedState != null) {
                    _uiState.value = savedState
                } else {
                    // Seed initial empty state
                    val initialState = TournamentStateEntity()
                    _uiState.value = initialState
                    repository.saveTournamentState(initialState)
                }
            }
        }
    }
    
    fun updatePlayerInfo(
        p1Name: String, p1Team: String,
        p2Name: String, p2Team: String,
        p3Name: String, p3Team: String,
        p4Name: String, p4Team: String
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                p1Name = p1Name, p1Team = p1Team,
                p2Name = p2Name, p2Team = p2Team,
                p3Name = p3Name, p3Team = p3Team,
                p4Name = p4Name, p4Team = p4Team
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun performDrawAndStart() {
        viewModelScope.launch {
            val currentState = _uiState.value
            // Randomly pair player IDs 1, 2, 3, 4
            val playerIds = listOf(1, 2, 3, 4).shuffled()
            val sf1Home = playerIds[0]
            val sf1Away = playerIds[1]
            val sf2Home = playerIds[2]
            val sf2Away = playerIds[3]
            
            val updated = currentState.copy(
                phase = "SEMIFINALS",
                sf1HomeId = sf1Home,
                sf1AwayId = sf1Away,
                sf2HomeId = sf2Home,
                sf2AwayId = sf2Away,
                
                // Reset matches
                sf1L1HomeGoals = 0, sf1L1AwayGoals = 0, sf1L1Completed = false,
                sf1L2HomeGoals = 0, sf1L2AwayGoals = 0, sf1L2Completed = false,
                sf2L1HomeGoals = 0, sf2L1AwayGoals = 0, sf2L1Completed = false,
                sf2L2HomeGoals = 0, sf2L2AwayGoals = 0, sf2L2Completed = false,
                
                finalHomeGoals = 0, finalAwayGoals = 0, finalCompleted = false,
                thirdHomeGoals = 0, thirdAwayGoals = 0, thirdCompleted = false,
                
                championId = 0, secondId = 0, thirdId = 0, fourthId = 0
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateSF1Leg1(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                sf1L1HomeGoals = homeGoals,
                sf1L1AwayGoals = awayGoals,
                sf1L1Completed = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateSF1Leg2(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                sf1L2HomeGoals = homeGoals,
                sf1L2AwayGoals = awayGoals,
                sf1L2Completed = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateSF2Leg1(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                sf2L1HomeGoals = homeGoals,
                sf2L1AwayGoals = awayGoals,
                sf2L1Completed = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateSF2Leg2(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                sf2L2HomeGoals = homeGoals,
                sf2L2AwayGoals = awayGoals,
                sf2L2Completed = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun advanceToFinals(sf1WinnerId: Int, sf1LoserId: Int, sf2WinnerId: Int, sf2LoserId: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                phase = "FINALS",
                championId = sf1WinnerId, // Temporary Finalist 1
                secondId = sf2WinnerId,   // Temporary Finalist 2
                thirdId = sf1LoserId,     // Temporary 3rd Contender 1
                fourthId = sf2LoserId     // Temporary 3rd Contender 2
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateFinalMatch(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                finalHomeGoals = homeGoals,
                finalAwayGoals = awayGoals,
                finalCompleted = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun updateThirdPlaceMatch(homeGoals: Int, awayGoals: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                thirdHomeGoals = homeGoals,
                thirdAwayGoals = awayGoals,
                thirdCompleted = completed
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun finishTournament(
        champ: Int,
        runnerUp: Int,
        third: Int,
        fourth: Int
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val updated = currentState.copy(
                phase = "PODIUM",
                championId = champ,
                secondId = runnerUp,
                thirdId = third,
                fourthId = fourth
            )
            _uiState.value = updated
            repository.saveTournamentState(updated)
        }
    }
    
    fun resetTournament() {
        viewModelScope.launch {
            val resetState = TournamentStateEntity(
                id = 1,
                phase = "SETUP",
                p1Name = _uiState.value.p1Name, p1Team = _uiState.value.p1Team,
                p2Name = _uiState.value.p2Name, p2Team = _uiState.value.p2Team,
                p3Name = _uiState.value.p3Name, p3Team = _uiState.value.p3Team,
                p4Name = _uiState.value.p4Name, p4Team = _uiState.value.p4Team
            )
            _uiState.value = resetState
            repository.saveTournamentState(resetState)
        }
    }
}
