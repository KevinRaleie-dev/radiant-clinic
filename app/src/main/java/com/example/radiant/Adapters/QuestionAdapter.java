package com.example.radiant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiant.Classes.Questions;
import com.example.radiant.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class QuestionAdapter extends FirestoreRecyclerAdapter<Questions, QuestionAdapter.QuestionHolder > {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public QuestionAdapter(@NonNull FirestoreRecyclerOptions<Questions> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionHolder questionHolder, int i, @NonNull Questions questions) {
        questionHolder.tv_question.setText(questions.getQuestion());
        questionHolder.tv_answer.setText(questions.getAnswer());
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);

        return new QuestionHolder(view);
    }

    class QuestionHolder extends RecyclerView.ViewHolder
    {
        TextView tv_question;
        TextView tv_answer;


        public QuestionHolder(@NonNull View itemView) {
            super(itemView);

            tv_question = itemView.findViewById(R.id.tv_question);
            tv_answer = itemView.findViewById(R.id.tv_answer);
        }
    }
}
