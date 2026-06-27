package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tournament_state")
data class TournamentStateEntity(
    @PrimaryKey val id: Int = 1,
    val phase: String = "SETUP", // SETUP, SEMIFINALS, FINALS, PODIUM
    
    // Players (Name and Team)
    val p1Name: String = "", val p1Team: String = "",
    val p2Name: String = "", val p2Team: String = "",
    val p3Name: String = "", val p3Team: String = "",
    val p4Name: String = "", val p4Team: String = "",
    
    // SF pairings (Player IDs 1, 2, 3, 4)
    val sf1HomeId: Int = 1, val sf1AwayId: Int = 2,
    val sf2HomeId: Int = 3, val sf2AwayId: Int = 4,
    
    // SF1 Leg 1 (home is sf1HomeId, away is sf1AwayId)
    val sf1L1HomeGoals: Int = 0, val sf1L1AwayGoals: Int = 0, val sf1L1Completed: Boolean = false,
    // SF1 Leg 2 (home is sf1AwayId, away is sf1HomeId)
    val sf1L2HomeGoals: Int = 0, val sf1L2AwayGoals: Int = 0, val sf1L2Completed: Boolean = false,
    
    // SF2 Leg 1 (home is sf2HomeId, away is sf2AwayId)
    val sf2L1HomeGoals: Int = 0, val sf2L1AwayGoals: Int = 0, val sf2L1Completed: Boolean = false,
    // SF2 Leg 2 (home is sf2AwayId, away is sf2HomeId)
    val sf2L2HomeGoals: Int = 0, val sf2L2AwayGoals: Int = 0, val sf2L2Completed: Boolean = false,
    
    // Final (home is SF1 Winner, away is SF2 Winner)
    val finalHomeGoals: Int = 0, val finalAwayGoals: Int = 0, val finalCompleted: Boolean = false,
    // Third place (home is SF1 Loser, away is SF2 Loser)
    val thirdHomeGoals: Int = 0, val thirdAwayGoals: Int = 0, val thirdCompleted: Boolean = false,
    
    // Final Rankings (Player IDs 1-4)
    val championId: Int = 0,
    val secondId: Int = 0,
    val thirdId: Int = 0,
    val fourthId: Int = 0
)

@Dao
interface TournamentStateDao {
    @Query("SELECT * FROM tournament_state WHERE id = 1 LIMIT 1")
    fun getTournamentState(): Flow<TournamentStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTournamentState(state: TournamentStateEntity)

    @Query("DELETE FROM tournament_state")
    suspend fun clearTournamentState()
}

@Database(entities = [TournamentStateEntity::class], version = 1, exportSchema = false)
abstract class TournamentDatabase : RoomDatabase() {
    abstract fun tournamentStateDao(): TournamentStateDao
}

class TournamentRepository(private val dao: TournamentStateDao) {
    fun getTournamentState(): Flow<TournamentStateEntity?> = dao.getTournamentState()
    
    suspend fun saveTournamentState(state: TournamentStateEntity) {
        dao.saveTournamentState(state)
    }
    
    suspend fun clearTournamentState() {
        dao.clearTournamentState()
    }
}
