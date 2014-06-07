package dev.blacksheep.trif.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import dev.blacksheep.trif.EventsActivity;
import dev.blacksheep.trif.R;
import dev.blacksheep.trif.adapters.PlacesOfInterestAdapter;
import dev.blacksheep.trif.classes.Item;

public class PlacesOfInterestFragment extends Fragment {

	ArrayList<Item> gridArray = new ArrayList<Item>();
	PlacesOfInterestAdapter customGridAdapter;

	public PlacesOfInterestFragment() {
		Log.e("PLACES ON INTEREST", "PUBLIC");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		String type = bundle.getString("type");
		View rootView = inflater.inflate(R.layout.places_of_interest_layout, container, false);
		GridView gvPlaces = (GridView) rootView.findViewById(R.id.gvPlaces);
		if (type.equals("Nature & Wildlife")) {
			gridArray.add(new Item("Zoo", "http://www.onthewaytosomewhere.com/wp-content/uploads/2010/02/CIMG1005.jpg"));
			gridArray.add(new Item("Botanical Garden", "http://traveldelight.biz/components/com_virtuemart/shop_image/product/Singapore_Botani_50f176187af66.jpg"));
			gridArray.add(new Item("River Safari", "http://tripandtravelblog.com/wp-content/uploads/2013/04/River-Safari.jpg"));
			gridArray.add(new Item("Night Safari", "http://www.nightsafari.com.sg/images/home/banner-attractions-tramride.jpg"));
			gridArray.add(new Item("BirdPark", "http://www.kp-away.com/image/place/3/DSC01133.jpg"));
			gridArray.add(new Item("Garden By The Bay", "http://www.storyofbing.com/pics7/ff3818_flower_dome_gardens_by_the_bay_singapore.jpg"));

		} else if (type.equals("Leisure & Entertainment")) {
			gridArray.add(new Item("Sentosa", "http://www.sentosa.com.sg/uploadedImages/en/Getting_to_and_around_Sentosa/getting_to_around_masthead01.jpg"));
			gridArray.add(new Item("Singapore Flyer", "http://therealsingapore.com/sites/default/files/field/image/6313619731_ec1f566f0b_z.jpg"));
			gridArray.add(new Item("USS", "http://www.bestselling.sg/wp-content/uploads/unversal-studio.jpeg"));
			gridArray.add(new Item("MBS Casino", "http://www.straitstimes.com/sites/straitstimes.com/files/RWS1501e.jpg"));
			gridArray.add(new Item("Underwater World", "http://singaporecity360.com/wp-content/uploads/2011/06/Underwater-World-and-Dolphin-Lagoon-1.jpg"));

		} else if (type.equals("Arts & Discovery")) {
			gridArray.add(new Item("National Museum", "http://www.enlightermagazine.com/images/2009/10/shm_01.jpg"));
			gridArray.add(new Item("Discovery Centre", "http://www.saex.com.sg/admin/UserFiles/AttractionGallery/Discovery%20centre%20(2).png"));
			gridArray.add(new Item("Science Centre", "http://cdn.sheylara.com/images09/0404ssc.jpg"));
		} else if (type.equals("Shopping")) {
			gridArray.add(new Item("Bugis Street", "http://loctravel.files.wordpress.com/2012/02/bugis-street.jpg"));
			gridArray.add(new Item("ION Orchard", "http://media.timeoutsingapore.com/contentFiles/image/venues/ion-orchard-1-482x298.jpg"));
			gridArray.add(new Item("Orchard Central", "http://www.controltechasia.com/images/orchard%20central/2.jpg"));
			gridArray.add(new Item("313 Sommerset", "http://www.yoursingapore.com/content/traveller/en/browse/shopping/shopping-malls/313-at-somerset/_jcr_content/html/image.img.png"));
		} else if (type.equals("Dining")) {
			gridArray.add(new Item("East Coast Lagoon Food Village", "http://cdn.designhome.pics/design/graphics8.nytimes.com/images/2007/12/20/travel/v-lagoon-ext395.1.jpg"));
			gridArray.add(new Item("Fordham & Grand", "http://www.asia-bars.com/wp-content/uploads/2013/04/05-singapore_688.jpg"));
			gridArray.add(new Item("Maxwell Road Hawker Centre", "http://lifestylewiki.com/images/c/cb/Maxwell_Road_Food_Centre_02.jpg"));
			gridArray.add(new Item("Chinatown Food Street", "http://www.soshiok.com/sites/default/files/public/styles/news_thumb/public/article/images/featured/chinatfs_np.jpg?itok=YOx-GZ0K"));
		} else if (type.equals("Nightlife")) {
			gridArray.add(new Item("Zouk", "http://stcommunities.straitstimes.com/sites/default/files/photos/zouk2602e.jpg"));
			gridArray.add(new Item("Butter Factory", "http://media.timeoutsingapore.com/contentFiles/image/venues/butter-factory-1-482x402.jpg"));
			gridArray.add(new Item("timbre", "http://www.guidegecko.com/content/poi/608168455/dk2bp-timbre-the-arts-house-t4.JPG"));
			gridArray.add(new Item("Home Club", "http://www.yoursingapore.com/content/traveller/en/browse/see-and-do/nightlife/dance-clubs/home-club/_jcr_content/html/image.img.png"));
			gridArray.add(new Item("Arena", "http://www.eguide.com.sg/Uploads/Companies/The-Arena_694_image.jpg"));
			gridArray.add(new Item("Azzura", "https://2.bp.blogspot.com/-jsPKDuw4_dA/UpA5oHGjDUI/AAAAAAAABLg/iAHqdUNqV10/s1600/beach+party.jpg"));
		} else {
			gridArray.add(new Item("Azzura", "https://2.bp.blogspot.com/-jsPKDuw4_dA/UpA5oHGjDUI/AAAAAAAABLg/iAHqdUNqV10/s1600/beach+party.jpg"));
		}

		customGridAdapter = new PlacesOfInterestAdapter(getActivity(), R.layout.places_of_interest_item, gridArray);
		gvPlaces.setAdapter(customGridAdapter);
		Log.e("FRAGMENT", "done");
		gvPlaces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
				TextView tvName = (TextView) view.findViewById(R.id.tvPlacesName);
				TextView tvImage = (TextView) view.findViewById(R.id.tvImage);
				startActivity(new Intent(getActivity(), EventsActivity.class).putExtra("name", tvName.getText().toString()).putExtra("image", tvImage.getText().toString()));
				getActivity().overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
			}
		});
		return rootView;
	}

}
