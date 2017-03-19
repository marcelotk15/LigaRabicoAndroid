package com.ligarabico.ligarabico;

import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Marcelo on 17/03/2017.
 */

public class CustomViewBinder implements SimpleAdapter.ViewBinder{
    @Override
    public boolean setViewValue(View view, Object inputData, String textRepresentation) {
        int id = view.getId();
        String data = (String) inputData;

        switch (id) {
            case R.id.avatar:
                populateAvatar(view, data);
                break;
            case R.id.clubImage:
                populateCLubImage(view, data);
                break;
            case R.id.name:
                populateName(view, data);
                break;
            case R.id.rating:
                populateRating(view, data);
                break;
            case R.id.position:
                populatePostion(view, data);
                break;
            case R.id.club:
                populateClub(view, data);
                break;
        }

        return true;
    }

    public void populateAvatar(View view, String imageData)
    {
        ImageView imageView = (ImageView) view.findViewById(R.id.avatar);
        Picasso.with(view.getContext()).load(imageData).into(imageView);
    }

    public void populateCLubImage(View view, String imageData)
    {
        ImageView imageView = (ImageView) view.findViewById(R.id.clubImage);
        Picasso.with(view.getContext()).load(imageData).into(imageView);
    }

    public void populateName(View view, String nameData)
    {
        TextView textView = (TextView) view.findViewById(R.id.name);
        textView.setText(nameData);
    }

    public void populateRating(View view, String nameData)
    {
        TextView textView = (TextView) view.findViewById(R.id.rating);
        textView.setText(nameData);

        int overall = Integer.parseInt(nameData);

        if (overall >= 90 ) {
            textView.setBackgroundResource(R.drawable.overall_shape_90);
        } else if (overall >= 80) {
            textView.setBackgroundResource(R.drawable.overall_shape_80);
        } else if (overall >= 66) {
            textView.setBackgroundResource(R.drawable.overall_shape_66);
        } else if (overall >= 50) {
            textView.setBackgroundResource(R.drawable.overall_shape_50);
        }
    }

    public void populatePostion(View view, String nameData)
    {
        TextView textView = (TextView) view.findViewById(R.id.position);
        textView.setText(nameData);

    }

    public void populateClub(View view, String nameData)
    {
        TextView textView = (TextView) view.findViewById(R.id.club);
        textView.setText(nameData);
    }
}
