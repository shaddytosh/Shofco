package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.SecurityQuestion;


public class SecurityQuestionResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;
    @SerializedName("data")
    public List<SecurityQuestion> securityQuestions;

}
