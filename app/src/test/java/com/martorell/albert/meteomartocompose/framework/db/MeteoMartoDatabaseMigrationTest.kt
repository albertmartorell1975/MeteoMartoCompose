package com.martorell.albert.meteomartocompose.framework.db

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.lang.reflect.Proxy

class MeteoMartoDatabaseMigrationTest {

    @Test
    fun `migration 2 to 3 executes non-destructive alter statements for weatherConditionId and isNight`() {
        val executedSql = mutableListOf<String>()
        val recordingDatabase = createSqlRecorder(executedSql)

        assertEquals(2, MIGRATION_2_3.startVersion)
        assertEquals(3, MIGRATION_2_3.endVersion)

        MIGRATION_2_3.migrate(recordingDatabase)

        assertEquals(
            listOf(
                "ALTER TABLE CityWeather ADD COLUMN weatherConditionId INTEGER NOT NULL DEFAULT 0",
                "ALTER TABLE CityWeather ADD COLUMN isNight INTEGER NOT NULL DEFAULT 0",
            ),
            executedSql,
        )
        executedSql.forEach { sql ->
            val normalized = sql.uppercase()
            assertTrue("Unexpected destructive statement: $sql", !normalized.contains("DROP "))
            assertTrue("Unexpected destructive statement: $sql", !normalized.contains("DELETE "))
            assertTrue("Unexpected destructive statement: $sql", !normalized.contains("TRUNCATE "))
        }
    }

    @Test
    fun `exported Room v3 schema matches CityWeather migration columns and non-null defaults`() {
        val schemaFile = resolveSchemaFile()
        assertTrue("Expected exported Room schema at ${schemaFile.absolutePath}", schemaFile.isFile)

        val root = Json.parseToJsonElement(schemaFile.readText()).jsonObject
        val database = root.getValue("database").jsonObject
        assertEquals(3, database.getValue("version").jsonPrimitive.int)

        val entities = database.getValue("entities").jsonArray.map { it.jsonObject }
        val cityWeatherEntity = entities.firstOrNull {
            it.getValue("tableName").jsonPrimitive.content == "CityWeather"
        }
        assertNotNull("CityWeather entity must exist in exported v3 schema", cityWeatherEntity)

        val createSql = cityWeatherEntity!!.getValue("createSql").jsonPrimitive.content
        assertTrue(createSql.contains("`weatherConditionId` INTEGER NOT NULL DEFAULT 0"))
        assertTrue(createSql.contains("`isNight` INTEGER NOT NULL DEFAULT 0"))

        val fieldsByName = cityWeatherEntity.getValue("fields").jsonArray
            .map { it.jsonObject }
            .associateBy { it.getValue("columnName").jsonPrimitive.content }

        val conditionIdField = fieldsByName["weatherConditionId"]
        assertNotNull("weatherConditionId field missing from schema", conditionIdField)
        assertEquals("INTEGER", conditionIdField!!.getValue("affinity").jsonPrimitive.content)
        assertTrue(conditionIdField.getValue("notNull").jsonPrimitive.boolean)
        assertEquals("0", conditionIdField.getValue("defaultValue").jsonPrimitive.content)

        val isNightField = fieldsByName["isNight"]
        assertNotNull("isNight field missing from schema", isNightField)
        assertEquals("INTEGER", isNightField!!.getValue("affinity").jsonPrimitive.content)
        assertTrue(isNightField.getValue("notNull").jsonPrimitive.boolean)
        assertEquals("0", isNightField.getValue("defaultValue").jsonPrimitive.content)
    }

    private fun createSqlRecorder(executedSql: MutableList<String>): SupportSQLiteDatabase =
        Proxy.newProxyInstance(
            SupportSQLiteDatabase::class.java.classLoader,
            arrayOf(SupportSQLiteDatabase::class.java),
        ) { _, method, args ->
            when (method.name) {
                "execSQL" -> {
                    require(args != null && args.size == 1) {
                        "Expected single-argument execSQL(String), received ${args?.size ?: 0} args"
                    }
                    executedSql.add(args[0] as String)
                    Unit
                }
                else -> throw UnsupportedOperationException("Unexpected database call: ${method.name}")
            }
        } as SupportSQLiteDatabase

    private fun resolveSchemaFile(): File {
        val relativePath =
            "schemas/com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase/3.json"
        val moduleRelative = File(relativePath)
        return if (moduleRelative.isFile) moduleRelative else File("app/$relativePath")
    }
}
