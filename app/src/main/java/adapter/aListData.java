package adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fcfas.firebase.R;
import com.fcfas.firebase.itemdata;

import java.util.List;

import model.Objek;

public class aListData extends RecyclerView.Adapter<aListData.MyViewHolder> {

    private List<Objek> users;
    private Activity mActivity;

    public aListData(List<Objek> users, Activity mActivity) {
        this.users = users;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View objek = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item,viewGroup,false);
        return new MyViewHolder(objek);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Objek user = users.get(i);

        myViewHolder.tv_Nama.setText(user.getNama());
        myViewHolder.tv_Kelas.setText(user.getKelas());
        myViewHolder.tv_Pesan.setText(user.getPesan());

        //membuat item menjadi dapat di klik
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //membuka activity itemdata dengan parsing parameter dengan nama dan didapatkan
                //dari objek user

                Intent itemData = new Intent(mActivity, itemdata.class);
                itemData.putExtra("Key",user.getKey());
                itemData.putExtra("Nama",user.getNama());
                itemData.putExtra("Kelas",user.getKelas());
                itemData.putExtra("Pesan",user.getPesan());
                mActivity.startActivity(itemData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        public TextView tv_Nama,tv_Kelas,tv_Pesan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_item);
            tv_Nama = itemView.findViewById(R.id.tv_Nama);
            tv_Kelas = itemView.findViewById(R.id.tv_Kelas);
            tv_Pesan = itemView.findViewById(R.id.tv_Pesan);
        }
    }
}
