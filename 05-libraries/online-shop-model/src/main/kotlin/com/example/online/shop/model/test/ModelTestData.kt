package com.example.online.shop.model.test

import com.example.online.shop.model.*
import com.example.online.shop.model.AuthorUtils.MAX_AUTHOR_LENGTH
import com.example.online.shop.model.AuthorUtils.MIN_AUTHOR_LENGTH
import com.example.online.shop.model.BuildingUtils.MAX_BUILDING_LENGTH
import com.example.online.shop.model.BuildingUtils.MIN_BUILDING_LENGTH
import com.example.online.shop.model.CityUtils.MAX_CITY_LENGTH
import com.example.online.shop.model.CityUtils.MIN_CITY_LENGTH
import com.example.online.shop.model.CountryUtils.MAX_COUNTRY_LENGTH
import com.example.online.shop.model.CountryUtils.MIN_COUNTRY_LENGTH
import com.example.online.shop.model.DescriptionUtils.MAX_DESCRIPTION_LENGTH
import com.example.online.shop.model.DescriptionUtils.MIN_DESCRIPTION_LENGTH
import com.example.online.shop.model.PriceValueUtils.MAX_PRICE_VALUE
import com.example.online.shop.model.PriceValueUtils.MIN_PRICE_VALUE
import com.example.online.shop.model.QuantityUtils.MAX_QUANTITY
import com.example.online.shop.model.QuantityUtils.MIN_QUANTITY
import com.example.online.shop.model.RatingUtils.MAX_RATING
import com.example.online.shop.model.RatingUtils.MIN_RATING
import com.example.online.shop.model.ReviewTextUtils.MAX_REVIEW_TEXT_LENGTH
import com.example.online.shop.model.ReviewTextUtils.MIN_REVIEW_TEXT_LENGTH
import com.example.online.shop.model.SearchQueryUtils.MAX_SEARCH_QUERY_LENGTH
import com.example.online.shop.model.SearchQueryUtils.MIN_SEARCH_QUERY_LENGTH
import com.example.online.shop.model.StreetUtils.MAX_STREET_LENGTH
import com.example.online.shop.model.StreetUtils.MIN_STREET_LENGTH
import com.example.online.shop.model.TitleUtils.MAX_TITLE_LENGTH
import com.example.online.shop.model.TitleUtils.MIN_TITLE_LENGTH
import org.apache.commons.lang3.RandomStringUtils
import java.math.BigDecimal
import kotlin.random.Random

object ModelTestData {

    private val randomAuthorLength = (MIN_AUTHOR_LENGTH..MAX_AUTHOR_LENGTH).random()
    private val randomBuildingLength = (MIN_BUILDING_LENGTH..MAX_BUILDING_LENGTH).random()
    private val randomCityLength = (MIN_CITY_LENGTH..MAX_CITY_LENGTH).random()
    private val randomCountryLength = (MIN_COUNTRY_LENGTH..MAX_COUNTRY_LENGTH).random()
    private val randomDescriptionLength = (MIN_DESCRIPTION_LENGTH..MAX_DESCRIPTION_LENGTH).random()
    private val randomPriceValue = Random.nextDouble(MIN_PRICE_VALUE.toDouble(), MAX_PRICE_VALUE.toDouble()).toBigDecimal()
    private val randomQuantity = Random.nextInt(MIN_QUANTITY, MAX_QUANTITY)
    private val randomRating = Random.nextDouble(MIN_RATING.toDouble(), MAX_RATING.toDouble()).toBigDecimal()
    private val randomReviewTextLength = (MIN_REVIEW_TEXT_LENGTH..MAX_REVIEW_TEXT_LENGTH).random()
    private val randomSearchQueryLength = (MIN_SEARCH_QUERY_LENGTH..MAX_SEARCH_QUERY_LENGTH).random()
    private val randomStreetLength = (MIN_STREET_LENGTH..MAX_STREET_LENGTH).random()
    private val randomTitleLength = (MIN_TITLE_LENGTH..MAX_TITLE_LENGTH).random()

    private val randomString = RandomStringUtils.insecure()

    fun author(length: Int = randomAuthorLength): Author = Author(randomString.nextAlphabetic(length))
    fun building(length: Int = randomBuildingLength): Building = Building(randomString.nextAlphanumeric(length))
    fun city(length: Int = randomCityLength): City = City(randomString.nextAlphabetic(length))
    fun country(length: Int = randomCountryLength): Country = Country(randomString.nextAlphabetic(length))
    fun description(length: Int = randomDescriptionLength): Description = Description(randomString.nextAlphanumeric(length))
    fun priceValue(value: BigDecimal = randomPriceValue): PriceValue = PriceValue(value)
    fun quantity(value: Int = randomQuantity): Quantity = Quantity(value)
    fun rating(value: BigDecimal = randomRating): Rating = Rating(value)
    fun reviewText(length: Int = randomReviewTextLength): ReviewText = ReviewText(randomString.nextAlphabetic(length))
    fun searchQuery(length: Int = randomSearchQueryLength): SearchQuery = SearchQuery(randomString.nextAlphabetic(length))
    fun street(length: Int = randomStreetLength): Street = Street(randomString.nextAlphanumeric(length))
    fun title(length: Int = randomTitleLength): Title = Title(randomString.nextAlphabetic(length))

}