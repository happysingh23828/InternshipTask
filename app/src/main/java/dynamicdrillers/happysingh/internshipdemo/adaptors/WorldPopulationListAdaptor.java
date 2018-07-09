package dynamicdrillers.happysingh.internshipdemo.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dynamicdrillers.happysingh.internshipdemo.ImageFullViewActivity;
import dynamicdrillers.happysingh.internshipdemo.R;
import dynamicdrillers.happysingh.internshipdemo.models.WorldPopulationModel;
import dynamicdrillers.happysingh.internshipdemo.models.Worldpopulation;

public class WorldPopulationListAdaptor extends RecyclerView.Adapter<WorldPopulationListAdaptor.WorldPopulationViewHolder> {

    List<Worldpopulation> worldPopulationModels;
    Context context;

    public WorldPopulationListAdaptor(List<Worldpopulation> worldPopulationModels, Context context) {
        this.worldPopulationModels = worldPopulationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public WorldPopulationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_countrylist_item,parent,false);
        return  new WorldPopulationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorldPopulationViewHolder holder, final int position) {

        final Boolean imageFullCheck = null;
       holder.country.setText(worldPopulationModels.get(position).getCountry());
       holder.population.setText(worldPopulationModels.get(position).getPopulation());
       holder.rank.setText(worldPopulationModels.get(position).getRank().toString());
       Picasso.get().load(worldPopulationModels.get(position).getFlag()).into(holder.countryFlag);

       holder.countryFlag.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(context, ImageFullViewActivity.class);
               intent.putExtra("flag",worldPopulationModels.get(position).getFlag());
               context.startActivity(intent);

           }
       });


    }

    @Override
    public int getItemCount() {
        return worldPopulationModels.size();
    }

    public  class  WorldPopulationViewHolder extends RecyclerView.ViewHolder{

        ImageView countryFlag;
        TextView rank,population,country;


        public WorldPopulationViewHolder(View itemView) {
            super(itemView);

            countryFlag = (ImageView)itemView.findViewById(R.id.img_flag);
            rank = (TextView)itemView.findViewById(R.id.txt_rank);
            population = (TextView)itemView.findViewById(R.id.txt_population);
            country = (TextView)itemView.findViewById(R.id.txt_country_name);
        }
    }
}
