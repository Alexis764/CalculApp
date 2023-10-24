package com.example.calculapp.data.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.calculapp.data.sqlite.model.CreditModel
import com.example.calculapp.domain.credit.CalculateCredit
import com.example.calculapp.domain.credit.CreditState.*
import com.example.calculapp.domain.user.UserModel
import com.example.calculapp.ui.credit.CreditRegisterMessageResponse
import com.example.calculapp.ui.credit.CreditRegisterMessageResponse.*
import com.example.calculapp.ui.register.UserRegisterMessageResponse
import com.example.calculapp.ui.register.UserRegisterMessageResponse.ErrorEmail
import com.example.calculapp.ui.register.UserRegisterMessageResponse.ErrorIdentification
import com.example.calculapp.ui.register.UserRegisterMessageResponse.Registered
import java.time.format.DateTimeFormatter

class UserCreditsDataBase(context: Context): SQLiteOpenHelper(context, "UserCredits", null, 1) {

    //Constants
    companion object {
        //Database tables
        const val USER = "user"
        const val CREDIT = "credit"

        //User table columns
        const val NAME = "userName"
        const val LASTNAME = "userLastName"
        const val DOCUMENT_TYPE = "userDocumentType"
        const val IDENTIFICATION_NUMBER = "userIdentityNumber"
        const val EMAIL = "userEmail"
        const val PHONE_NUMBER = "userPhoneNumber"
        const val PASSWORD = "userPassword"

        //Credit table columns
        const val CREDIT_ID = "creditId"
        const val CREDIT_USER_IDENTIFICATION_NUMBER = "creditUserIdentificationNumber"
        const val AMOUNT_REQUESTED = "creditAmountRequested"
        const val DAYS_REQUESTED = "creditDaysRequested"
        const val CREDIT_DATE = "creditDate"
        const val TOTAL = "creditTotal"
        const val STATE = "creditState"
    }

    //Val and var
    private val calculatedCredit = CalculateCredit()


    //Function to create database tables
    override fun onCreate(db: SQLiteDatabase?) {
        val sqlUserTable = "CREATE TABLE $USER (" +
                "$NAME VARCHAR(15), " +
                "$LASTNAME VARCHAR(15), " +
                "$DOCUMENT_TYPE VARCHAR(30), " +
                "$IDENTIFICATION_NUMBER INTEGER PRIMARY KEY, " +
                "$EMAIL VARCHAR(200), " +
                "$PHONE_NUMBER INTEGER, " +
                "$PASSWORD VARCHAR(200))"
        db!!.execSQL(sqlUserTable)

        val sqlCreditTable = "CREATE TABLE $CREDIT (" +
                "$CREDIT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CREDIT_USER_IDENTIFICATION_NUMBER INTEGER, " +
                "$AMOUNT_REQUESTED INTEGER, " +
                "$DAYS_REQUESTED INTEGER, " +
                "$CREDIT_DATE VARCHAR(13), " +
                "$TOTAL INTEGER, " +
                "$STATE VARCHAR(15))"
        db.execSQL(sqlCreditTable)
    }


    //Function to upgrade the database
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val sqlDropUserTable = "DROP TABLE IF EXISTS $USER"
        db!!.execSQL(sqlDropUserTable)

        val sqlDropCreditTable = "DROP TABLE IF EXISTS $CREDIT"
        db.execSQL(sqlDropCreditTable)

        onCreate(db)
    }


    //FUNCTIONS TO USER TABLE
    //Function to insert a new user into the database
    fun insertUser(userModel: UserModel): UserRegisterMessageResponse {
        val data = ContentValues()
        data.put(NAME, userModel.name)
        data.put(LASTNAME, userModel.lastName)
        data.put(DOCUMENT_TYPE, userModel.documentType)
        data.put(IDENTIFICATION_NUMBER, userModel.identificationNumber)
        data.put(EMAIL, userModel.email)
        data.put(PHONE_NUMBER, userModel.phoneNumber)
        data.put(PASSWORD, userModel.password)

        return when {
            userIdentificationExist(userModel.identificationNumber) -> ErrorIdentification
            userEmailExist(userModel.email) -> ErrorEmail
            else -> {
                val db = this.writableDatabase
                db.insert(USER, null, data)
                Registered
            }
        }
    }

    //Function to verify user identification number
    private fun userIdentificationExist(identificationNumber: Long): Boolean {
        val sql = "SELECT * FROM $USER WHERE $IDENTIFICATION_NUMBER = '$identificationNumber'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        val identificationExists = cursor.moveToFirst()
        cursor.close()
        return identificationExists
    }

    //Function to verify user email
    private fun userEmailExist(email: String): Boolean {
        val sql = "SELECT * FROM $USER WHERE $EMAIL = '$email'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        val emailExist = cursor.moveToFirst()
        cursor.close()
        return emailExist
    }


    //Function to search and return user information
    @SuppressLint("Range")
    fun getUserInformation(userIdentificationNumber: Int): UserModel? {
        val sql = "SELECT * FROM $USER WHERE $IDENTIFICATION_NUMBER = $userIdentificationNumber"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        var userModel: UserModel

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(LASTNAME))
                val documentType = cursor.getString(cursor.getColumnIndex(DOCUMENT_TYPE))
                val identificationNumber = cursor.getLong(cursor.getColumnIndex(IDENTIFICATION_NUMBER))
                val email = cursor.getString(cursor.getColumnIndex(EMAIL))
                val phoneNumber = cursor.getLong(cursor.getColumnIndex(PHONE_NUMBER))
                val password = cursor.getString(cursor.getColumnIndex(PASSWORD))

                userModel = UserModel(name, lastName, documentType, identificationNumber, email, phoneNumber, password)
            } while (cursor.moveToNext())

        } else {
            return null
        }

        cursor.close()
        return userModel
    }


    //Function to user login
    @SuppressLint("Range")
    fun userLogin(userEmail: String, userPassword: String): Long? {
        val sql = "SELECT * FROM $USER WHERE $EMAIL = '$userEmail' AND $PASSWORD = '$userPassword'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(sql, null)
        var userIdentificationNumber: Long

        if (cursor.moveToFirst()) {
            do {
                userIdentificationNumber = cursor.getLong(cursor.getColumnIndex(IDENTIFICATION_NUMBER))
            } while (cursor.moveToNext())

        } else {
            return null
        }

        cursor.close()
        return userIdentificationNumber
    }



    //FUNCTIONS TO CREDIT TABLE
    //Function to insert credit for user
    fun insertCredit(valueMoney: Int, valueDays: Int, userIdentificationNumber: Long): CreditRegisterMessageResponse {
        val calculatedCreditModel = calculatedCredit.calculateDataCredit(valueMoney, valueDays)
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val data = ContentValues()

        data.put(CREDIT_USER_IDENTIFICATION_NUMBER, userIdentificationNumber)
        data.put(AMOUNT_REQUESTED, calculatedCreditModel.amountRequested)
        data.put(DAYS_REQUESTED, calculatedCreditModel.daysRequested)
        data.put(CREDIT_DATE, calculatedCreditModel.creditDate.format(dateFormat))
        data.put(TOTAL, calculatedCreditModel.total)
        data.put(STATE, Proceso.toString())

        return when{
            userCurrentCredit(userIdentificationNumber) -> CurrentCredit
            userCreditInProcess(userIdentificationNumber) -> RequestInProcess
            else -> {
                val db = this.writableDatabase
                db.insert(CREDIT, null, data)
                CreditRegistered
            }
        }
    }

    //Function to verify if user has a current credit
    private fun userCurrentCredit(userIdentificationNumber: Long): Boolean {
        val creditModelList = allUserCredit(userIdentificationNumber)
        if (creditModelList != null) {
            creditModelList.forEach { creditModel ->
                if (creditModel.state == Actual.toString()) {
                    return true
                }
            }

        } else {
            return false
        }

        return false
    }

    //Function to verify if user has a credit in process
    private fun userCreditInProcess(userIdentificationNumber: Long): Boolean {
        val creditModelList = allUserCredit(userIdentificationNumber)
        if (creditModelList != null) {
            creditModelList.forEach { creditModel ->
                if (creditModel.state == Proceso.toString()) {
                    return true
                }
            }

        } else {
            return false
        }

        return false
    }


    //Function to search all credits for a user
    @SuppressLint("Range")
    fun allUserCredit(userIdentificationNumber: Long): List<CreditModel>? {
        val sql = "SELECT * FROM $CREDIT WHERE $CREDIT_USER_IDENTIFICATION_NUMBER = $userIdentificationNumber"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        val creditModelList = mutableListOf<CreditModel>()

        if (cursor.moveToFirst()) {
            do {
                creditModelList.add(
                    CreditModel(
                        id =  cursor.getInt(cursor.getColumnIndex(CREDIT_ID)),
                        userIdentificationNumber = cursor.getLong(cursor.getColumnIndex(CREDIT_USER_IDENTIFICATION_NUMBER)),
                        amountRequested = cursor.getInt(cursor.getColumnIndex(AMOUNT_REQUESTED)),
                        daysRequested = cursor.getInt(cursor.getColumnIndex(DAYS_REQUESTED)),
                        creditDate = cursor.getString(cursor.getColumnIndex(CREDIT_DATE)),
                        total = cursor.getInt(cursor.getColumnIndex(TOTAL)),
                        state = cursor.getString(cursor.getColumnIndex(STATE))
                    )
                )
            } while (cursor.moveToNext())
        } else {
            return null
        }

        cursor.close()
        return creditModelList
    }


}