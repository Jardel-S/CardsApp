package projeto.cards

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import projeto.cards.databinding.FragmentEmailPasswordBinding

class EmailPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentEmailPasswordBinding>(
            inflater,
            R.layout.fragment_email_password,
            container,
            false
        )

        val auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser

        if (currentuser != null) {
            findNavController()
                .navigate(R.id.action_EmailPasswordActivity_to_deckListFragment)
        }

        val email = binding.emailText
        val pass = binding.passText

        binding.singUpButton.setOnClickListener {
            if (email.text.isNotEmpty() && pass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController()
                                .navigate(R.id.action_EmailPasswordActivity_to_deckListFragment)
                        } else {
                            showAlert()
                        }
                    }
            }
        }

        binding.loginButton.setOnClickListener {
            if (email.text.isNotEmpty() && pass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController()
                                .navigate(R.id.action_EmailPasswordActivity_to_deckListFragment)
                        } else {
                            showAlert()
                        }
                    }
            }
        }

        return binding.root
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Check the data you entered.")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}