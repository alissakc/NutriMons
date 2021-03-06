package com.BAMM.nutrimons.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.BAMM.nutrimons.MainActivity;
import com.BAMM.nutrimons.NutrientTablesApi;
import com.BAMM.nutrimons.R;
import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.TamagotchiPet;
import com.BAMM.nutrimons.database.Token;
import com.BAMM.nutrimons.database.User;

import java.util.List;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    /**/private LoginViewModel loginViewModel;
    private AppDatabase mDb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        ((MainActivity)getActivity()).setDrawer_Locked();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);*/

        final EditText emailEditText = view.findViewById(R.id.email);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button registerButton = view.findViewById(R.id.register);
        final Button loginButton = view.findViewById(R.id.loginButton);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        mDb = AppDatabase.getInstance(getContext());

        //mDb.userDao().nukeTable();



        /*loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    System.out.println("LOSER");

                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    System.out.println("SUCCESS ayaya");
                    Navigation.findNavController(view).navigate(R.id.action_nav_registration_to_nav_login);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };*/
        /*usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Navigation.findNavController(view).navigate(R.id.action_nav_registration_to_nav_login);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                List<User> myList = mDb.userDao().getAll();
                User user = new User(emailEditText.getText().toString(), passwordEditText.getText().toString());

                int emailValidation = emailValidate(myList, user);
                int passwordValidation = passwordValidate(user);
                if(emailValidation != 0){ //0 is valid response
                    switch(emailValidation)
                    {
                        case 1:
                        {
                            emailEditText.setError("Email is not entered");
                            break;
                        }
                        case 2:
                        {
                            emailEditText.setError("Email is invalid");
                            break;
                        }
                        case 3:
                        {
                            emailEditText.setError("Email is already registered");
                            break;
                        }
                        default:
                        {
                            emailEditText.setError("Unknown error");
                        }
                    }
                    emailEditText.requestFocus();
                }
                else if(passwordValidation != 0){
                    switch(passwordValidation)
                    {
                        case 1:
                        {
                            passwordEditText.setError("Password must be at least 8 characters");
                            break;
                        }
                        case 2:
                        {
                            passwordEditText.setError("Password must be at most 24 characters");
                            break;
                        }
                        case 3:
                        {
                            passwordEditText.setError("Password must have at least one lowercase " +
                                    "letter, one uppercase letter, a number, and a special character");
                            break;
                        }
                        case 4:
                        {
                            passwordEditText.setError("Password cannot have more than 2 repeating characters");
                            break;
                        }
                        default:
                        {
                            passwordEditText.setError("Unknown error");
                        }
                    }
                    passwordEditText.requestFocus();
                }
                else{
                    System.out.println("INSERTING INTO DATABASE");
                    mDb.userDao().insert(user);

                    User u2 = mDb.userDao().findByEmail(user.email);
                    u2.profileFocus = "Maintain Weight";
                    u2.age = "25";
                    u2.sex = "Male";
                    u2.weight = "160";
                    u2.height = "70";
                    u2.activityLevel = "Sedentary";
                    mDb.userDao().insert(u2);
                    Log.d("user", u2.toString());

                    NutrientTablesApi nta = new NutrientTablesApi(mDb);
                    nta.updateUserNutrients(u2.userID);

                    TamagotchiPet tama = new TamagotchiPet();
                    tama.userId = u2.userID;
                    mDb.tamagotchiDao().insert(tama);

                    Token t = mDb.tokenDao().getToken();
                    if(t == null)
                        t = new Token(-1);
                    t.areTablesInitialized = true;
                    t.isShopInitialized = true;
                    mDb.tokenDao().insert(t);
                    
                    Navigation.findNavController(view).navigate(R.id.action_nav_registration_to_nav_login);
                }
            }
        });
    }
    //returns 1 for null email, 2 for invalid email, 3 for existing email, else 0
    private int emailValidate(List<User> list,User user) {
        if(user.email.length() == 0)
        {
            return 1;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user.email).matches())
        {
            return 2;
        }
        for(User i:list)
        {
            //'.' does not matter, case does not matter
            if(i.email.toLowerCase().replace(".", "").equals(user.email.toLowerCase().replace(".", "")))
            {
                return 3;
            }
        }
        return 0;
    }
    private int passwordValidate(User user) {
        if(user.password.length() < 8)
        {
            return 1;
        }
        if(user.password.length() > 24)
        {
            return 2;
        }
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$");
        if(!PASSWORD_PATTERN.matcher(user.password).matches())
        {
            return 3;
        }
        String[] pass = user.password.split("");
        for(int i = 2; i < user.password.length(); ++i)
        {
            if(pass[i].equals(pass[i - 1]) && pass[i].equals(pass[i - 2]))
                return 4;
        }
        return 0;
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)getActivity()).setDrawer_UnLocked();
    }
}