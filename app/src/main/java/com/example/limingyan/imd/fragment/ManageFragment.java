package com.example.limingyan.imd.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.limingyan.imd.R;
import com.example.limingyan.imd.activity.DetailsActivity;
import com.example.limingyan.imd.adapter.MyBaseAdapter;
import com.example.limingyan.imd.entity.Introduction;
import com.example.limingyan.imd.entity.t_goods;
import com.example.limingyan.imd.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageFragment extends Fragment {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.manageList)
    ListView manageList;
    Unbinder unbinder;
    @BindView(R.id.type_choice)
    Spinner typeChoice;
    private List<Introduction> introductionList;
    private MyBaseAdapter adapter;
    private List<t_goods> t_goodsList;

    public ManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        title.setText("管理");
        Bmob.initialize(getActivity(), "3cb928f0c2d0133c575f7564c0d786e0");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        introductionList = new ArrayList<>();
        adapter = new MyBaseAdapter(introductionList);
        manageList.setAdapter(adapter);

        edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String content=edtContent.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)){
                        ((InputMethodManager) edtContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                        getData("goodsName",content);
                        typeChoice.setVisibility(View.GONE);
                    }else {
                        ((InputMethodManager) edtContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                        typeChoice.setVisibility(View.VISIBLE);
                        getData("type",String.valueOf(typeChoice.getSelectedItem()));
                    }
                }
                return false;
            }
        });

        manageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra("name",t_goodsList.get(position).getGoodsName());
                startActivity(intent);
            }
        });

        adapter.setAdapter(new MyBaseAdapter.OnList() {
            @Override
            public View getList(int position, View convertView) {
                View view1=LayoutInflater.from(getContext()).inflate(R.layout.info_list,null);
                TextView price=view1.findViewById(R.id.price);
                TextView standard=view1.findViewById(R.id.standard);
                TextView name=view1.findViewById(R.id.name);
                ImageView commodity_img=view1.findViewById(R.id.commodity_img);

                price.setText(introductionList.get(position).getPrice());
                standard.setText(introductionList.get(position).getStandard());
                name.setText(introductionList.get(position).getName());
                commodity_img.setImageBitmap(introductionList.get(position).getBitmap());
                return view1;
            }
        });
        typeChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getData("type",String.valueOf(typeChoice.getSelectedItem()));
                        break;
                    case 1:
                        getData("type",String.valueOf(typeChoice.getSelectedItem()));

                        break;
                    case 2:
                        getData("type",String.valueOf(typeChoice.getSelectedItem()));

                        break;
                    case 3:
                        getData("type",String.valueOf(typeChoice.getSelectedItem()));

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getData(String column,String type) {
        introductionList.clear();
        BmobQuery<t_goods> query = new BmobQuery<>();
        query.addWhereEqualTo(column,type);
        query.findObjects(new FindListener<t_goods>() {
            @Override
            public void done(final List<t_goods> list, BmobException e) {
                if (e == null){
                    t_goodsList=list;
                    for (int i = 0; i < list.size(); i++) {
                        final int finalI = i;
                        HttpRequest.getBitmap(list.get(i).getImages(), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                Introduction introduction=new Introduction();
                                introduction.setBitmap(bitmap);
                                introduction.setName(list.get(finalI).getGoodsName());
                                introduction.setStandard("规格："+list.get(finalI).getSize());
                                introduction.setPrice("价格："+list.get(finalI).getCurrentPrice()+"元");
                                introductionList.add(introduction);
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                    }
                }else {
                    Log.e("***", String.valueOf(e));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        String content=edtContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)){
            getData("goodsName",content);
            typeChoice.setVisibility(View.GONE);
        }else {
            typeChoice.setVisibility(View.VISIBLE);
            getData("type",String.valueOf(typeChoice.getSelectedItem()));
        }
    }
}
