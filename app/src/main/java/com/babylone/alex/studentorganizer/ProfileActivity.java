package com.babylone.alex.studentorganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    EditText firstName, lastName;
    Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, universitiesRef;
    CircleImageView profileImage;
    private Uri profilePhotoUri;
    Spinner university, faculty, branch, course;

    final static int GALLERY_PICK = 1;
    ProgressDialog loadingBar;
    ArrayList<String> universitiesList, facultiesList, branchesList;

    private StorageReference userProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        university = findViewById(R.id.profileUniversity);
        faculty = findViewById(R.id.profileFaculty);
        branch = findViewById(R.id.profileBranch);
        course = findViewById(R.id.profileCourse);
        course.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5"}));
        universitiesRef = FirebaseDatabase.getInstance().getReference().child("Universities");
        universitiesList = new ArrayList<>();
        facultiesList = new ArrayList<>();
        branchesList = new ArrayList<>();

        universitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                universitiesList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    universitiesList.add(data.getKey());
                }
                university.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, universitiesList));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        university.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universitiesRef.child(universitiesList.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        facultiesList.clear();
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            facultiesList.add(data.getKey().toString());
                        }
                        faculty.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, facultiesList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                universitiesRef.child(university.getSelectedItem().toString()).child(facultiesList.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        branchesList.clear();
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            branchesList.add(data.getValue().toString());
                        }
                        branch.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, branchesList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        firstName = findViewById(R.id.profileFirstName);
        lastName = findViewById(R.id.profileLastName);

        profileImage = findViewById(R.id.profileImage);
        loadingBar = new ProgressDialog(this);
        saveButton = findViewById(R.id.profileSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap postMap = new HashMap();
                postMap.put("first_name", firstName.getText().toString());
                postMap.put("last_name", lastName.getText().toString());
                postMap.put("university", university.getSelectedItem().toString());
                postMap.put("faculty", faculty.getSelectedItem().toString());
                postMap.put("branch", branch.getSelectedItem().toString());
                postMap.put("course", course.getSelectedItem().toString());
                postMap.put("group", faculty.getSelectedItem().toString()+"_"+branch.getSelectedItem().toString()+"_"+course.getSelectedItem().toString());
                postMap.put("role", "student");
                String key = usersRef.child(mAuth.getUid()).push().getKey();
                usersRef.child(mAuth.getUid()).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {// додаємо рецепт в бд
                        if (task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Error: "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crop.pickImage(ProfileActivity.this);
            }
        });

        usersRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {// якщо користувач є в бд заповняє поля його данними
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.hasChild("profileimage")){
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).into(profileImage);
                    }
                    if (dataSnapshot.hasChild("first_name")){
                        firstName.setText(dataSnapshot.child("first_name").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("last_name")){
                        lastName.setText(dataSnapshot.child("last_name").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {// вікно вибору фото та збереження в хранилище
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK){
            Crop.of(data.getData(), Uri.fromFile(new File(getCacheDir(),"cropped "))).asSquare().start(this);
            profileImage.setImageURI(Crop.getOutput(data));

        }else if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri uri = Crop.getOutput(data);
            profileImage.setImageURI(uri);

            profilePhotoUri = uri;

            StorageReference filePath = userProfileImageRef.child(mAuth.getUid() + ".png");
            loadingBar.setMessage("Saving photo");
            loadingBar.show();
            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {// пересилання файлу в firebase
                    if (task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this, "Photo saved successfully", Toast.LENGTH_SHORT).show();
                        final String downloadUrl = task.getResult().getDownloadUrl().toString();
                        usersRef.child(mAuth.getUid()).child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ProfileActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ProfileActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                loadingBar.dismiss();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
