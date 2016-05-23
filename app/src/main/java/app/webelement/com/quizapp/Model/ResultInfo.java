package app.webelement.com.quizapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ADMIN on 3/23/16.
 */

public class ResultInfo implements Parcelable {



    private String correct_count;
    private String incorrect_count;
    private String total_count;

    private ArrayList<Result> resultArrayList;

    public String getCorrect_count() {
        return correct_count;
    }

    public void setCorrect_count(String correct_count) {
        this.correct_count = correct_count;
    }

    public String getIncorrect_count() {
        return incorrect_count;
    }

    public void setIncorrect_count(String incorrect_count) {
        this.incorrect_count = incorrect_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public ArrayList<Result> getResultArrayList() {
        return resultArrayList;
    }

    public void setResultArrayList(ArrayList<Result> resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

    public ResultInfo() {
        super();
    }

    protected ResultInfo(Parcel in) {
        correct_count = in.readString();
        incorrect_count = in.readString();
        total_count = in.readString();
        if (in.readByte() == 0x01) {
            resultArrayList = new ArrayList<Result>();
            in.readList(resultArrayList, Result.class.getClassLoader());
        } else {
            resultArrayList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(correct_count);
        dest.writeString(incorrect_count);
        dest.writeString(total_count);
        if (resultArrayList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(resultArrayList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ResultInfo> CREATOR = new Parcelable.Creator<ResultInfo>() {
        @Override
        public ResultInfo createFromParcel(Parcel in) {
            return new ResultInfo(in);
        }

        @Override
        public ResultInfo[] newArray(int size) {
            return new ResultInfo[size];
        }
    };
}