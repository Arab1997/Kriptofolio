/*
 * Copyright 2018-2020 Andrius Baruckis www.baruckis.com | kriptofolio.app
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baruckis.kriptofolio.utilities

/**
 * Static methods used to calculate financial values.
 */

// Get total amount value in fiat currency based on its price.
fun getAmountFiatCounted(amount: Double?, priceFiat: Double): Double? =
        amount?.let { it -> it * priceFiat }

// Get total amount value change during last 24 hours in fiat currency.
fun getAmountFiatChange24hCounted(amountFiat: Double?, pricePercentChange24h: Double): Double? =
        amountFiat?.let { it -> it * (pricePercentChange24h / 100) }