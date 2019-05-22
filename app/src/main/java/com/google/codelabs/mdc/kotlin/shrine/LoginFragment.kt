package com.google.codelabs.mdc.kotlin.shrine

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.shr_login_fragment.*
import kotlinx.android.synthetic.main.shr_login_fragment.view.*
import android.app.Activity



/** Código levemente modificado para incluir o Firebase à primeira atividade de login do Material.io Kotlin
 * Fragment representing the login screen for Shrine.
 * Código original do google codelabs - basic material(kotlin)
 */
class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var mActivity: Activity

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        //guardando o valor da activity, para evitar valores nulos futuramente
        mActivity = activity;
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.shr_login_fragment, container, false)
        // Snippet from "Navigate to the next Fragment" section goes here.

        view.next_button.setOnClickListener({
            //verificação se é um email valido, e criação da conta do usuári
            if(!isPasswordValid(password_edit_text.text!!)){
                password_text_input.error = getString(R.string.shr_error_password)
            } else if(!isEmailValid(email_edit_text.text)){
                email_text_input.error =getString(R.string.shr_error_email)
            }else{
                password_text_input.error = null
                //navegação para a próxima tela
                val email:String = email_edit_text.text.toString()
                    //autenticação utilizando email e senha, boa parte do código é o mesmo gerado pelo firebase
                    auth.signInWithEmailAndPassword(email, password_edit_text.text.toString())
                            .addOnCompleteListener(mActivity) { task ->
                                if (task.isSuccessful) {
                                    // Sucesso no login, guarda usuário e passa para a próxima tela
                                    Log.d(TAG, "signInWithEmail:success")
                                    val user = auth.currentUser
                                    (activity as NavigationHost).navigateTo(ProductGridFragment(), false)
                                } else {
                                    // caso de falha na tentativa de login
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(mActivity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                }

                            }

                }

        })


        view.register_button.setOnClickListener({
            //verifica senha e email, para saber se são válidos
            if(!isPasswordValid(password_edit_text.text!!)){
                password_text_input.error = getString(R.string.shr_error_password)
            }else if(!isEmailValid(email_edit_text.text)){
                email_text_input.error =getString(R.string.shr_error_email)
            }
            else{
                password_text_input.error = null
                email_text_input.error=null
                    //criação de conta utilizando email e senha, boa parte do código é o mesmo gerado pelo firebase
                    auth.createUserWithEmailAndPassword(email_edit_text.text.toString(), password_edit_text.text.toString())
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    // Sucesso na criação do usuário, passa para a próxima tela
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser
                                    (activity as NavigationHost).navigateTo(ProductGridFragment(), false)
                                } else {
                                    // Falha na criação, avia o usuário
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(mActivity, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                }
                            }
                }
        })

        //onClick já existente do código, apenas modifiquei a função chamada
        view.password_edit_text.setOnKeyListener({ _, _, _ ->
            if(isPasswordValid(password_edit_text.text!!)) {
                password_text_input.error = null
            }
            false
        })
        //baseando-se na anterior, fiz esta e criei o
        view.email_edit_text.setOnKeyListener({ _, _, _ ->
            if(isEmailValid(email_edit_text.text!!)){
                email_text_input.error = null
            }
            false
        })

        auth = FirebaseAuth.getInstance()
        /* opicional, dependendo do caso utilizo implemento o onClick no fragment e utilizo o when(view.id) para identificar qual view foi clicada*/


        return view
    }

    fun isEmailValid(emailTxt: Editable?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()
    }

    override fun onStart(){
        super.onStart()
        //login automático
        val currentUser = auth.currentUser
        if (currentUser != null) {
            (activity as NavigationHost).navigateTo(ProductGridFragment(), false)
        }
    }




    //modificação do código para se verificar também se há números na senha
    private fun isPasswordValid(text: Editable?): Boolean{
        return text!=null&& text.length>=8 && containsDigit(text.toString());
    }

    //função para verificar se a string contém números
    fun containsDigit(text : String?): Boolean{
        var containsDigit=false
        if(text ==null || text.isEmpty()){
            return containsDigit
        } else{
            for (c in text){
                containsDigit = Character.isDigit(c)
                if(containsDigit==true) {
                    break
                }
            }
            return containsDigit
        }
    }

}
