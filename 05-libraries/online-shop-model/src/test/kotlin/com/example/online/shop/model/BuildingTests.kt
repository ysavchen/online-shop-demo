package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class BuildingTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 10

    @Test
    fun `valid building`() {
        val buildingRange = minLength..maxLength
        val minBuilding = randomString.nextAlphabetic(minLength)
        val randomBuilding = randomString.nextAlphabetic(buildingRange.random())
        val maxBuilding = randomString.nextAlphabetic(maxLength)

        listOf(minBuilding, randomBuilding, maxBuilding).forEach { Building.valueOf(it) }
    }

    @Test
    fun `invalid building length`() {
        val building = randomString.nextAlphabetic(maxLength + 1)
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