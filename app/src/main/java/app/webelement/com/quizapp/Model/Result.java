package app.webelement.com.quizapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ADMIN on 3/22/16.
 */
public class Result implements Parcelable {
    private String answer;
    private boolean isCorrect;
    private String question;
    private String correct_answer;

    public Result() {
        super();
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    protected Result(Parcel in) {
        answer = in.readString();
        question = in.readString();
        correct_answer = in.readString();
        isCorrect = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(answer);
        dest.writeString(question);
        dest.writeString(correct_answer);
        dest.writeByte((byte) (isCorrect ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }
}
