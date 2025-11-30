package com.example.online.shop.model

import com.example.online.shop.model.BuildingUtils.MAX_BUILDING_LENGTH
import com.example.online.shop.model.BuildingUtils.MIN_BUILDING_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class BuildingTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid building`() {
        val buildingRange = MIN_BUILDING_LENGTH..MAX_BUILDING_LENGTH
        val minBuilding = randomString.nextAlphanumeric(MIN_BUILDING_LENGTH)
        val randomBuilding = randomString.nextAlphanumeric(buildingRange.random())
        val maxBuilding = randomString.nextAlphanumeric(MAX_BUILDING_LENGTH)

        listOf(minBuilding, randomBuilding, maxBuilding).forEach { Building.valueOf(it) }
    }

    @Test
    fun `invalid building length`() {
        val building = randomString.nextAlphanumeric(MAX_BUILDING_LENGTH + 1)
        val exception = assertThrows<ModelValidationException> { Building.valueOf(building) }
        assertContains(exception.message!!, "invalid building", true)
    }

    @Test
    fun `invalid blank building`() {
        val emptyBuilding = ""
        val blankBuilding = " "
        listOf(emptyBuilding, blankBuilding).forEach {
            val exception = assertThrows<ModelValidationException> { Building.valueOf(it) }
            assertContains(exception.message!!, "invalid building", true)
        }
    }
}