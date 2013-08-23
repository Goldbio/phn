package com.geference;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExamFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		
		return inflater.inflate(R.layout.exam_fragment, container,  false);
	}

}
