package com.example.sih2;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arasthel.asyncjob.AsyncJob;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CompProfileFragment extends Fragment {

    ImageView company_image;
    View previewDpView;
    TextView firstname, lastname, username, email;
    SharedPrefrencesHelper sharedPrefrencesHelper;

    private RequestQueue rQueue;
    private String imageEncoded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_comp_profile, container, false);

        //Start of the classs
        sharedPrefrencesHelper = new SharedPrefrencesHelper(getActivity());
        firstname = view.findViewById(R.id.emp_firstname);
        lastname = view.findViewById(R.id.emp_lastname);
        username = view.findViewById(R.id.emp_username);
        email = view.findViewById(R.id.emp_email);
        company_image = view.findViewById(R.id.company_image);

        //default refresh functions
        updateDisplayProfile();

        company_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(CompProfileFragment.this.getActivity(), company_image);
                popup.getMenuInflater().inflate(R.menu.popup_edit, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CompProfileFragment.this.getActivity());
                        final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_edit_display_picture, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        ImageView imageView = view.findViewById(R.id.company_image);
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        final ImageView displayPicture;
                        Button cancel, delete, edit, submit;
                        displayPicture = dialogView.findViewById(R.id.displayPicture);
                        previewDpView = dialogView;
                        displayPicture.setImageBitmap(bitmap);
                        cancel = dialogView.findViewById(R.id.cancel);
                        delete = dialogView.findViewById(R.id.delete);
                        edit = dialogView.findViewById(R.id.edit);
                        submit = dialogView.findViewById(R.id.apply);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getPreview();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDisplayProfile();
                                alertDialog.cancel();
                            }
                        });
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

        //End of the Class
        return view;
    }


    private void setDisplayProfile() {
                ImageView previewImage;
                previewImage = previewDpView.findViewById(R.id.displayPicture);
                final Bitmap bitmap = ((BitmapDrawable) previewImage.getDrawable()).getBitmap();
                company_image.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] b = byteArrayOutputStream.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "setDisplayProfile.php",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {

                                        Toast.makeText(getActivity(), "Image upload success", Toast.LENGTH_SHORT).show();
                                       updateDisplayProfile();
                                    } else {
                                       // Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(CompProfileFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CompProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", sharedPrefrencesHelper.getUsername());
                        params.put("imageEncoded", imageEncoded);
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(CompProfileFragment.this.getActivity());
                rQueue.add(stringRequest3);
    }


    private void updateDisplayProfile() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                //WRITE THE CODE HERE
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getDisplayProfile.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        //Toast.makeText(getActivity(), "Image upload success", Toast.LENGTH_SHORT).show();
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                                        imageEncoded = jsonObject1.getString("imagelocation");
                                        byte[] decodedByte = Base64.decode(imageEncoded, 0);
                                        final Bitmap svdimg = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                                        company_image.setImageBitmap(svdimg);
                                    } else {
                                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", sharedPrefrencesHelper.getUsername());
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(getActivity());
                rQueue.add(stringRequest3);

                final boolean result = true;
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        // Toast.makeText(getActivity(), "Result was: ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getPreview() {
        CropImage.ActivityBuilder activity = CropImage.activity();
        activity.setGuidelines(CropImageView.Guidelines.ON);
        activity.setAspectRatio(1, 1);
        activity.start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();
            int select = 0;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                int nh = (int) (bitmap.getHeight() * (250.0 / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, 250, nh, true);
                //Bitmap image = bitmap;
                //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                //byte[] b = byteArrayOutputStream.toByteArray();
                //imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                // SharedPreferences.Editor editor = prefs.edit();
                //editor.putString("namePreferance", itemNAme);
                //  editor.putString("svdimgs", imageEncoded);
                // editor.apply();
                //displayPicture.setImageBitmap(bitmap);
                //Glide.with(this).load(path).into(profileImg);
                select = 1;
                final Bitmap finalBitmap = bitmap;
                ImageView previewDp = previewDpView.findViewById(R.id.displayPicture);
                previewDp.setImageBitmap(finalBitmap);
                company_image.setImageBitmap(finalBitmap);

            } catch (IOException e) {
                select = 0;
            }

        }
    }
}

/*
AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
                @Override
                public void doOnBackground() {

                //WRITE THE CODE HERE
                    final boolean result = true;
                    AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                        @Override
                        public void doInUIThread() {
                            // Toast.makeText(getActivity(), "Result was: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
*/