package uz.ibrohim.amwayuz.admin.products.productsAdd

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.admin.products.ProductsModel
import uz.ibrohim.amwayuz.databinding.FragmentProductsAddBinding
import uz.ibrohim.amwayuz.utils.errorToast
import uz.ibrohim.amwayuz.utils.progressInterface
import uz.ibrohim.amwayuz.utils.requestStoragePermission
import uz.ibrohim.amwayuz.utils.successToast
import uz.ibrohim.amwayuz.utils.warningToast
import java.io.File
import java.util.concurrent.TimeUnit

class ProductsAddFragment : Fragment() {

    private var _binding: FragmentProductsAddBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()
    private var filePath = ""
    private val REQUEST_PERMISSIONS = 1
    private val viewModel: ProductsModel by activityViewModels()
    private var bool = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsAddBinding.inflate(inflater, container, false)
// *f6YdaL2*
        if (viewModel.name.isNotEmpty() && viewModel.bool){
            bool = true
            viewModel.apply {
                binding.productsName.setText(name)
                binding.barCode.setText(code)
                binding.bodyPrice.setText(bodyPrice)
                binding.price.setText(price)
                binding.amount.setText(amount)
                Glide.with(requireContext()).load(image).into(binding.image)
            }
        }

        binding.apply {
            progressInterface(circularProgress)

            image.setOnClickListener {
                requestStoragePermission(REQUEST_PERMISSIONS, imageProfile)
            }

            productBtn.setOnClickListener {
                if (bool){
                    checkData()
                }else if (filePath.isNotEmpty()) {
                    checkData()
                } else {
                    warningToast(getString(R.string.upload_image))
                }
            }
        }
        return binding.root
    }

    private val imageProfile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri: Uri = data?.data!!
                    filePath = FileUriUtils.getRealPath(requireActivity(), uri).toString()
                    binding.apply {
                        image.setImageURI(Uri.fromFile(File(filePath)))
                    }

                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(), ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun checkData() {
        binding.apply {
            val name = productsName.text.toString()
            val code = barCode.text.toString()
            val bodyPrice = bodyPrice.text.toString()
            val price = price.text.toString()
            val amount = amount.text.toString()
            val key: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            var parseDate = key.toString()

            if (name.isEmpty() || code.isEmpty() || bodyPrice.isEmpty() || price.isEmpty() || amount.isEmpty()) {
                warningToast(getString(R.string.fill_them_all))
            } else {
                productBtn.isVisible = false
                progress.isVisible = true

                if (filePath.isEmpty()){
                    if (bool){
                        parseDate = viewModel.key
                        val imageUrls = viewModel.image
                        val dataMap = hashMapOf(
                            "name" to name,
                            "code" to code,
                            "bodyPrice" to bodyPrice,
                            "price" to price,
                            "amount" to amount,
                            "image" to imageUrls,
                            "key" to parseDate
                        )

                        db.collection("products").document(parseDate).set(dataMap)
                            .addOnSuccessListener {
                                findNavController().popBackStack()
                            }
                            .addOnFailureListener {
                                errorToast(getString(R.string.login_error))
                                progress.isVisible = false
                                productBtn.isVisible = true
                            }
                    }
                }else{
                    if (bool){
                        parseDate = viewModel.key
                    }
                    val storageRef = FirebaseStorage.getInstance().reference
                    val file: Uri = Uri.fromFile(File(filePath))
                    val imageRef = storageRef.child("images/$key")
                    val uploadTask = imageRef.putFile(file)
                    uploadTask.addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        Log.d("rasimyuklanmoqda", "Upload is $progress% done")
                    }
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { uri -> // URL of the uploaded image
                                val imageUrl = uri.toString()

                                Log.d("rasimyuklanmoqda", "Image uploaded successfully. Image URL: $imageUrl")
                                val dataMap = hashMapOf(
                                    "name" to name,
                                    "code" to code,
                                    "bodyPrice" to bodyPrice,
                                    "price" to price,
                                    "amount" to amount,
                                    "image" to imageUrl,
                                    "key" to parseDate
                                )

                                db.collection("products").document(parseDate).set(dataMap)
                                    .addOnSuccessListener {
                                        findNavController().popBackStack()
                                    }
                                    .addOnFailureListener {
                                        errorToast(getString(R.string.login_error))
                                        progress.isVisible = false
                                        productBtn.isVisible = true
                                    }
                            }
                        }
                        .addOnFailureListener { e -> Log.e("rasimyuklanmoqda", "Image upload failed", e)
                            productBtn.isVisible = true
                            progress.isVisible = false
                        }
                }

            }
        }
    }
}