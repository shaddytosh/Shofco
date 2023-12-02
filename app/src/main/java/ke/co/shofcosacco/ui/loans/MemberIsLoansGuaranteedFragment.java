package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentMemberIsLoanQuaranteedBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.PdfUtils;
import ke.co.shofcosacco.ui.auth.AuthViewModel;

public class MemberIsLoansGuaranteedFragment extends BaseFragment {

    private FragmentMemberIsLoanQuaranteedBinding binding;
    private AuthViewModel authViewModel;

    private String base64Pdf;

    public MemberIsLoansGuaranteedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMemberIsLoanQuaranteedBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });
        binding.btnGoBack.setOnClickListener(v -> {
            navigateUp();
        });


        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            memberIsLoanGuaranteed();
            binding.swipeToRefresh.setRefreshing(false);
        });

        memberIsLoanGuaranteed();

        binding.tvDownload.setOnClickListener(view -> {
            shareBase64ImageAsPdf(base64Pdf);

        });


        return binding.getRoot();
    }

    private void memberIsLoanGuaranteed(){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);
        authViewModel.memberIsLoanGuaranteed().observe(getViewLifecycleOwner(), listAPIResponse -> {
            binding.llLoading.setVisibility(View.GONE);
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    base64Pdf = listAPIResponse.body().baseReport;

                    previewPdf(base64Pdf);


                }else {
                    binding.llData.setVisibility(View.GONE);
                    binding.llEmpty.setVisibility(View.VISIBLE);

                }

            }else {
                binding.llData.setVisibility(View.GONE);
                binding.llEmpty.setVisibility(View.VISIBLE);
            }
        });
    }


    public void shareBase64ImageAsPdf(String base64Pdf) {
        if (base64Pdf != null){
            PdfUtils.shareBase64Pdf(requireContext(), base64Pdf, "Detailed statement");
        }else {
            Toast.makeText(requireContext(), "Could not download statement", Toast.LENGTH_SHORT).show();
        }
    }

    private void previewPdf(String base64Pdf){

        binding.llData.setVisibility(View.VISIBLE);
        binding.llEmpty.setVisibility(View.GONE);
        binding.tvDownload.setVisibility(View.VISIBLE);

        byte[] pdfData = Base64.decode(base64Pdf, Base64.DEFAULT);

        try {
            // Convert byte array to ByteArrayInputStream
            File tempFile = File.createTempFile("temp_pdf", ".pdf", requireContext().getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(pdfData);
            fos.close();

            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            // Open a PdfRenderer


            // Open the first page
            PdfRenderer.Page page = pdfRenderer.openPage(0);

            // Create a Bitmap object
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

            // Render the page as bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            // Display the bitmap in an ImageView
            binding.imageView.setImageBitmap(bitmap);

            // Close the page and renderer
            page.close();
            pdfRenderer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}