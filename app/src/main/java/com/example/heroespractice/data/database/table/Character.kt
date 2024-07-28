package com.example.heroespractice.data.database.table

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Entity(tableName = "characters")
data class CharacterEntity(

	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "id")
	val id: Long,

	@ColumnInfo(name = "name")
	val name: String,

	@ColumnInfo(name = "is_description")
	val isDescription: Boolean,

	@ColumnInfo(name = "description")
	val description: String,

	@ColumnInfo(name = "thumbnail")
	val thumbnail: String,

	@ColumnInfo(name = "comics_quantity")
	val comicsQuantity: Int,

	@ColumnInfo(name = "series_quantity")
	val seriesQuantity: Int,

	@ColumnInfo(name = "stories_quantity")
	val storiesQuantity: Int,

	@ColumnInfo(name = "events_quantity")
	val eventsQuantity: Int,

	@ColumnInfo(name = "plots_url")
	val plotsUrl: String,

	@ColumnInfo(name = "date_time")
	val dateTime: String,

	)

@Dao
interface CharacterDao {

	@Query("SELECT * FROM characters")
	fun getCharacters(): Single<List<CharacterEntity>>

	@Query("SELECT * FROM characters WHERE id = :characterId LIMIT 1")
	fun getCharacterByCharacterId(characterId: Long): Single<List<CharacterEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCharacter(characters: List<CharacterEntity>): Completable

}