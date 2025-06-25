package com.example.koreanshopee.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koreanshopee.Item;
import com.example.koreanshopee.ItemAdapter;
import com.example.koreanshopee.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    public HomeFragment() {}

    private TextView textGreeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView iconChat = view.findViewById(R.id.icon_chat);
        iconChat.setOnClickListener(v -> {
            Fragment chatFragment = new ChatFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });

        textGreeting = view.findViewById(R.id.text_greeting);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour < 12) {
            greeting = "Chào buổi sáng!";
        } else if (hour < 18) {
            greeting = "Chào buổi chiều!";
        } else {
            greeting = "Chào buổi tối!";
        }

        textGreeting.setText(greeting);

        View sectionBooks = view.findViewById(R.id.section_top_books);
        TextView tvBookTitle = sectionBooks.findViewById(R.id.tvSectionTitle);
        tvBookTitle.setText("Top sách hay");

        RecyclerView recyclerBooks = sectionBooks.findViewById(R.id.recyclerSectionList);

        List<Item> bookList  = new ArrayList<>();
        bookList.add(new Item("Tự học tiếng Hàn", "Minh Trang", 90000, R.drawable.book1));
        bookList.add(new Item("Ngữ pháp tiếng Hàn", "Lee Ji Eun", 120000, R.drawable.book2));
        bookList.add(new Item("Luyện nghe", "Park Soo Jin", 85000, R.drawable.book3));

        ItemAdapter bookAdapter  = new ItemAdapter(getContext(), bookList );
        recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerBooks.setAdapter(bookAdapter );

        View sectionCourses = view.findViewById(R.id.section_top_courses);
        TextView tvLectureTitle = sectionCourses.findViewById(R.id.tvSectionTitle);
        tvLectureTitle.setText("Top bài giảng");

        RecyclerView recyclerCoures = sectionCourses.findViewById(R.id.recyclerSectionList);

        List<Item> courseList  = new ArrayList<>();
        courseList.add(new Item("Tiếng Hàn sơ cấp", "Lee Eun Soo", 1120000, R.drawable.book3));
        courseList.add(new Item("Tiếng Hàn sơ cấp", "Lee Ji Eun", 1320000, R.drawable.book1));
        courseList.add(new Item("Luyện thi TOPIK 3", "MINN", 8500000, R.drawable.book2));

        ItemAdapter courseAdapter  = new ItemAdapter(getContext(), courseList);
        recyclerCoures.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerCoures.setAdapter(courseAdapter );

        return view;
    }
}

