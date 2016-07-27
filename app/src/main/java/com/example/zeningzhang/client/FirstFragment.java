package com.example.zeningzhang.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<TradingItems> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter mAdapter;
    private JSONObject listOfItem;


    private OnFragmentInteractionListener mListener;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void prepareMovieData(JSONObject listOfItem) throws JSONException {
        for(int i=0;i<listOfItem.getJSONArray("items").length();i++)
        {
            JSONObject obj = (JSONObject) listOfItem.getJSONArray("items").get(i);
            Log.d("zznmizzouItem",obj.get("itemName").toString());
            TradingItems item = new TradingItems(obj.get("itemName").toString(),obj.get("itemType").toString(),obj.get("itemDate").toString());
            itemList.add(item);
        }
//        TradingItems item = new TradingItems("google","Internet","2015");
//        itemList.add(item);
//
//        item = new TradingItems("apple","Internet","2015");
//        itemList.add(item);
//
//        item = new TradingItems("Amazon","Internet","2015");
//        itemList.add(item);
//
//        item = new TradingItems("Facebook","Internet","2015");
//        itemList.add(item);
//
//
//        item = new TradingItems("Hulu","Internet","2015");
//        itemList.add(item);

        mAdapter.notifyDataSetChanged();


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        mAdapter = new ItemAdapter(itemList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Map<String,String> map = new HashMap<>();
        map.put("status","viewItem");

        String url = HttpUtil.BASE_URL;
        try {
            String temp = HttpUtil.postRequest(url,map);
//            Log.d("zznmizzou",temp);
            listOfItem = new JSONObject(temp);


        } catch (Exception e) {
            e.printStackTrace();
        }
        if(listOfItem!=null)
        {
            try {
                prepareMovieData(listOfItem);
                Log.d("zznmizzou",listOfItem.getJSONArray("items").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
