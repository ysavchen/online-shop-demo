package com.example.online.shop.model

import com.example.online.shop.model.BuildingUtils.MAX_LENGTH
import com.example.online.shop.model.BuildingUtils.MIN_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class BuildingTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid building`() {
        val buildingRange = MIN_LENGTH..MAX_LENGTH
        val minBuilding = randomString.nextAlphabetic(MIN_LENGTH)
        val randomBuilding = randomString.nextAlphabetic(buildingRange.random())
        val maxBuilding = randomString.nextAlphabetic(MAX_LENGTH)

        listOf(minBuilding, randomBuilding, maxBuilding).forEach { Building.valueOf(it) }
    }

    @Test
    fun `invalid building length`() {
        val building = randomString.nextAlphabetic(MAX_LENGTH + 1)
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