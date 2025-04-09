package uz.ibrohim.amwayuz.admin.employee

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.admin.employee.request_response.RequestEmployee
import uz.ibrohim.amwayuz.admin.employee.services.EmployeeViewModel
import uz.ibrohim.amwayuz.databinding.EmployeeDialogBinding
import uz.ibrohim.amwayuz.databinding.FragmentEmployeeBinding
import uz.ibrohim.amwayuz.retrofit.ApiClient
import uz.ibrohim.amwayuz.retrofit.NetworkHelper
import uz.ibrohim.amwayuz.retrofit.Resource
import uz.ibrohim.amwayuz.utils.errorToast
import uz.ibrohim.amwayuz.utils.progressInterface
import uz.ibrohim.amwayuz.utils.successToast
import uz.ibrohim.amwayuz.utils.warningToast

class EmployeeFragment : Fragment() {

    private var _binding: FragmentEmployeeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userArrayList : ArrayList<EmployeeItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        userArrayList = arrayListOf()
        getUserData()

        binding.employeeAdd.setOnClickListener {
            gtDialog()
        }

        return binding.root
    }

    private fun gtDialog() {
        val dialog = Dialog(requireContext())
        val binding: EmployeeDialogBinding = EmployeeDialogBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window!!.setLayout(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.apply {
            progressInterface(employeeAddCircularProgress)

            employeeAddBtn.setOnClickListener {
                val name = name.text.toString()
                val number = number.text.toString()
                val password = password.text.toString()
                val password2 = password2.text.toString()
                val email = "$number@amway.uz"
                if (name.isEmpty() || number.isEmpty() || password.isEmpty() || password2 != password){
                    warningToast(getString(R.string.fill_them_all))
                }else{
                    employeeAddBtn.isVisible = false
                    employeeAddProgress.isVisible = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val currentUserID = auth.currentUser!!.uid
                                val dataHas = HashMap<String, String>()
                                dataHas["name"] = name
                                dataHas["number"] = number
                                dataHas["status"] = "false"
                                dataHas["password"] = password
                                dataHas["uid"] = currentUserID
                                db.child("users").child(currentUserID).setValue(dataHas)
                                    .addOnCompleteListener {
                                        successToast(getString(R.string.success))
                                        userArrayList.clear()
                                        getUserData()
                                        dialog.dismiss()

                                    }
                            }else{
                                employeeAddProgress.isVisible = false
                                employeeAddBtn.isVisible = true
                                errorToast(getString(R.string.this_number_was_previously_registered))
                            }
                        }
                }
            }

        }
    }

    private fun getUserData() {
        db.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user:EmployeeItem? = userSnapshot.getValue(EmployeeItem::class.java)
                        userArrayList.add(user!!)
                    }
                    binding.employeeRv.adapter = EmployeeAdapter(userArrayList, object :EmployeeAdapter.OnItemClickListener{
                        override fun onItemClick(student: EmployeeItem, position: Int) {
                            db.child("users").child(student.uid!!).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "O'chirildi", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(requireContext(), "Xatolik $exception", Toast.LENGTH_SHORT).show()
                                }
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}