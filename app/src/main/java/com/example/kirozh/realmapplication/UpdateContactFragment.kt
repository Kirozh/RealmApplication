package com.example.kirozh.realmapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm

private const val ARG_CONTACT_ID = "contact_id"

class UpdateContactFragment : Fragment() {
    private lateinit var mNameEt: EditText
    private lateinit var mSurnameEt: EditText
    private lateinit var mNumberEt: EditText
    private lateinit var mUpdateButton: Button
    private lateinit var realm: Realm
    private var contactId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactId = arguments?.getSerializable(ARG_CONTACT_ID) as Int
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_contact, container, false)
        mNameEt = view.findViewById(R.id.updateNameEt)
        mSurnameEt = view.findViewById(R.id.updateSurnameEt)
        mNumberEt = view.findViewById(R.id.updateNumberEt)
        mUpdateButton = view.findViewById(R.id.updateBtn)

        realm = Realm.getDefaultInstance()

        fillEditText(contactId)

        mUpdateButton.setOnClickListener {
            if (mNameEt.text.toString().isNotEmpty()
                && mSurnameEt.text.toString().isNotEmpty()
                && mNumberEt.text.toString().isNotEmpty()){

                updateDb(contactId)

                replaceFragment()
            }
        }
        return view
    }

    @SuppressLint("UseValueOf")
    private fun updateDb(contactId:Int?){
        try{


            realm.beginTransaction()

            val contactToUpdate: Contact? = realm.where(Contact::class.java)
                .equalTo("id", contactId?.plus(1)).findFirst()

            Log.d("contactToUpdate", contactToUpdate?.name.toString())
            contactToUpdate?.apply{

                name = mNameEt.text.toString()
                surname = mSurnameEt.text.toString()
                number = mNumberEt.text.toString()

            }
            realm.insertOrUpdate(contactToUpdate)
            realm.commitTransaction()

            Toast.makeText(activity, "contact updated", Toast.LENGTH_LONG).show()
            Log.d("ADD_SUCCESS", "contact_updated")
        }
        catch(e: Exception){
            Log.e("ADD_EXCEPTION", e.toString())
            Toast.makeText(activity, "updating failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun fillEditText(contactId: Int?){
        val contactChosen: Contact? = realm.where(Contact::class.java)
            .equalTo("id", (contactId?.plus(1))).findFirst()

        mNameEt.setText( contactChosen?.name)
        mSurnameEt.setText( contactChosen?.surname)
        mNumberEt.setText(contactChosen?.number)
    }

    private fun replaceFragment(){
        val fragment = ListContactFragment()
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    companion object{

        fun newInstance( contactId: Int): UpdateContactFragment{
            val args = Bundle().apply{
                putSerializable(ARG_CONTACT_ID, contactId)
            }
            return UpdateContactFragment().apply{
                arguments = args
            }
        }
    }

}