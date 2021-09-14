package com.example.kirozh.realmapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.realm.Realm

/**
 * @author Kirill Ozhigin on 03.09.2021
 */
open class AddContactFragment : Fragment() {
    private lateinit var mAddButton:Button
    private lateinit var mNameEt: EditText
    private lateinit var mSurnameEt: EditText
    private lateinit var mNumberEt: EditText
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)
        mNameEt = view.findViewById(R.id.addNameEt)
        mSurnameEt = view.findViewById(R.id.addSurnameEt)
        mNumberEt = view.findViewById(R.id.addNumberEt)
        mAddButton = view.findViewById(R.id.addBtn)

        mAddButton.setOnClickListener {

            if (mNameEt.text.toString().isNotEmpty() &&
                mSurnameEt.text.toString().isNotEmpty() &&
                mNumberEt.text.toString().isNotEmpty() ) {

                    addContactToDb()

                    replaceFragment()
            }
        }
        return view
    }

    private fun replaceFragment(){
        val fragment = ListContactFragment()
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun addContactToDb(){
        try{
            realm = Realm.getDefaultInstance()

            realm.beginTransaction()
            val currentIdNumber: Number? = realm.where(Contact::class.java).max("id")
            val nextIdNumber = currentIdNumber?.toInt()?.plus(1) ?: 1
            var addContact = realm.createObject(Contact::class.java, nextIdNumber)

            addContact.apply{

                name = mNameEt.text.toString()
                surname = mSurnameEt.text.toString()
                number = mNumberEt.text.toString()

            }

            realm.commitTransaction()
            Toast.makeText(activity, "contact added", Toast.LENGTH_LONG)
            Log.d("ADD_SUCCESS", "contact_added")
        }
        catch(e: Exception){
            Log.e("ADD_EXCEPTION", e.toString())
            Toast.makeText(activity, "adding failed", Toast.LENGTH_LONG)
        }
    }
}