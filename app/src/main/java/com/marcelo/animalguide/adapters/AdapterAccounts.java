package com.marcelo.animalguide.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.classes.UserGoogle;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.MyViewHolder>
{
    private List<UserClass> listUsers;
    private Context context;
    private String typeUser, getEmailBundle, idPhotos;

    private StorageReference imageReference, storageReference = ServicesFirebase.getFirebaseStorage();

    public AdapterAccounts(List<UserClass> listUsers, Context context, String typeUser, String getEmail)
    {
        this.listUsers = listUsers;
        this.context = context;
        this.typeUser = typeUser;
        this.getEmailBundle = getEmail;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_accouns, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        if (typeUser.equals("Pet Owner"))
        {
            holder.email.setText(getEmailBundle);
            holder.typeUser.setText(R.string.owner_pet_text);

            idPhotos = EncryptionSHA1.encryptionString(getEmailBundle);

            imageReference = storageReference
                    .child("Cadastro do Usuário")
                    .child(Objects.requireNonNull(idPhotos))
                    .child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    if (uri != null)
                    {
                        Picasso.get().load(uri).into(holder.imageUser);
                    }
                    else
                    {
                        holder.imageUser.setImageResource(R.drawable.photo_profile);
                    }
                }
            });
        }
        else if (typeUser.equals("Student"))
        {
            holder.email.setText(getEmailBundle);
            holder.typeUser.setText(R.string.type_user_student_adapter_accounts);

            idPhotos = EncryptionSHA1.encryptionString(getEmailBundle);

            imageReference = storageReference
                    .child("Cadastro do Usuário")
                    .child(Objects.requireNonNull(idPhotos))
                    .child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    if (uri != null)
                    {
                        Picasso.get().load(uri).into(holder.imageUser);
                    }
                    else
                    {
                        holder.imageUser.setImageResource(R.drawable.photo_profile);
                    }
                }
            });
        }
        else if (typeUser.equals("Veterinary"))
        {
            holder.email.setText(getEmailBundle);
            holder.typeUser.setText(R.string.veterinary_text);

            idPhotos = EncryptionSHA1.encryptionString(getEmailBundle);

            imageReference = storageReference
                    .child("Cadastro do Usuário")
                    .child(Objects.requireNonNull(idPhotos))
                    .child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    if (uri != null)
                    {
                        Picasso.get().load(uri).into(holder.imageUser);
                    }
                    else
                    {
                        holder.imageUser.setImageResource(R.drawable.photo_profile);
                    }
                }
            });
        }
        else if (typeUser.equals("ONG"))
        {
            holder.email.setText(getEmailBundle);
            holder.typeUser.setText(R.string.type_user_ong_adapter_accounts);

            idPhotos = EncryptionSHA1.encryptionString(getEmailBundle);

            imageReference = storageReference
                    .child("Cadastro do Usuário")
                    .child(Objects.requireNonNull(idPhotos))
                    .child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    if (uri != null)
                    {
                        Picasso.get().load(uri).into(holder.imageUser);
                    }
                    else
                    {
                        holder.imageUser.setImageResource(R.drawable.photo_profile);
                    }
                }
            });
        }
        holder.email.setEnabled(false);
    }

    @Override
    public int getItemCount()
    {
        return listUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText email;
        private TextView typeUser;
        private CircleImageView imageUser;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            email = itemView.findViewById(R.id.editTextAdapterEmailAccountsUser);
            typeUser = itemView.findViewById(R.id.textViewAdapterTypeAccount);
            imageUser = itemView.findViewById(R.id.imageViewAdapterAccountsUser);
        }
    }
}
