package com.example.medialab.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medialab.Model.StudentVO;
import com.example.medialab.R;

import java.util.ArrayList;

/** Recyclerview에서 사용될 adapter입니다.
 *

 */

public class CurrentUserAdapter extends RecyclerView.Adapter<CurrentUserAdapter.ViewHolder> {

    private OnItemClickListener clickListener = null;
    private ArrayList<StudentVO> studentList = null ;

    public CurrentUserAdapter(ArrayList<StudentVO> studentList) {
        this.studentList = studentList ;
    }

    /*----------------------------Recyclerview 필수 메소드--------------------------*/

    @NonNull
    @Override
    public CurrentUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recycle_item, parent, false) ;
        CurrentUserAdapter.ViewHolder viewHolder = new CurrentUserAdapter.ViewHolder(view) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentUserAdapter.ViewHolder holder, int position) {

        StudentVO student = studentList.get(position);

        holder.nameText.setText(student.getName());
        holder.idText.setText(String.valueOf(student.getStudentId()));
        holder.departmentText.setText(student.getDepartment());
        holder.entranceTimeText.setText(student.getEntranceTime());
        holder.computerNumberText.setText(student.getComputerNumber());
        holder.purposeText.setText(student.getPurpose());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    /*----------------------------ViewHolder 클래스---------------------------------*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameText ;
        TextView idText;
        TextView departmentText;
        TextView entranceTimeText;
        TextView computerNumberText;
        TextView purposeText;

        ViewHolder(View itemView) {
            super(itemView) ;

            nameText = (TextView)itemView.findViewById(R.id.recycler_item_name);
            idText = (TextView)itemView.findViewById(R.id.recycler_item_id);
            departmentText = (TextView)itemView.findViewById(R.id.recycler_item_department);
            entranceTimeText = (TextView)itemView.findViewById(R.id.recycler_item_entrance_time);
            computerNumberText = (TextView)itemView.findViewById(R.id.recycler_item_computer_number);
            purposeText = (TextView)itemView.findViewById(R.id.recycler_item_purpose);

            purposeText.setSelected(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if(clickListener != null)
                            clickListener.onItemClick(v,pos);
                    }
                }
            });
        }
    }

    /*----------------------------클릭 리스너 관련---------------------------------*/

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    /*---------------------------------기타 메소드-------------------------------*/

    public StudentVO getStudentVO(int position){
        return studentList.get(position);
    }

    public void setStudentList(ArrayList<StudentVO> studentList){ this.studentList = studentList;}
}
