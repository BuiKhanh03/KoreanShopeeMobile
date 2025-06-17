package com.example.koreanshopee.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.OrderHistory.OrderHistoryAdapter;
import com.example.koreanshopee.OrderHistory.OrderItem;
import com.example.koreanshopee.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveredFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderItem> orderItemList;

    public DeliveredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        recyclerView = view.findViewById(R.id.recycler_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderItemList = new ArrayList<>();
        orderItemList.add(new OrderItem(R.drawable.book1, "한국 능력 시험", "다티하이옌", "900.000", "x1"));
        orderItemList.add(new OrderItem(R.drawable.book1, "TOPIK II 마스터", "홍길동", "700.000", "x2"));

        adapter = new OrderHistoryAdapter(getContext(), orderItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
