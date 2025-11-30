package com.example.online.shop.model.test

import com.example.online.shop.model.*
import org.apache.commons.lang3.RandomStringUtils

object ModelTestData {

    private val authorRange = AuthorUtils.MIN_LENGTH..AuthorUtils.MAX_LENGTH
    private val buildingRange = BuildingUtils.MIN_LENGTH..BuildingUtils.MAX_LENGTH
    private val cityRange = CityUtils.MIN_LENGTH..CityUtils.MAX_LENGTH
    private val countryRange = CountryUtils.MIN_LENGTH..CountryUtils.MAX_LENGTH
    private val descriptionRange = DescriptionUtils.MIN_LENGTH..DescriptionUtils.MAX_LENGTH
    private val priceValueRange = PriceValueUtils.MIN_PRICE_VALUE..PriceValueUtils.MAX_PRICE_VALUE
    private val quantityRange = QuantityUtils.MIN_QUANTITY..QuantityUtils.MAX_QUANTITY
    private val ratingRange = RatingUtils.MIN_RATING..RatingUtils.MAX_RATING
    private val reviewTextRange = ReviewTextUtils.MIN_LENGTH..ReviewTextUtils.MAX_LENGTH
    private val searchQueryRange = SearchQueryUtils.MIN_LENGTH..SearchQueryUtils.MAX_LENGTH
    private val streetRange = StreetUtils.MIN_LENGTH..StreetUtils.MAX_LENGTH
    private val titleRange = TitleUtils.MIN_LENGTH..TitleUtils.MAX_LENGTH

    private val randomString = RandomStringUtils.insecure()

    fun author(length: Int = authorRange.random()): Author = Author(randomString.nextAlphabetic(length))
    fun building(length: Int = buildingRange.random()): Building = Building(randomString.nextAlphabetic(length))
    fun city(length: Int = cityRange.random()): City = City(randomString.nextAlphabetic(length))

}