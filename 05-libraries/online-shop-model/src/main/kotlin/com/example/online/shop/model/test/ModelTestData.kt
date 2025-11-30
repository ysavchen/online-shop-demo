package com.example.online.shop.model.test

import com.example.online.shop.model.*
import com.example.online.shop.model.PriceValueUtils.MAX_PRICE_VALUE
import com.example.online.shop.model.PriceValueUtils.MIN_PRICE_VALUE
import org.apache.commons.lang3.RandomStringUtils
import java.math.BigDecimal
import kotlin.random.Random

object ModelTestData {

    private val randomAuthorLength = (AuthorUtils.MIN_LENGTH..AuthorUtils.MAX_LENGTH).random()
    private val randomBuildingLength = (BuildingUtils.MIN_LENGTH..BuildingUtils.MAX_LENGTH).random()
    private val randomCityLength = (CityUtils.MIN_LENGTH..CityUtils.MAX_LENGTH).random()
    private val randomCountryLength = (CountryUtils.MIN_LENGTH..CountryUtils.MAX_LENGTH).random()
    private val randomDescriptionLength = (DescriptionUtils.MIN_LENGTH..DescriptionUtils.MAX_LENGTH).random()
    private val randomPriceValue =
        Random.nextDouble(MIN_PRICE_VALUE.toDouble(), MAX_PRICE_VALUE.toDouble()).toBigDecimal()
    private val quantityRange = QuantityUtils.MIN_QUANTITY..QuantityUtils.MAX_QUANTITY
    private val ratingRange = RatingUtils.MIN_RATING..RatingUtils.MAX_RATING
    private val reviewTextRange = ReviewTextUtils.MIN_LENGTH..ReviewTextUtils.MAX_LENGTH
    private val searchQueryRange = SearchQueryUtils.MIN_LENGTH..SearchQueryUtils.MAX_LENGTH
    private val streetRange = StreetUtils.MIN_LENGTH..StreetUtils.MAX_LENGTH
    private val titleRange = TitleUtils.MIN_LENGTH..TitleUtils.MAX_LENGTH

    private val randomString = RandomStringUtils.insecure()

    fun author(length: Int = randomAuthorLength): Author = Author(randomString.nextAlphabetic(length))
    fun building(length: Int = randomBuildingLength): Building = Building(randomString.nextAlphabetic(length))
    fun city(length: Int = randomCityLength): City = City(randomString.nextAlphabetic(length))
    fun country(length: Int = randomCountryLength): Country = Country(randomString.nextAlphabetic(length))
    fun description(length: Int = randomDescriptionLength): Description =
        Description(randomString.nextAlphabetic(length))

    fun priceValue(value: BigDecimal = randomPriceValue) = PriceValue(value)

}