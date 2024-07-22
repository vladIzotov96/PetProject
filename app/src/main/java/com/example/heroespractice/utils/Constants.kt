package com.example.heroespractice.utils

/**Marvel API settings*/
const val MARVEL_API = "marvel_api"
const val BASE_MARVEL_URL = "https://gateway.marvel.com/v1/public/"
const val PUBLIC_KEY_MARVEL_API = "254190fa18c59f18160c44779e46329a"
const val PRIVATE_KEY_MARVEL_API = "1bbcbfd496b01e902a4baef6fac7cb2a5ccb2ebb"

/**Pixabay API settings*/
const val PIXABAY_API = "pixabay_api"
const val BASE_PIXABAY_URL = "https://cdn.pixabay.com/"

/**Interactor settings*/
const val USE_MOCK_DATA = false

/**Repository settings
 *
 * DATA_SOURCE_NETWORK_AND_DATABASE = true + DATA_SOURCE_NETWORK = any – usage of GeneralRepository
 * DATA_SOURCE_NETWORK_AND_DATABASE = false + DATA_SOURCE_NETWORK = true – usage of NetworkRepository
 * DATA_SOURCE_NETWORK_AND_DATABASE = false + DATA_SOURCE_NETWORK = false – doesn't work because deleted MarvelRepository implementation by DatabaseRepository
 *
 * */
const val DATA_SOURCE_NETWORK_AND_DATABASE = true
const val DATA_SOURCE_NETWORK = true

/**Other settings*/
const val PLACEHOLDER_VALUE_FOR_NUMBERS = 0