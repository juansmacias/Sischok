package uniandes.sischok;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import uniandes.sischok.R;
import uniandes.sischok.mundo.Section;
import uniandes.sischok.mundo.SectionItem;


public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener
{
	 private ExpandableListView sectionListView;
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        
	        List<Section> sectionList = createMenu();
	                
	        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
	        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
	        this.sectionListView.setGroupIndicator(null);
	        
	        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
	        this.sectionListView.setAdapter(sectionListAdapter); 
	        
	        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
	              @Override
	              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
	                return true;
	              }
	            });
	        
	        this.sectionListView.setOnChildClickListener(this);
	        
	        int count = sectionListAdapter.getGroupCount();
	        for (int position = 0; position < count; position++) {
	            this.sectionListView.expandGroup(position);
	        }
	        
	        return view;
	    }

	    private List<Section> createMenu() {
	        List<Section> sectionList = new ArrayList<Section>();	        
	        Section oTitulo = new Section("Turistiando");
	        oTitulo.addSectionItem(101, "Perfil", null);
	        oTitulo.addSectionItem(102, "Comunidad",null);	        
	        sectionList.add(oTitulo);
	        return sectionList;
	    }

	    @Override
	    public boolean onChildClick(ExpandableListView parent, View v,
	            int groupPosition, int childPosition, long id) {
//            Intent in = new Intent();
//	        switch ((int)id) {
//	        case 101:
//
//	            in.setClass(parent.getContext(), Perfil.class);
//	            startActivity(in);
//	            break;
//	        case 102:
//	           in.setClass(parent.getContext(), Comunidad.class);
//	           startActivity(in);
//	           break;
//	        }
//	        if(id>200&&id<300)
//	        {
//	        	
//	        	int posi =childPosition;
//	        	in.putExtra("CIUDAD_PUESTO", posi);
//	        	in.setClass(parent.getContext(), Ciudad.class);
//		        startActivity(in);
//	        }
	        return false;
	    }
}
