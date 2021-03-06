package com.BAMM.nutrimons.ui.login;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.BAMM.nutrimons.MainActivity;
import com.BAMM.nutrimons.MealPlan;
import com.BAMM.nutrimons.R;
import com.BAMM.nutrimons.database.AppDatabase;
import com.BAMM.nutrimons.database.Token;
import com.BAMM.nutrimons.database.User;

import java.util.List;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MealPlan.OnFragmentInteractionListener mListener;

    private LoginViewModel loginViewModel;
    private AppDatabase mDb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Login");
        }

        ((MainActivity)getActivity()).setDrawer_Locked();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText emailEditText = view.findViewById(R.id.email);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.loginButton);
        final Button registerButton = view.findViewById(R.id.registerButton);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
        mDb = AppDatabase.getInstance(getContext());

        try
        {
            Token t = mDb.tokenDao().getToken();
            if(t.userID > 0) //less than 0 means at least one user registered, but not logged in
            {
                Log.d("token found", "" + t.userID);
                Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_home);
            }
        }
        catch(NullPointerException e) //fresh database
        {
            Log.d("no token found", "nullptr exception");
            Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_registration);
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_registration);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

                User user = new User(emailEditText.getText().toString(),passwordEditText.getText().toString());
                List<User> myList = mDb.userDao().getAll();

                //loadingProgressBar.setVisibility(View.VISIBLE);
                if(checkEmail(myList,user))
                {
                    int userID = mDb.userDao().findByEmail(user.email).userID;
                    try
                    {
                        Token t = mDb.tokenDao().getToken();
                        t.userID = userID;
                        t.areTablesInitialized = true;
                        Log.d("userid", "" + t.userID);
                        mDb.tokenDao().insert(t); //set token
                    }
                    catch(NullPointerException e) //fresh database
                    {
                        Token t = new Token(userID);
                        t.areTablesInitialized = true;
                        mDb.tokenDao().insert(t);
                    }
                    List<Token> ts = mDb.tokenDao().getAll();
                    for(Token t0 : ts)
                        Log.d("tokens, userid", String.valueOf(t0.tokenID) + " " + String.valueOf(t0.userID));
                    Navigation.findNavController(view).navigate(R.id.action_nav_login_to_nav_home);
                }
                else {
                    emailEditText.setError("email or password does not match existing user");
                    emailEditText.requestFocus();
                }

            }
        });

        /*loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
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
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
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
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
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


    }
    private boolean checkEmail(List<User> list, User user)
    {
        for(User i : list)
        {
            if(i.email.equals(user.email) && i.password.equals(user.password))
            {
                return true;
            }
        }
        return false;
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

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MealPlan.OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }
}