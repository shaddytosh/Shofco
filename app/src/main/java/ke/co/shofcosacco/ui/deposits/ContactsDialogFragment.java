package ke.co.shofcosacco.ui.deposits;

import static android.app.Activity.RESULT_OK;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.L;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.ContactsBinding;
import co.ke.shofcosacco.databinding.DialogOtpConfirmationBinding;
import ke.co.shofcosacco.app.models.Contacts;
import ke.co.shofcosacco.app.models.SourceAccount;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.SmsBroadcastReceiver;


public class ContactsDialogFragment extends DialogFragment {

    private static final int REQ_USER_CONSENT = 1500;

    private ContactsBinding binding;
    private List<Contacts> contactsList;
    private List<Contacts> originalContactsList;
    private  ArrayAdapter<Contacts> contactsArrayAdapter;

    public ContactsDialogFragment() {
        // Required empty public constructor
    }
    public static ContactsDialogFragment newInstance() {
        ContactsDialogFragment dialogFragment = new ContactsDialogFragment();
        return dialogFragment;

    }

   public static void show (FragmentManager fragmentManager){
        newInstance().show(fragmentManager,"paymentTrueDialogFragment");
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.RoundedCornersDialogFull);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ContactsBinding.inflate(getLayoutInflater());


        binding.ivClose.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // Provide additional rationale if needed and then request the permission
                // For example, you can show a dialog explaining why the permission is needed
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
                } else {
                    // Permission has already been granted, proceed with reading contacts
                    readContacts();
                }            } else {
                // No explanation needed, request the permission
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
            }
        } else {
            // Permission has already been granted, proceed with reading contacts
            readContacts();
        }


        // Set a click listener for the ListView items
        binding.ListView.setOnItemClickListener((parent, view, position, id) -> {
            // Ensure the position is within the bounds of the filtered contactsList
            if (position < contactsArrayAdapter.getCount()) {
                Contacts selectedContact = contactsArrayAdapter.getItem(position);
                if (selectedContact != null) {
                    Intent result = new Intent();
                    result.putExtra("phoneNumber", selectedContact.phone);
                    if (getParentFragment() != null) {
                        getParentFragment().onActivityResult(3000, RESULT_OK, result);
                    }
                    dismiss();
                }
            }
        });


        setupSearch();

        return binding.getRoot();

    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Filter the list based on the search input
                contactsArrayAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this example
            }
        });
    }


    private void readContacts() {
        contactsList = new ArrayList<>();

        Cursor phones = requireContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (phones != null) {
            int nameIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int phoneTypeIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

            while (phones.moveToNext()) {
                String name = "";
                String phoneNumber = "";
                String phoneNumberWithSpaces = "";
                int phoneType = -1;

                // Check if the column index is valid before retrieving data
                if (nameIndex != -1) {
                    name = phones.getString(nameIndex);
                }

                if (phoneNumberIndex != -1) {
                    phoneNumberWithSpaces = phones.getString(phoneNumberIndex);
                }

                if (phoneTypeIndex != -1) {
                    phoneType = phones.getInt(phoneTypeIndex);
                }

                // Filter only mobile phone numbers
//                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    phoneNumber = phoneNumberWithSpaces.replaceAll("\\s", "");

                    // Check for duplicates before adding to the list
                    if (!contactExists(contactsList, name, phoneNumber)) {
                        contactsList.add(new Contacts(name, phoneNumber));
//                    }
                }
            }

            phones.close();

            // Save the original list to handle duplicates
            originalContactsList = new ArrayList<>(contactsList);

            contactsArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, contactsList);
            binding.ListView.setAdapter(contactsArrayAdapter);
        }
    }

    // Helper method to check for duplicates
    private boolean contactExists(List<Contacts> contactsList, String name, String phoneNumber) {
        for (Contacts contact : contactsList) {
            if (contact.name.equals(name) && contact.phone.equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                // Handle permission denied
                Toast.makeText(requireContext(), "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}