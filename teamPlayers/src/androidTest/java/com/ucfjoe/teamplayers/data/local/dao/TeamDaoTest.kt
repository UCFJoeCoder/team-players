package com.ucfjoe.teamplayers.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.ucfjoe.teamplayers.data.TeamPlayersDatabase
import com.ucfjoe.teamplayers.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TeamDaoTest {

    private lateinit var database: TeamPlayersDatabase
    private lateinit var teamDao: TeamDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TeamPlayersDatabase::class.java
        ).allowMainThreadQueries().build()
        teamDao = database.teamDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    // This tests "upsert"... from a insert perspective and "getTeam"
    @Test
    fun insertTeamTest() = runTest {
        val team = TeamEntity(name = "TestTeam", 1)
        val id = teamDao.upsertTeam(team)

        val insertedTeam = teamDao.getTeam(id)

        Truth.assertThat(team).isEqualTo(insertedTeam)
    }

    @Test
    fun updateTeamTest() = runTest {
        val team = TeamEntity("SomeTeam") // Create team
        val id = teamDao.upsertTeam(team) // Insert team, get Id
        val insertedTeam = teamDao.getTeam(id) // Get team based on Id

        Truth.assertThat(insertedTeam).isNotNull() // Verify a team was returned

        val teamWithUpdates =
            TeamEntity("UpdatedTeamName", insertedTeam!!.id) // create team with same id, new name

        teamDao.upsertTeam(teamWithUpdates) // update team
        val updatedTeam = teamDao.getTeam(id) // Get team based on original Id

        Truth.assertThat(updatedTeam).isNotNull() // Verify a team was returned
        Truth.assertThat(insertedTeam)
            .isNotEqualTo(updatedTeam) // make sure the teams are different
        Truth.assertThat(insertedTeam.id)
            .isEqualTo(updatedTeam!!.id) // Check that the ids of the inserted and updated teams are the same
        Truth.assertThat(insertedTeam.name)
            .isNotEqualTo(updatedTeam.name) // Check that the names are different
    }

    @Test
    fun deleteTeamTest() = runTest {
        val team = TeamEntity("MyTestTeam", 1)
        val id = teamDao.upsertTeam(team)
        teamDao.deleteTeam(team)

        val retrievedTeam = teamDao.getTeam(id)

        Truth.assertThat(retrievedTeam).isNull()
    }

    @Test
    fun getTeamsTest() = runTest {
        // Create a list of alphabetized names
        val names = listOf("Alpha", "Charlie", "November", "Whiskey", "Zulu")
        // Loops through the shuffled list and add each new TeamEntity to the database
        names.shuffled().forEach {
            teamDao.upsertTeam(TeamEntity(it))
        }

        // getTeams will sort the list alphabetically
        val retrievedTeams = teamDao.getTeams().first()
        // Verify data was returned, and the size is equal to the number of names
        Truth.assertThat(retrievedTeams).isNotNull()
        Truth.assertThat(retrievedTeams.size).isEqualTo(names.size)
        // Verify each returned Team has the same name as the names in the list by index.
        // This proves they are sorted alphabetically
        retrievedTeams.forEachIndexed { index, _ ->
            Truth.assertThat(retrievedTeams[index].name).isEqualTo(names[index])
        }
    }

    @Test
    fun getTeamsWithNameCaseInsensitiveTest() = runTest {
        val foundCount = 1
        val notFoundCount = 0
        val insertName = "GoodTeam"
        val insertNameDifferentCase = "GoOdTeAm"
        val notInsertedName = "BadName"
        teamDao.upsertTeam(TeamEntity(insertName))

        // Perform search when names have the same case
        val count = teamDao.getTeamsWithName(insertName)
        Truth.assertThat(count).isEqualTo(foundCount)

        // Perform search when names do not have the same case
        val goodCount = teamDao.getTeamsWithName(insertNameDifferentCase)
        Truth.assertThat(goodCount).isEqualTo(foundCount)

        // Perform search for name that does not exist
        val missingCount = teamDao.getTeamsWithName(notInsertedName)
        Truth.assertThat(missingCount).isEqualTo(notFoundCount)
    }

    @Test
    fun getTeamsWithNameCaseSensitiveTest() = runTest {
        val foundCount = 1
        val notFoundCount = 0
        val insertName = "GoodTeam"
        val insertNameDifferentCase = "GoOdtEaM"
        val notInsertedName = "BadName"
        teamDao.upsertTeam(TeamEntity(insertName))

        // Perform search when names have the same case
        val count = teamDao.getTeamsWithNameCaseSensitive(insertName)
        Truth.assertThat(count).isEqualTo(foundCount)

        // Perform search when names do not have the same case
        val goodCount = teamDao.getTeamsWithNameCaseSensitive(insertNameDifferentCase)
        Truth.assertThat(goodCount).isEqualTo(notFoundCount)

        // Perform search for name that does not exist
        val missingCount = teamDao.getTeamsWithNameCaseSensitive(notInsertedName)
        Truth.assertThat(missingCount).isEqualTo(notFoundCount)
    }

    @Test
    fun getTeamWithGamesTest() = runTest {
        // TODO: Maybe test in another test class... this requires gameDao to insert test data

        //  @Query("SELECT * FROM teams WHERE id=:teamId")
        //  fun getTeamWithGames(teamId: Long): Flow<TeamWithGamesRelation>
    }

    @Test
    fun getTeamWithPlayers() = runTest {
        // TODO: This might not be used at all
        // TODO: Maybe test in another test class... this requires playerDao to insert test data

        //  @Query("SELECT * FROM teams WHERE id=:teamId")
        //  fun getTeamWithPlayers(teamId: Long): Flow<TeamWithPlayersRelation>
    }

}