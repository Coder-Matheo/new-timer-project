package rob.myappcompany.timer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    List<String> timeListForRecyclerAdapter;
    List<String> timeDescriptionListForRecyclerAdapter;
    List<byte[]> timeImageListForRecyclerAdapter;
    RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerAdapter(List<String> timeList, List<String> timedescriptionListParam, ArrayList<byte[]> imageTimeListParam, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.timeListForRecyclerAdapter = timeList;
        this.timeDescriptionListForRecyclerAdapter = timedescriptionListParam;
        this.timeImageListForRecyclerAdapter = imageTimeListParam;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.textView1.setText(timeDescriptionListForRecyclerAdapter.get(position));
        holder.textView1.setText(String.valueOf(position));
        holder.textView2.setText(timeListForRecyclerAdapter.get(position));
        holder.imageView.setImageBitmap(byteImageToBitmap(timeImageListForRecyclerAdapter.get(position)));
    }

    public Bitmap byteImageToBitmap(byte[] imgOfByteParam){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgOfByteParam, 0, imgOfByteParam.length);
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return timeListForRecyclerAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1, textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.rowTextView);

            //This setOnClickListener is from Android
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //call and get position Item for class/function recyclerViewClickInterface
                    recyclerViewClickInterface.onItemClickInterface(getAdapterPosition());
                }
            });
        }
    }
}
