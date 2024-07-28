package com.example.heroespractice.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.heroespractice.data.database.table.CharacterDao
import com.example.heroespractice.data.database.table.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
	abstract fun characterDao(): CharacterDao

	companion object {
		val MIGRATION_6_7 = object : Migration(6, 7) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters ADD COLUMN is_description INTEGER NOT NULL DEFAULT 0")
			}
		}

		val MIGRATION_5_6 = object : Migration(5, 6) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters RENAME TO characters_old")

				db.execSQL(
					"""
            CREATE TABLE IF NOT EXISTS characters (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                thumbnail TEXT NOT NULL,
                comics_quantity INTEGER NOT NULL,
                series_quantity INTEGER NOT NULL,
                stories_quantity INTEGER NOT NULL,
                events_quantity INTEGER NOT NULL,
                plots_url TEXT NOT NULL,
                date_time TEXT NOT NULL
            )
        """
				)

				db.execSQL(
					"""
            INSERT OR REPLACE INTO characters (id, name, description, thumbnail, comics_quantity, 
                                    series_quantity, stories_quantity, events_quantity, 
                                    plots_url, date_time)
            SELECT id, name, description, thumbnail, comicsQuantity, series_quantity, 
                   stories_quantity, events_quantity, plots_url, timestamp
            FROM characters_old
        """
				)

				db.execSQL("DROP TABLE characters_old")
			}
		}

		val MIGRATION_4_5 = object : Migration(4, 5) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
			}
		}

		val MIGRATION_3_4 = object : Migration(3, 4) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters ADD COLUMN plots_url TEXT NOT NULL DEFAULT ''")
			}
		}

		val MIGRATION_2_3 = object : Migration(2, 3) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters ADD COLUMN series_quantity INTEGER NOT NULL DEFAULT 0")
				db.execSQL("ALTER TABLE characters ADD COLUMN stories_quantity INTEGER NOT NULL DEFAULT 0")
				db.execSQL("ALTER TABLE characters ADD COLUMN events_quantity INTEGER NOT NULL DEFAULT 0")
			}
		}

		val MIGRATION_1_2 = object : Migration(1, 2) {
			override fun migrate(db: SupportSQLiteDatabase) {
				db.execSQL("ALTER TABLE characters ADD COLUMN comicsQuantity INTEGER NOT NULL DEFAULT 0")
			}
		}
	}
}