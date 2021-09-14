package com.example.kirozh.realmapplication

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmResults

/**
 * @author Kirill Ozhigin on 03.09.2021
 */
class ListContactFragment : androidx.fragment.app.Fragment(){
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var contacts: ArrayList<Contact>
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        mFloatingActionButton = view.findViewById(R.id.floatingActionButton)

        mFloatingActionButton.setOnClickListener {
            val addFragment = AddContactFragment()
            replaceFragment(addFragment)
        }
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        realm = Realm.getDefaultInstance()
        getAllNodes()

        return view
    }

    private inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view){
        val mNameTv: TextView = itemView.findViewById(R.id.contact_name)
        val mSurnameTv: TextView = itemView.findViewById(R.id.contact_surname)
        val mNumberTv: TextView = itemView.findViewById(R.id.contact_number)

    }

    private inner class ContactAdapter(private val contactList:RealmResults<Contact>)
        :RecyclerView.Adapter<ContactHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            val view = layoutInflater.inflate(R.layout.contact_row, parent, false)
            return ContactHolder(view)
        }

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            val contact = contactList[position]
            holder.apply{
                mNameTv.text = contact?.name
                mSurnameTv.text = contact?.surname
                mNumberTv.text = contact?.number
            }
            holder.itemView.setOnClickListener {
                Log.d("POSITION_CLICKED", position.toString())
                val updateFragment = UpdateContactFragment.newInstance(position)
                replaceFragment(updateFragment)
            }
        }

        override fun getItemCount(): Int {
            return contactList.size
        }

    }

    private fun getAllNodes(){

        contacts = ArrayList()
        val results:RealmResults<Contact> = realm.where(Contact::class.java).findAll()
        Log.d("RESULTS_LENGTH", results.size.toString())
        mRecyclerView.adapter = ContactAdapter(results)
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }


}