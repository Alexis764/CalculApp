package com.example.calculapp.domain.credit

class CalculateCredit {

    //Constants
    companion object {
        const val ANNUAL_INTEREST_RATE = 35 //35% EA
        const val DAYS_YEAR = 365 //365 days
        const val STEP_SIZE_MONEY = 10000 //Steps that the user can take to choose the credit amount
        const val ENDORSEMENT_FOR_STEP_SIZE_MONEY = 2356.2 //Endorsement value for each step size money (10000 -> 2356)
        const val ELECTRONIC_SIGNATURE = 130500
        const val DISCOUNT_MAX = 123250 //Maximum discount that a person can access (5 days)
        const val LESS_DISCOUNT_FOR_MORE_DAYS = 1450 //Lower discount per day added
    }

    //Function to calculate credit data
    fun calculateDataCredit(valueMoney: Int, valueDays: Int): CalculatedCreditModel {
        val dailyInterestRate = (ANNUAL_INTEREST_RATE.toDouble() / DAYS_YEAR.toDouble()) / 100

        val interest = (valueMoney * dailyInterestRate * valueDays).toInt()
        val endorsement = ((valueMoney / STEP_SIZE_MONEY) * ENDORSEMENT_FOR_STEP_SIZE_MONEY).toInt()

        val endorsementDiscount = when {
            valueDays > 60 -> 0
            else -> ((valueMoney / STEP_SIZE_MONEY) * 1011.5).toInt()
        }

        val discount = when {
            valueDays > 60 -> 0
            valueDays == 5 -> DISCOUNT_MAX
            else -> (DISCOUNT_MAX - ((valueDays - 5) * LESS_DISCOUNT_FOR_MORE_DAYS))
        }

        val total =
            (valueMoney + interest + endorsement - endorsementDiscount + ELECTRONIC_SIGNATURE - discount)

        return CalculatedCreditModel(
            amountRequested = valueMoney,
            daysRequested = valueDays,
            interest = interest,
            endorsement = endorsement,
            endorsementDiscount = endorsementDiscount,
            electronicSignature = ELECTRONIC_SIGNATURE,
            discount = discount,
            total = total
        )
    }

}